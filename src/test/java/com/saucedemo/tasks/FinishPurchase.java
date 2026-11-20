package com.saucedemo.tasks;

import com.saucedemo.userinterfaces.CheckoutUI;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class FinishPurchase implements Task {

    public static FinishPurchase now() {
        return instrumented(FinishPurchase.class);
    }

    @Override
    @Step("{0} finaliza la compra")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(CheckoutUI.FINISH_BUTTON),
                WaitUntil.the(CheckoutUI.CONFIRMATION_HEADER, isVisible()));
    }
}
