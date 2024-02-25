# Gemstone Powerups
-------------------

Gemstone Powerups is a PaperMC plugin for giving players potion effects
when in proximity to specific blocks. This is a custom plugin is made for 
a specific gemstone-themed Minecraft server. Players are able to choose a
Gemstone class and will receive potion effects when in proximity to blocks
relating to that Gemstone. The potion effects that correspond to each Gemstone
class can be customized in the config.
  
For example, when a player chooses the "Diamond" Gemstone class, they will
receive Resistance I when in proximity of diamond blocks or diamond ore.

## User Commands
----------------
- ``/gemstones choose <Gemstone>``
  - Selects a Gemstone class for the player
- ``/gemstones toggle <on/off>``
  - Toggles potion effects on or off for the player
- ``/gemstones show``
  - Displays the player's current selected Gemstone class and the toggle
    status of the potion effects

## Admin Commands
-----------------
- ``/gemstones enable``
  - Enable all potion effects globally
- ``/gemstones disable``
  - Disable all potion effects globally

## Admin Guide
--------------

### Permission Nodes
--------------------
- ``gemstones.user``: General user permission. If a player has this, then they
  can receive potion effects, choose their Gemstone class, and toggle potion
  effects on and off
  - ``/gemstones choose <Gemstone>``
  - ``/gemstones toggle <on/off>``
  - ``/gemstones show``
- ``gemstones.admin``: Admin permission. Players with this permission will be
  able to enable/disable all potion effects globally.
  - ``/gemstones enable``
  - ``/gemstones disable``

### Config
----------
Here is an excerpt of the default config:
```
gemstones:
  prismarine:
    potion_effects:
      WATER_BREATHING:
        - radius: 30
          level: 0
          blocks:
            - PRISMARINE
            - PRISMARINE_BRICKS
            - DARK_PRISMARINE
            - PRISMARINE_STAIRS
            - PRISMARINE_BRICK_STAIRS
            - DARK_PRISMARINE_STAIRS
  diamond:
    potion_effects:
      RESISTANCE:
        - radius: 30
          level: 0
          blocks:
            - DIAMOND_BLOCK
        - radius: 5
          level: 0
          blocks:
            - DIAMOND_ORE
            - DEEPSLATE_DIAMOND_ORE
```
The potion effects of each Gemstone class can be specified under 
``potion_effects``. Multiple potion effects can be listed. Furthermore,
the config provides the option to specify the radius of effect and potion
level. Each radius/potion level has a list of blocks. The blocks in the list
will apply the potion effect at their corresponding level and within their
corresponding radii.

This allows the administrator to have a more granulated control over how far
each type of block affects the player. For the Diamond example, a player will
only need to be within 30 blocks of a Diamond Block to receive Resistance I.
However, they would need to be within 5 blocks of Diamond Ore to receive the
same effect.
