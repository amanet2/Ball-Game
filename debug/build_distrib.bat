set pkg_dir=%~dp0..\..\pkg_Ball-Game
if exist %pkg_dir% del /f /s /q %pkg_dir%
if exist %pkg_dir% rmdir /s /q %pkg_dir%
mkdir %pkg_dir%
xcopy /y /e /i %~dp0..\bin %pkg_dir%\bin
xcopy /y /e /i %~dp0..\lib %pkg_dir%\lib
xcopy /y /e /i %~dp0..\pkg %pkg_dir%\pkg
xcopy /y %~dp0..\Readme.txt %pkg_dir%
xcopy /y %~dp0..\Ball-Game.exe %pkg_dir%
xcopy /y %~dp0..\Ball-Game-Mapmaker.exe %pkg_dir%
if exist %pkg_dir%.zip del %pkg_dir%.zip
powershell.exe Compress-Archive -Path %pkg_dir%\* -DestinationPath %pkg_dir%.zip
pause
