package com.saucedemo.tasks;

import com.saucedemo.models.Product;
import com.saucedemo.questions.ProductsListed;
import com.saucedemo.userinterfaces.InventoryUI;
import com.saucedemo.utils.ActorMemory;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Lee el inventario en vivo, elige productos distintos al azar (con la semilla del escenario) y los agrega. */
public class AddRandomProductsToCart implements Task {

    private final int quantity;

    public AddRandomProductsToCart(int quantity) {
        this.quantity = quantity;
    }

    public static AddRandomProductsToCart ofQuantity(int quantity) {
        return instrumented(AddRandomProductsToCart.class, quantity);
    }

    @Override
    @Step("{0} agrega #quantity productos aleatorios al carrito")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(WaitUntil.the(InventoryUI.PRODUCTS, isVisible()));
        List<String> picked = pickFrom(actor.asksFor(ProductsListed.inTheInventory()), actor.recall(ActorMemory.RANDOM));
        actor.attemptsTo(AddProductsToCart.named(picked));
    }

    private List<String> pickFrom(List<Product> catalog, Random random) {
        if (catalog.size() < quantity) {
            throw new IllegalStateException("El inventario tiene " + catalog.size()
                    + " productos y se pidieron " + quantity);
        }
        List<Product> shuffled = new ArrayList<>(catalog);
        Collections.shuffle(shuffled, random);
        return shuffled.subList(0, quantity).stream().map(Product::name).toList();
    }
}
