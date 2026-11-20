package com.saucedemo.questions;

import com.saucedemo.models.Product;
import com.saucedemo.userinterfaces.CartUI;
import com.saucedemo.userinterfaces.InventoryUI;
import com.saucedemo.userinterfaces.ProductCardUI;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.targets.Target;

import java.util.List;

/** Productos (nombre y precio) que se ven en una lista de tarjetas: inventario o carrito. */
public final class ProductsListed {

    private ProductsListed() {
    }

    public static Question<List<Product>> inTheInventory() {
        return in(InventoryUI.PRODUCTS);
    }

    public static Question<List<Product>> inTheCart() {
        return in(CartUI.ITEMS);
    }

    private static Question<List<Product>> in(Target cards) {
        return Question.about(cards.getName()).answeredBy(actor ->
                cards.resolveAllFor(actor).stream().map(ProductsListed::toProduct).toList());
    }

    private static Product toProduct(WebElementFacade card) {
        return new Product(
                card.findBy(ProductCardUI.NAME).getText(),
                Product.parsePrice(card.findBy(ProductCardUI.PRICE).getText()));
    }
}
