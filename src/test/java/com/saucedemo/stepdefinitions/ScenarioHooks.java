package com.saucedemo.stepdefinitions;

import com.saucedemo.utils.ActorMemory;
import com.saucedemo.utils.SeededRandom;
import io.cucumber.java.Before;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ScenarioHooks {

    static final String BUYER = "Comprador";

    private static final Logger LOG = LoggerFactory.getLogger(ScenarioHooks.class);

    @Before
    public void prepareStage() {
        OnStage.setTheStage(new OnlineCast());

        long seed = SeededRandom.seed();
        OnStage.theActorCalled(BUYER).remember(ActorMemory.RANDOM, new Random(seed));
        LOG.info("Semilla del escenario: {} (repetir con -Dseed={})", seed, seed);
        Serenity.recordReportData().withTitle("Semilla aleatoria")
                .andContents("Repetir esta corrida con: mvn clean verify -Dseed=" + seed);
    }
}
