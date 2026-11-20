package com.saucedemo.tasks;

import com.saucedemo.models.BuyerData;
import com.saucedemo.userinterfaces.CartUI;
import com.saucedemo.userinterfaces.CheckoutUI;
import com.saucedemo.utils.ActorMemory;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Va del carrito al checkout y completa el formulario con datos de comprador generados con la semilla. */
public class FillCheckoutInformation implements Task {

    public static FillCheckoutInformation withRandomBuyerData() {
        return instrumented(FillCheckoutInformation.class);
    }

    @Override
    @Step("{0} completa el formulario de compra con datos generados")
    public <T extends Actor> void performAs(T actor) {
        BuyerData buyer = BuyerData.random(actor.recall(ActorMemory.RANDOM));
        actor.attemptsTo(
                Click.on(CartUI.CHECKOUT_BUTTON),
                WaitUntil.the(CheckoutUI.FIRST_NAME, isVisible()),
                // Task.where agrupa los tres campos para que el reporte muestre los datos usados
                Task.where("{0} escribe los datos del comprador: " + buyer,
                        Enter.theValue(buyer.firstName()).into(CheckoutUI.FIRST_NAME),
                        Enter.theValue(buyer.lastName()).into(CheckoutUI.LAST_NAME),
                        Enter.theValue(buyer.postalCode()).into(CheckoutUI.POSTAL_CODE)));
    }
}
