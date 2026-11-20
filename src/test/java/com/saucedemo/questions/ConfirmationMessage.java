package com.saucedemo.questions;

import com.saucedemo.userinterfaces.CheckoutUI;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public final class ConfirmationMessage {

    private ConfirmationMessage() {
    }

    public static Question<String> displayed() {
        return Text.of(CheckoutUI.CONFIRMATION_HEADER).describedAs("el mensaje de confirmación");
    }
}
