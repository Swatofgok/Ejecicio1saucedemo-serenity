package com.saucedemo.models;

import java.util.List;
import java.util.Random;

/**
 * Datos del comprador para el formulario de checkout.
 * Se generan con el generador con semilla del escenario, para poder reproducirlos.
 */
public record BuyerData(String firstName, String lastName, String postalCode) {

    private static final List<String> FIRST_NAMES =
            List.of("Camila", "Andrés", "Valentina", "Santiago", "Laura", "Mateo", "Sofía", "Daniel");
    private static final List<String> LAST_NAMES =
            List.of("Moreno", "Gómez", "Ramírez", "Torres", "Castro", "Vargas", "Rojas", "Herrera");
    private static final int POSTAL_CODE_BOUND = 1_000_000;

    public static BuyerData random(Random random) {
        return new BuyerData(
                FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size())),
                LAST_NAMES.get(random.nextInt(LAST_NAMES.size())),
                String.format("%06d", random.nextInt(POSTAL_CODE_BOUND)));
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " - CP " + postalCode;
    }
}
