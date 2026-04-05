CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          category VARCHAR(255),
                          price NUMERIC(10,2),
                          image VARCHAR(255),
                          in_stock BOOLEAN NOT NULL,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER products_set_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TABLE carts (
                       id BIGSERIAL PRIMARY KEY,
                       session_id VARCHAR(255),
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER carts_set_updated_at
    BEFORE UPDATE ON carts
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TABLE product_variants (
                                  id BIGSERIAL PRIMARY KEY,
                                  product_id BIGINT NOT NULL,
                                  sku VARCHAR(255) UNIQUE,
                                  color VARCHAR(255),
                                  size VARCHAR(255),
                                  price NUMERIC(10,2),
                                  CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TRIGGER product_variants_set_updated_at
    BEFORE UPDATE ON product_variants
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT,
                        total_price NUMERIC(10,2),
                        currency VARCHAR(50),
                        status VARCHAR(50),
                        delivery_method VARCHAR(255),
                        delivery_address VARCHAR(255),
                        tracking_number VARCHAR(255),
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()

);

CREATE TRIGGER orders_set_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TABLE product_images (
                                id BIGSERIAL PRIMARY KEY,
                                product_id BIGINT,
                                url VARCHAR(255),
                                alt_text VARCHAR(255),
                                position INTEGER NOT NULL,
                                CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TRIGGER product_images_set_updated_at
    BEFORE UPDATE ON product_images
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TABLE cart_items (
                            id BIGSERIAL PRIMARY KEY,
                            cart_id BIGINT,
                            variant_id BIGINT,
                            quantity INTEGER NOT NULL,
                            price NUMERIC(10,2),
                            product_name VARCHAR(255),
                            sku VARCHAR(255),
                            color VARCHAR(255),
                            size VARCHAR(255),
                            CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id),
                            CONSTRAINT fk_cart_items_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id)
);

CREATE TRIGGER cart_items_set_updated_at
    BEFORE UPDATE ON cart_items
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT,
                             variant_id BIGINT,
                             quantity INTEGER NOT NULL,
                             price NUMERIC(10,2),
                             product_name VARCHAR(255),
                             sku VARCHAR(255),
                             color VARCHAR(255),
                             size VARCHAR(255),
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_order_items_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id)
);

CREATE TRIGGER order_items_set_updated_at
    BEFORE UPDATE ON order_items
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();