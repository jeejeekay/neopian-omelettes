package net.igobyjake.neopianomelettes.gametest;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

// Registers each check as a test function. The tests themselves (which function, how long
// it may take, which structure) are the JSON files in src/gametest/resources/data/neopianomelettes/test_instance/.
// Run them with ./gradlew runGameTestServer. The checks are in OmeletteChecks.
@EventBusSubscriber(modid = "neopianomelettes")
public class OmeletteGameTests {
    @SubscribeEvent
    public static void registerTests(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, helper -> {
            helper.register(id("eating"), OmeletteChecks::eating);
            helper.register(id("recipes"), OmeletteChecks::recipes);
            helper.register(id("meat_tag"), OmeletteChecks::meatTag);
            helper.register(id("showcase_loads"), OmeletteChecks::showcaseLoads);
        });
    }

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath("neopianomelettes", name);
    }
}
