package com.saucedemo.questions;

import com.saucedemo.userinterfaces.HeaderUI;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public final class PageTitle {

    private PageTitle() {
    }

    public static Question<String> displayed() {
        return Text.of(HeaderUI.PAGE_TITLE).describedAs("el título de la página");
    }
}
