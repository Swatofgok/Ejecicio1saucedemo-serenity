package com.saucedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

public final class InventoryUI {

    public static final String TITLE = "Products";

    public static final Target PRODUCTS = Target.the("productos del inventario").locatedBy(".inventory_item");

    /** Botón "Add to cart" de la tarjeta cuyo nombre visible es {0}. */
    public static final Target ADD_TO_CART_BUTTON = Target.the("botón agregar de '{0}'").locatedBy(
            "//div[contains(concat(' ', normalize-space(@class), ' '), ' inventory_item ')]"
                    + "[.//div[contains(@class, 'inventory_item_name') and normalize-space()='{0}']]"
                    + "//button[starts-with(@id, 'add-to-cart')]");

    private InventoryUI() {
    }
}
