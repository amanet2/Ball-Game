set out_dir=%~dp0..\pkg
%~dp0..\bin\jdk-20.0.1\bin\javac -d %out_dir% %~dp0..\src\*.java
cd %out_dir%
..\bin\jdk-20.0.1\bin\jar cmf ..\debug\MANIFEST.MF BALL_GAME.jar *.class
del *.class
cd %~dp0
set pkg_dir=%~dp0..\..\pkg_ballmaster
if exist %pkg_dir% del /f /s /q %pkg_dir%
if exist %pkg_dir% rmdir /s /q %pkg_dir%
mkdir %pkg_dir%
xcopy /y /e /i %~dp0..\bin %pkg_dir%\bin
xcopy /y /e /i %~dp0..\pkg %pkg_dir%\pkg
xcopy /y %~dp0..\Readme.txt %pkg_dir%
xcopy /y %~dp0..\ballmaster.exe %pkg_dir%
xcopy /y %~dp0..\ballmaster_editor.exe %pkg_dir%
if exist %pkg_dir%.zip del %pkg_dir%.zip
powershell.exe Compress-Archive -Path %pkg_dir%\* -DestinationPath %pkg_dir%.zip
