set out_dir=%~dp0..\pkg
%~dp0..\bin\jdk-20.0.1\bin\javac -d %out_dir% %~dp0..\src\*.java
cd %out_dir%
..\bin\jdk-20.0.1\bin\jar cmf ..\debug\MANIFEST.MF BALL_GAME.jar *.class
del *.class
cd %~dp0
