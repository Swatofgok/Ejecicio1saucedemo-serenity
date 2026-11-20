package com.saucedemo.tasks;

import com.saucedemo.userinterfaces.HeaderUI;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class ViewCart implements Task {

    public static ViewCart contents() {
        return instrumented(ViewCart.class);
    }

    @Override
    @Step("{0} abre el carrito")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(HeaderUI.CART_LINK),
                WaitUntil.the(HeaderUI.PAGE_TITLE, isVisible()));
    }
}
