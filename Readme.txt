## Ball-Game
* System Requirements: Windows_x64
---
####KNOWN ISSUES
* _Riva Tuner Statistics Server_ (RTSS.exe) may cause crash on start
    * WORKAROUND: In RTSS set "Application detection level" to "None"
---
Some Useful Launch Arguments
* vidmode width,height,refresh_hz (force video mode)
* audioenabled 0 (no sound)
---
Starting a New Game
* From the home screen, select 'New Game'
* Select the map you want to play.  Other maps will load in on a rotation after each round
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
Mapmaker
* Start a New map by selecting File->New
* Save your map by going to File->Save As
* To join an editing session, go to Multiplayer->Join Game
* Edit an existing map by going to File->Open
---
Mapmaker - Creating/Editing Prefabs
* Start a New map by selecting File->New
* Use existing prefabs to create a new prefab
    -> keep in mind the origin when exporting is hardcoded to 0,0
    -> mouse info is displayed in the top left corner to help
* To export, open the console and enter command "exportasprefab"
    -> a dialog window will open
    -> open and close the console with the tilde/back-quote key (~/`)
* To open prefabs using a file browser dialog, open the console and enter "e_openprefab"
    -> NOTE: due to technical shortcomings, files must be in the 'prefabs/' folder to open