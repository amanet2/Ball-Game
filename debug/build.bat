%~dp0..\bin\jdk-18.0.2.1\bin\javac --module-path %~dp0..\bin\javafx-sdk-11.0.2\lib --add-modules=javafx.media -d %~dp0..\pkg %~dp0..\src\*.java
cd %~dp0..\pkg
%~dp0..\bin\jdk-18.0.2.1\bin\jar cmf %~dp0..\debug\MANIFEST.MF %~dp0..\pkg\BALL_GAME.jar *.class
del %~dp0..\pkg\*.class
cd %~dp0
