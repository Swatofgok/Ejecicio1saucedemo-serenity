package com.saucedemo.tasks;

import com.saucedemo.questions.OrderSummary;
import com.saucedemo.userinterfaces.CheckoutUI;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Pasa al resumen de compra. Si el sitio muestra el item total sin redondear (defecto conocido de
 * SauceDemo con ciertas combinaciones de productos), lo deja como evidencia en el reporte.
 */
public class ContinueToOrderSummary implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(ContinueToOrderSummary.class);
    private static final String DEFECT_TITLE = "Defecto del sitio: item total sin redondear";

    public static ContinueToOrderSummary page() {
        return instrumented(ContinueToOrderSummary.class);
    }

    @Override
    @Step("{0} revisa el resumen de la compra")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(CheckoutUI.CONTINUE_BUTTON),
                WaitUntil.the(CheckoutUI.ITEM_TOTAL, isVisible()));

        String rawItemTotal = actor.asksFor(OrderSummary.rawItemTotal());
        if (OrderSummary.isUnrounded(rawItemTotal)) {
            LOG.warn("{}: '{}'", DEFECT_TITLE, rawItemTotal);
            Serenity.recordReportData().withTitle(DEFECT_TITLE).andContents(rawItemTotal);
        }
    }
}
