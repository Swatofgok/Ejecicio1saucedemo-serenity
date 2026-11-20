package com.saucedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Elementos de la cabecera, comunes a todas las páginas después del login. */
public final class HeaderUI {

    public static final Target PAGE_TITLE = Target.the("título de la página").locatedBy(".title");
    public static final Target CART_BADGE = Target.the("contador del carrito").locatedBy(".shopping_cart_badge");
    public static final Target CART_LINK = Target.the("ícono del carrito").locatedBy(".shopping_cart_link");

    private HeaderUI() {
    }
}
