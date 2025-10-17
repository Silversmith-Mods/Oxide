package com.ordana.oxide.reg;

import com.google.common.base.Stopwatch;
import com.ordana.oxide.Oxide;
import com.ordana.oxide.entities.SprayParticleEntity;

import java.util.ArrayList;
import java.util.List;

public class ModSetup {

    private static int setupStage = 0;

    private static final List<Runnable> MOD_SETUP_WORK = List.of(
            ModCreativeTab::setup
    );

    public static void setup() {
        var list = new ArrayList<Long>();
        try {
            Stopwatch watch = Stopwatch.createStarted();

            SprayParticleEntity.DATA_FLUID.get();

            for (int i = 0; i < MOD_SETUP_WORK.size(); i++) {
                setupStage = i;
                MOD_SETUP_WORK.get(i).run();
                list.add(watch.elapsed().toMillis());
                watch.reset();
                watch.start();
            }

            Oxide.LOGGER.info("Finished mod setup in: {} ms", list);

        } catch (Exception e) {
            terminateWhenSetupFails(e);
        }
    }

    private static void terminateWhenSetupFails(Exception e) {
        throw new IllegalStateException("Mod setup has failed to complete (" + setupStage + ").\n" +
                " This might be due to some mod incompatibility or outdated dependencies (check if everything is up to date).\n" +
                " Refusing to continue loading with a broken modstate. Next step: crashing this game, no survivors", e);
    }
}