package com.saucedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

public final class CartUI {

    public static final String TITLE = "Your Cart";

    public static final Target ITEMS = Target.the("productos del carrito").locatedBy(".cart_item");
    public static final Target CHECKOUT_BUTTON = Target.the("botón Checkout").locatedBy("#checkout");

    private CartUI() {
    }
}
