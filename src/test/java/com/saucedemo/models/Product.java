package com.saucedemo.models;

import java.math.BigDecimal;

/**
 * Producto del catálogo de SauceDemo (nombre y precio).
 */
public record Product(String name, BigDecimal price) {

    /** Convierte un texto como "$29.99" o "Item total: $29.99" en BigDecimal. */
    public static BigDecimal parsePrice(String text) {
        return new BigDecimal(text.replaceAll("[^0-9.]", ""));
    }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}
