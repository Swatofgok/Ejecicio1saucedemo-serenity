package com.saucedemo.questions;

import com.saucedemo.models.Product;
import com.saucedemo.userinterfaces.CheckoutUI;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Importes del resumen de compra, redondeados a 2 decimales como los vería un usuario. */
public final class OrderSummary {

    private static final int MONEY_SCALE = 2;
    private static final String UNROUNDED_AMOUNT = ".*\\.\\d{3,}.*";

    private OrderSummary() {
    }

    public static Question<BigDecimal> itemTotal() {
        return amountOf(CheckoutUI.ITEM_TOTAL);
    }

    public static Question<BigDecimal> tax() {
        return amountOf(CheckoutUI.TAX);
    }

    public static Question<BigDecimal> total() {
        return amountOf(CheckoutUI.TOTAL);
    }

    public static Question<BigDecimal> itemTotalPlusTax() {
        return Question.about("item total + impuestos")
                .answeredBy(actor -> actor.asksFor(itemTotal()).add(actor.asksFor(tax())));
    }

    /** Texto crudo del item total, tal como lo muestra el sitio (puede venir sin redondear). */
    public static Question<String> rawItemTotal() {
        return Text.of(CheckoutUI.ITEM_TOTAL).describedAs("el texto del item total");
    }

    /** Importe del item total exactamente como aparece en pantalla, sin redondear (ej. "57.98"). */
    public static Question<String> itemTotalAsShown() {
        return Question.about("el item total tal como se muestra")
                .answeredBy(actor -> actor.asksFor(rawItemTotal()).replaceAll("[^0-9.]", ""));
    }

    public static boolean isUnrounded(String amountText) {
        return amountText.matches(UNROUNDED_AMOUNT);
    }

    private static Question<BigDecimal> amountOf(Target amount) {
        return Question.about(amount.getName()).answeredBy(actor ->
                Product.parsePrice(actor.asksFor(Text.of(amount)))
                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP));
    }
}
