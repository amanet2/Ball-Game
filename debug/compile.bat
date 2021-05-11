set PATH=%~dp0..\bin\jdk-14.0.1\bin\;%~dp0..\bin\javafx-sdk-11.02\bin\
javac -cp %~dp0..\src\ballgraphics --module-path %~dp0..\lib --add-modules=javafx.media -d %~dp0..\pkg\ballgame %~dp0..\src\ballgame\*.java
pause
