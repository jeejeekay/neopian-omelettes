package net.igobyjake.neopianomelettes.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

// Fabric runs every @GameTest method listed through the "fabric-gametest" entrypoint
// (src/gametest/resources/fabric.mod.json). The checks themselves are in OmeletteChecks.
public class OmeletteGameTests {
    @GameTest
    public void eating(GameTestHelper helper) { OmeletteChecks.eating(helper); }

    @GameTest
    public void recipes(GameTestHelper helper) { OmeletteChecks.recipes(helper); }

    @GameTest
    public void meatTag(GameTestHelper helper) { OmeletteChecks.meatTag(helper); }

    @GameTest
    public void showcaseLoads(GameTestHelper helper) { OmeletteChecks.showcaseLoads(helper); }
}
