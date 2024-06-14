![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_64h.png)
<img src="https://i.imgur.com/iaETp3c.png" alt="" width="200" >
![fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_64h.png)

## CustomPlayerHeads
Small Fabric mod to drop player heads upon player death - or get them via a command - that's compatible with Bedrock players joined through Geyser, or other mods changing the skin while in-game; e.g. FabricTailor.

## Supported versions: 1.20.6, 1.21 Fabric
Availble on Modrinth: 
#### https://modrinth.com/mod/customplayerheads

Why a mod instead of a datapack? Mods, unlike datapacks, allow me to access the skin data in the GameProfile.

Requires FabricAPI! Floodgate is optional for Bedrock support, but necessary if you're using a floodgate prefix other than ".".
Because of https://bugs.mojang.com/browse/MC-174496, it is recommended to use a mod like https://modrinth.com/mod/headfix to keep Custom Head Names when breaking the head.

### Current featureset:
- drops a playerhead upon player death
- will display (and keep displaying) the skin used at the time of death!
- if killed by player: adds "killed by [player]" in the lore of the item - or anything else; fully customizable!
- optional "/getskull" command - you can get heads for Java and Bedrock players. For the latter, it will work as long as they have a skin uploaded to the GeyserAPI (done automatically when they join any Geyser+floodgate server)
- option to disable floodgate prefixes showing in the head names

### Left to be added:
Nothing further planned for now! I am open for suggestions though ^^
