## Ball-Game
* Computer game for Windows_x64
* Create or join online servers
* Build maps with the map editor
---
####KNOWN ISSUES
* _Riva Tuner Statistics Server_ (RTSS.exe) may cause crash on start
    * WORKAROUND: In RTSS set "Application detection level" to "None"
---
Gameplay
* Temporarily boost your speed by pressing [Shift] or Right Mouse Button
* Run over powerup icons (?) to get a bonus item... or a penalty
---
Initial Setup (Windows)
* Install game by double-clicking install.exe
---
Some Useful Launch Arguments
* vidmode width,height,refresh_hz (force video mode)
* audioenabled 0 (no sound)
---
Starting a New Game
* From the home screen, select 'New Game'
* Select the map you want to play.  Other maps will load in on a rotation after each round
* Set the Time Limit, Score Limit, and Mode of gameplay for each round
* Select "- Start -" to start a server and begin accepting incoming connections
---
Joining a Game
* From the home screen, select 'Join Game'
* Set the address and port of the server you want to connect to
* Select "- Start -" to join the server
---
Customization
* Edit files (at your own risk) in pkg/game/config to customize game experience
** e.g. edit bindings or add custom resolution(s)
* Open the console to interact with the engine directly
** enter `commandlist` to view commands
---
Creating a new Map (Ball-Game-Mapmaker.exe)
* Editor loads an empty map on start, place tiles, props, and flares in this space
* Save your map by going to File->Save
* Distribute your custom map to players and add the map to your map playlist (settings.cfg) or load it from the console
---
Editing a Map (Ball-Game-Mapmaker.exe)
* Open an existing map by going to File->Open
* Make changes to the map
* Save changes in File->Save or exit without saving
* Distribute your custom map to players
* Save map to pkg\game\ballgame if you want it selectable in game
---
Map Design Thoughts
* Design maps so players do not usually get killed by shooters off screen
    -> it messes up the groove for everyone to have to switch mental gears and memorize "special spots" in a map
