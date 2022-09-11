My attempt to make a mod, that would allow playerheads to be dropped upon player death.
Why a mod instead of a datapack? Mods, unlike datapacks, allow for custom skin files on playerheads which are necessary in the approach here to get bedrock player skins. Code is messy, since i have no clue what i'm doing!

ToDo:
- make WebRequests async + implement a timeout
- publish .jar

Current featureset:
- drops a playerhead upon player death, for java and bedrock players
- gets bedrock texture via the geyser global api
- optional floodgate hook to not rely on prefixes
- names bedrock playerhead


Note: This is NOT an official GeyserMC mod. It is made to work with Geyser, but it is not maintained or produced by GeyserMC. If you need support with this mod, please do not ask the Geyser developers - instead, please open an issue here
