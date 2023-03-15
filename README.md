Small fabric mod to drop player heads upon player death that's compatible with bedrock players joined through geyser!

Supported versions: 1.19.2-1.19.4 fabric

Why a mod instead of a datapack? Mods, unlike datapacks, allow for custom skin files on playerheads which are necessary in the approach here to get bedrock player skins.

Requires FabricAPI! Floodgate on the backend is optional, but necessary if you're using a prefix other than ".".
Because of https://bugs.mojang.com/browse/MC-174496, it is recommended to use a mod like https://modrinth.com/mod/headfix to keep Custom Head Names when breaking the head.

availble on Modrinth: https://modrinth.com/mod/geyserplayerheads

Current featureset:
- drops a playerhead upon player death, for java and bedrock players
- gets bedrock textures via the geyser global api
- optional floodgate hook to not rely on prefixes/"."
- names bedrock playerhead
- hooks in the web api once upon player join, async request to not freeze the game
- if killed by player: adds "killed by [player]" in the lore of the item!
- optional "/getskull" command - you can get heads for java players, and bedrock players. As long as they have a skin uploaded to the GeyserAPI (done automatically when they join any Geyser+floodgate server)
- option to disable prefixes showing in the head names


Left to be added:
Nothing furhter planned for now! I am open for suggestions though ^^

Huge thanks to Jens, the Fabric discord, and Elysian members for helping me test and develop this mod!

Note: This is NOT an official GeyserMC mod. It is made to work with Geyser, but it is not maintained or produced by GeyserMC. If you need support with this mod, please do not ask the Geyser developers - instead, please open an issue here
