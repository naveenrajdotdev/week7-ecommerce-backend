-- V3__add_indexes.sql
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_orders_user      ON orders(user_id);
CREATE INDEX idx_orders_status    ON orders(status);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_payments_order   ON payments(order_id);