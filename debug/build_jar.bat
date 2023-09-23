set out_dir=%~dp0..\pkg
set src_dir=%~dp0..\src\game
%~dp0..\bin\jdk-20.0.1\bin\javac -d %out_dir% %src_dir%\*.java
REM cd %out_dir%
%~dp0..\bin\jdk-20.0.1\bin\jar cmf %~dp0MANIFEST.MF %out_dir%\BALL_GAME.jar %out_dir%\*.class
del %out_dir%\*.class
REM cd %~dp0
