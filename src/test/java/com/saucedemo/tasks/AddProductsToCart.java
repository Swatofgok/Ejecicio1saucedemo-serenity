package com.saucedemo.tasks;

import com.saucedemo.models.Product;
import com.saucedemo.questions.ProductsListed;
import com.saucedemo.userinterfaces.InventoryUI;
import com.saucedemo.utils.ActorMemory;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Agrega al carrito los productos indicados por nombre y recuerda sus precios leídos del inventario. */
public class AddProductsToCart implements Task {

    private final List<String> names;

    public AddProductsToCart(List<String> names) {
        this.names = List.copyOf(names);
    }

    public static AddProductsToCart named(List<String> names) {
        return instrumented(AddProductsToCart.class, names);
    }

    @Override
    @Step("{0} agrega al carrito: #names")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(WaitUntil.the(InventoryUI.PRODUCTS, isVisible()));
        List<Product> inventory = actor.asksFor(ProductsListed.inTheInventory());
        List<Product> selection = names.stream().map(name -> findIn(inventory, name)).toList();
        actor.remember(ActorMemory.SELECTED_PRODUCTS, selection);
        selection.forEach(product ->
                actor.attemptsTo(Click.on(InventoryUI.ADD_TO_CART_BUTTON.of(product.name()))));
    }

    private static Product findIn(List<Product> inventory, String name) {
        return inventory.stream()
                .filter(product -> product.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró el producto en el inventario: " + name));
    }
}
