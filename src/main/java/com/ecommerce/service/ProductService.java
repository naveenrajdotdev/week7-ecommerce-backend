package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.dto.ProductDTO;
import com.ecommerce.model.entity.Category;
import com.ecommerce.model.entity.Product;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable("products")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductDTO getById(Long id) {
        return productRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO create(ProductDTO dto) {
        Product product = Product.builder()
                .name(dto.getName()).description(dto.getDescription())
                .price(dto.getPrice()).stock(dto.getStock())
                .category(Category.builder().id(dto.getCategoryId()).build())
                .build();
        return toDTO(productRepository.save(product));
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return toDTO(productRepository.save(product));
    }

    @CacheEvict(value = "products", allEntries = true)
    public void delete(Long id) { productRepository.deleteById(id); }

    public void reduceStock(Long productId, int qty) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (p.getStock() < qty) throw new InsufficientStockException("Not enough stock for: " + p.getName());
        p.setStock(p.getStock() - qty);
        productRepository.save(p);
    }

    private ProductDTO toDTO(Product p) {
        return ProductDTO.builder().id(p.getId()).name(p.getName())
                .description(p.getDescription()).price(p.getPrice()).stock(p.getStock())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .build();
    }
}