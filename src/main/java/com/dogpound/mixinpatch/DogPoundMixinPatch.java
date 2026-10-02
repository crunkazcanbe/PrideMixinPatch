package com.dogpound.mixinpatch;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.IEarlyMixinLoader;

/**
 * DogPound Mixin Patch — makes Custom NPCs work on Cleanroom alongside HBM/NTM-Cursed.
 *
 * CNPCs' EntityMixin must patch net.minecraft.entity.Entity BEFORE that class is
 * classloaded, but HBM/NTM coremods load Entity very early. This coremod runs at the
 * absolute-earliest sort index and registers CNPCs' mixin config right there (bootstrapping
 * Mixin itself if needed), so the config is live before HBM/NTM drag Entity in. Also
 * declares it via MixinBooter's IEarlyMixinLoader as a fallback.
 */
@IFMLLoadingPlugin.Name("DogPound Mixin Patch")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class DogPoundMixinPatch implements IFMLLoadingPlugin, IEarlyMixinLoader {

    private static final String CONFIG = "customnpcs.mixins.json";

    /** Pride Lite has no Custom NPCs: only register its mixins when its config file is actually on the classpath */
    private static boolean present() {
        ClassLoader cl = DogPoundMixinPatch.class.getClassLoader();
        return cl != null && cl.getResource(CONFIG) != null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        // register CNPCs' config at the earliest possible moment, before HBM/NTM load Entity
        if (!present()) return;
        try {
            MixinBootstrap.init();
            Mixins.addConfiguration(CONFIG);
        } catch (Throwable t) {
            // if Mixin isn't ready yet, IEarlyMixinLoader below still picks it up
        }
    }

    @Override
    public List<String> getMixinConfigs() {
        return present() ? Arrays.asList(CONFIG) : java.util.Collections.<String>emptyList();
    }

    @Override public String[] getASMTransformerClass() { return new String[0]; }
    @Override public String getModContainerClass() { return null; }
    @Override public String getSetupClass() { return null; }
    @Override public String getAccessTransformerClass() { return null; }
}
