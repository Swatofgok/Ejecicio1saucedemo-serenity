package com.saucedemo.questions;

import com.saucedemo.models.Product;
import com.saucedemo.utils.ActorMemory;
import net.serenitybdd.screenplay.Question;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** Productos que el actor eligió al azar en este escenario (guardados en su memoria). */
public final class SelectedProducts {

    private SelectedProducts() {
    }

    public static Question<List<Product>> list() {
        return Question.about("los productos seleccionados")
                .answeredBy(actor -> actor.recall(ActorMemory.SELECTED_PRODUCTS));
    }

    public static Question<BigDecimal> totalPrice() {
        return Question.about("la suma de precios de los productos seleccionados")
                .answeredBy(actor -> actor.asksFor(list()).stream()
                        .map(Product::price)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /** Suma esperada con exactamente 2 decimales, como debería mostrarla el sitio (ej. "57.98"). */
    public static Question<String> totalPriceWithTwoDecimals() {
        return Question.about("la suma esperada con dos decimales")
                .answeredBy(actor -> actor.asksFor(totalPrice()).setScale(2, RoundingMode.UNNECESSARY).toPlainString());
    }
}
