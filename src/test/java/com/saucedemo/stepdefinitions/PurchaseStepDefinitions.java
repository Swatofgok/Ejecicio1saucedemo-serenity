package com.saucedemo.stepdefinitions;

import com.saucedemo.models.Product;
import com.saucedemo.questions.CartBadgeCount;
import com.saucedemo.questions.ConfirmationMessage;
import com.saucedemo.questions.OrderSummary;
import com.saucedemo.questions.PageTitle;
import com.saucedemo.questions.ProductsListed;
import com.saucedemo.questions.SelectedProducts;
import com.saucedemo.tasks.AddProductsToCart;
import com.saucedemo.tasks.AddRandomProductsToCart;
import com.saucedemo.tasks.ContinueToOrderSummary;
import com.saucedemo.tasks.FillCheckoutInformation;
import com.saucedemo.tasks.FinishPurchase;
import com.saucedemo.tasks.Login;
import com.saucedemo.tasks.OpenTheStore;
import com.saucedemo.tasks.ViewCart;
import com.saucedemo.userinterfaces.CartUI;
import com.saucedemo.userinterfaces.InventoryUI;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class PurchaseStepDefinitions {

    @Dado("que el usuario abre la tienda SauceDemo")
    public void openStore() {
        theActorCalled(ScenarioHooks.BUYER).attemptsTo(OpenTheStore.sauceDemo());
    }

    @Cuando("inicia sesión como usuario estándar")
    public void login() {
        buyer().attemptsTo(Login.asStandardUser());
    }

    @Entonces("visualiza la página de productos")
    public void inventoryIsDisplayed() {
        buyer().should(seeThat("Título del inventario", PageTitle.displayed(), equalToIgnoringCase(InventoryUI.TITLE)));
    }

    @Cuando("agrega {int} productos aleatorios al carrito")
    public void addRandomProducts(int quantity) {
        buyer().attemptsTo(AddRandomProductsToCart.ofQuantity(quantity));
    }

    @Cuando("agrega al carrito los productos:")
    public void addProducts(List<String> names) {
        buyer().attemptsTo(AddProductsToCart.named(names));
    }

    @Entonces("el contador del carrito indica {int}")
    public void cartBadge(int expected) {
        buyer().should(seeThat("Contador del carrito", CartBadgeCount.value(), equalTo(expected)));
    }

    @Cuando("visualiza el carrito")
    public void viewCart() {
        buyer().attemptsTo(ViewCart.contents());
        buyer().should(seeThat("Título del carrito", PageTitle.displayed(), equalToIgnoringCase(CartUI.TITLE)));
    }

    @Entonces("el carrito contiene exactamente los productos seleccionados")
    public void cartMatchesSelection() {
        Product[] selected = buyer().asksFor(SelectedProducts.list()).toArray(Product[]::new);
        buyer().should(seeThat("Productos del carrito", ProductsListed.inTheCart(), containsInAnyOrder(selected)));
    }

    @Cuando("completa el formulario de compra con datos generados dinámicamente")
    public void fillCheckoutForm() {
        buyer().attemptsTo(FillCheckoutInformation.withRandomBuyerData());
    }

    @Cuando("revisa el resumen de la compra")
    public void reviewOrderSummary() {
        buyer().attemptsTo(ContinueToOrderSummary.page());
    }

    @Entonces("el total del resumen coincide con la suma de los productos más los impuestos")
    public void totalsMatch() {
        buyer().should(
                seeThat("Item total = suma de precios de los productos elegidos",
                        OrderSummary.itemTotal(), comparesEqualTo(buyer().asksFor(SelectedProducts.totalPrice()))),
                seeThat("Total = item total + impuestos",
                        OrderSummary.total(), comparesEqualTo(buyer().asksFor(OrderSummary.itemTotalPlusTax()))));
    }

    @Entonces("el item total se muestra exactamente con dos decimales")
    public void itemTotalIsShownExactly() {
        buyer().should(seeThat("Item total mostrado en pantalla",
                OrderSummary.itemTotalAsShown(), equalTo(buyer().asksFor(SelectedProducts.totalPriceWithTwoDecimals()))));
    }

    @Cuando("finaliza la compra")
    public void finishPurchase() {
        buyer().attemptsTo(FinishPurchase.now());
    }

    @Entonces("visualiza el mensaje de confirmación {string}")
    public void confirmationMessage(String message) {
        // El sitio muestra "Thank you for your order!": se valida que contenga el texto pedido, sin distinguir mayúsculas
        buyer().should(seeThat("Mensaje de confirmación", ConfirmationMessage.displayed(), containsStringIgnoringCase(message)));
    }

    private Actor buyer() {
        return theActorInTheSpotlight();
    }
}
