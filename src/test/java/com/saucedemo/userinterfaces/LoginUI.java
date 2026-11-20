package com.saucedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

public final class LoginUI {

    public static final Target USERNAME = Target.the("campo usuario").locatedBy("#user-name");
    public static final Target PASSWORD = Target.the("campo contraseña").locatedBy("#password");
    public static final Target LOGIN_BUTTON = Target.the("botón Login").locatedBy("#login-button");

    private LoginUI() {
    }
}
