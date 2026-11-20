package com.saucedemo.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

public final class CheckoutUI {

    // Paso 1: información del comprador
    public static final Target FIRST_NAME = Target.the("campo nombre").locatedBy("#first-name");
    public static final Target LAST_NAME = Target.the("campo apellido").locatedBy("#last-name");
    public static final Target POSTAL_CODE = Target.the("campo código postal").locatedBy("#postal-code");
    public static final Target CONTINUE_BUTTON = Target.the("botón Continue").locatedBy("#continue");

    // Paso 2: resumen
    public static final Target ITEM_TOTAL = Target.the("item total").locatedBy(".summary_subtotal_label");
    public static final Target TAX = Target.the("impuestos").locatedBy(".summary_tax_label");
    public static final Target TOTAL = Target.the("total").locatedBy(".summary_total_label");
    public static final Target FINISH_BUTTON = Target.the("botón Finish").locatedBy("#finish");

    // Paso 3: confirmación
    public static final Target CONFIRMATION_HEADER = Target.the("mensaje de confirmación").locatedBy(".complete-header");

    private CheckoutUI() {
    }
}
