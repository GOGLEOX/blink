# Blink

Blink is a small Forge mod for 1.20.1 that lets a few basic survival systems keep moving while players sleep.

That is basically the whole mod.

## What it does right now

Current behavior is limited to a few things:

- furnaces can process part of a smelt during sleep
- smokers and blast furnaces can move forward too
- campfires keep cooking
- crops can get partial growth ticks
- animal breeding cooldowns can recover a little
- sleep acceleration values are configurable

That scope is narrow on purpose. The mod is probably better off staying a little stubborn about that.

## Installation

1. Install Forge for Minecraft 1.20.1.
2. Drop Blink into the `mods` folder.
3. Launch once so config files generate.
4. Adjust the sleep values if the defaults feel too loose for your world.

## Compatibility

Right now this is closer to "works best with vanilla-style logic" than "broadly tested."

There is also optional compatibility with Configured if you want to handle the config in-game instead of editing files by hand.

Known rough spots:

- some modded machines do not respond correctly
- multiplayer balance still needs work
- crop acceleration may feel too aggressive on some servers
- very large farms can progress faster than expected
- edge-case interactions probably still exist

That is alpha territory. Better to say it plainly.

## Known limitations

Blink does not try to process every possible block or world mechanic during sleep.

It only pushes a limited set of systems forward, and only partway. If something does not participate in the sleep pass, it may simply do nothing. If a server runs giant crop setups, those worlds probably need lower values than a smaller co-op world.

## Roadmap, probably

- better multiplayer balancing
- optional dimension restrictions
- more configurable utility categories
- better compatibility handling for modded processing blocks
- maybe NeoForge later

Nothing much bigger than that is planned from the notes.

## License

MIT License

## Links

- GitHub: https://github.com/GOGLEOX/blink
- Website: https://gogleox.github.io/gxfoundry
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/blink
- Modrinth: [NOT UPLOADED YET]

---

GOGLEOX Foundry - Forged in fire, crafted for utility.
