package com.saucedemo.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generador aleatorio reproducible: usa -Dseed=<n> si se indica, o una semilla nueva por escenario.
 * La semilla se publica en el reporte para poder repetir exactamente una corrida que falló.
 */
public final class SeededRandom {

    private static final String SEED_PROPERTY = "seed";

    private SeededRandom() {
    }

    public static long seed() {
        String configured = System.getProperty(SEED_PROPERTY);
        return configured == null || configured.isBlank()
                ? ThreadLocalRandom.current().nextLong()
                : Long.parseLong(configured.trim());
    }
}
