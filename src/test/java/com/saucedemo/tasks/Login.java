package com.saucedemo.tasks;

import com.saucedemo.userinterfaces.HeaderUI;
import com.saucedemo.userinterfaces.LoginUI;
import com.saucedemo.utils.TestConfig;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class Login implements Task {

    private final String username;
    private final String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Login asStandardUser() {
        return instrumented(Login.class, TestConfig.standardUsername(), TestConfig.standardPassword());
    }

    @Override
    @Step("{0} inicia sesión con el usuario #username")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(LoginUI.USERNAME, isVisible()),
                Enter.theValue(username).into(LoginUI.USERNAME),
                Enter.theValue(password).into(LoginUI.PASSWORD),
                Click.on(LoginUI.LOGIN_BUTTON),
                WaitUntil.the(HeaderUI.PAGE_TITLE, isVisible()));
    }
}
