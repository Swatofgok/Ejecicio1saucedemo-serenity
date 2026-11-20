package com.saucedemo.tasks;

import com.saucedemo.utils.TestConfig;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class OpenTheStore implements Task {

    public static OpenTheStore sauceDemo() {
        return instrumented(OpenTheStore.class);
    }

    @Override
    @Step("{0} abre la tienda SauceDemo")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(TestConfig.baseUrl()));
    }
}
