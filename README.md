# PrideMixinPatch

A small coremod for Minecraft 1.12.2 that registers Custom NPCs' mixins early enough to work next to HBM's Nuclear Tech, and only when Custom NPCs is installed.

## About

Custom NPCs patches `net.minecraft.entity.Entity` with a mixin. That mixin must be registered before the `Entity` class is loaded. Some coremods, such as HBM's Nuclear Tech Mod and its forks (NTM / NTM-Cursed), load `Entity` very early. On Cleanroom this means Custom NPCs' mixin config arrives too late, and Custom NPCs breaks.

PrideMixinPatch runs as a coremod with the lowest possible sorting index, so it loads before the others. If Custom NPCs is present, it registers Custom NPCs' mixin config (`customnpcs.mixins.json`) right away, starting Mixin first if needed. It also declares the same config through MixinBooter's early-mixin loader as a fallback.

If Custom NPCs is **not** installed, the mod does nothing. That is why the same jar can ship in both the Heavy and Lite editions of the Pride modpack (a 1.12.2 pack with about 750 mods), and why it is safe to leave in any pack.

## Features

- Registers `customnpcs.mixins.json` from `injectData`, at sorting index `Integer.MIN_VALUE` (before every other coremod).
- Calls `MixinBootstrap.init()` first if Mixin has not started yet.
- Fallback: implements MixinBooter's `IEarlyMixinLoader`, so the config is still picked up if the early registration fails.
- Detects Custom NPCs by checking whether `customnpcs.mixins.json` is on the classpath. Without it, no mixin config is registered and no error is thrown.
- No ASM transformers, no mod container, no access transformer. It contains a single class.

## Commands

None.

## Config

None.

## Requirements

- Minecraft 1.12.2
- Cleanroom, or Forge 14.23.5.2860 with Mixin available
- **MixinBooter** (provides `IEarlyMixinLoader`)
- Custom NPCs, if you want the mod to do anything. It is optional; without it the mod is inactive.

## Install

1. Download `PrideMixinPatch-1.12.2-<version>.jar`.
2. Put it in your `mods/` folder.
3. Start the game. It is a coremod (`FMLCorePlugin`), so it does not appear as a normal mod in the mod list.

## Building

```sh
./gradlew build
```

The jar is written to `build/libs/`. The build targets Java 8. `libs/mixinbooter-api.jar` is compile-only and is not bundled.

## License

MIT License. © 2026 crunkazcanbe.

## Credits

Made by crunkazcanbe, with Claude.


## Compile-only jars

The build compiles against these jars in `libs/` (other authors' mods / APIs). They are not included in this repo — get them from their official pages and drop them in `libs/` before building:

- `mixinbooter-api.jar`
