package com.saucedemo.questions;

import com.saucedemo.userinterfaces.HeaderUI;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Question;

import java.util.List;

public final class CartBadgeCount {

    private CartBadgeCount() {
    }

    /** Número del ícono del carrito; 0 si el sitio no muestra el contador (carrito vacío). */
    public static Question<Integer> value() {
        return Question.about("el contador del carrito").answeredBy(actor -> {
            List<WebElementFacade> badge = HeaderUI.CART_BADGE.resolveAllFor(actor);
            return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
        });
    }
}
