set PATH=%~dp0..\bin\jdk-14.0.1\bin\;%~dp0..\bin\javafx-sdk-11.0.2\bin\
javac --module-path %~dp0..\lib --add-modules=javafx.media -d %~dp0..\pkg %~dp0..\src\*.java
cd %~dp0..\pkg
jar cmf MANIFEST.MF BALL_GAME.jar *.class ..\src\*.java ballgame
del *.class
cd %~dp0
pause
