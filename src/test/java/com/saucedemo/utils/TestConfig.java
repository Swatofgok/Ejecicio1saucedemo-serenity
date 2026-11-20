package com.saucedemo.utils;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;

/**
 * Lee URL y credenciales del entorno activo en serenity.conf (-Denvironment=...).
 * Las credenciales se pueden sobreescribir con variables de entorno.
 */
public final class TestConfig {

    private TestConfig() {
    }

    public static String baseUrl() {
        return property("webdriver.base.url");
    }

    public static String standardUsername() {
        return overridable("SAUCEDEMO_USERNAME", "users.standard.username");
    }

    public static String standardPassword() {
        return overridable("SAUCEDEMO_PASSWORD", "users.standard.password");
    }

    private static String overridable(String environmentVariable, String key) {
        String value = System.getenv(environmentVariable);
        return value == null || value.isBlank() ? property(key) : value;
    }

    private static String property(String key) {
        return EnvironmentSpecificConfiguration
                .from(SystemEnvironmentVariables.currentEnvironmentVariables())
                .getProperty(key);
    }
}
