# Nightshift

Nightshift is a small Forge mod for 1.20.1 that lets a few basic survival systems keep moving while players sleep.

## What it does right now

- furnaces can process part of a smelt during sleep
- smokers and blast furnaces can move forward too
- campfires keep cooking
- crops can get partial growth ticks
- animal breeding cooldowns can recover a little
- sleep acceleration values are configurable

That scope is narrow on purpose.

## Why it exists

Sleep mods usually drift one of two ways.

Vanilla leaves the world feeling oddly frozen.

The other direction is fake ticking, automation creep, machine boosting, weird sleep gadgets, or trying to simulate the whole skipped night. Nightshift is for the middle bit. Mostly survival servers. Mostly ordinary utility blocks. Mostly just making morning feel like at least a little time passed.

## Installation

1. Install Forge for Minecraft 1.20.1.
2. Drop Nightshift into the `mods` folder.
3. Launch once so config files generate.
4. Adjust the sleep values if the defaults feel too loose for your world.

Optional compatibility with Configured is included if you want in-game config access.

## Compatibility

Right now this is closer to "works best with vanilla-style logic" than "broadly tested."

Known rough spots:

- some modded machines do not respond correctly
- multiplayer balance still needs work
- crop acceleration may feel too aggressive on some servers
- very large farms can progress faster than expected
- edge-case interactions probably still exist

## Known limitations

Nightshift does not try to process every possible block or world mechanic during sleep.

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

- GitHub: https://github.com/GOGLEOX/nightshift
- Website: https://gogleox.github.io/gxfoundry
- CurseForge: [NOT UPLOADED YET]
- Modrinth: [NOT UPLOADED YET]

---

GOGLEOX Foundry - Forged in fire, Crafted for utility.
