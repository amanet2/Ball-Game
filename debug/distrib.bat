REM run build process to generate jar
set build_script=C:\Code\Ball-Game\debug\build.bat
call %build_script%

REM we have to set our variables here or they will be overwritten by previously called script
set home_dir=C:\Code\Ball-Game
set out_dir=C:\Code\pkg_ballmaster
set out_zip=%out_dir%.zip
set pkg_prefix=pkg
set pkg_dir=%home_dir%\%pkg_prefix%
set java_prefix=bin\jdk-20.0.1
set java_dir=%home_dir%\%java_prefix%

REM cleanup existing files
if exist %out_zip% del %out_zip%
if exist %out_dir% rmdir /s /q %out_dir%

REM copy files to output dir
mkdir %out_dir%
xcopy /y /e /i %java_dir% %out_dir%\%java_prefix%
xcopy /y /e /i %pkg_dir% %out_dir%\%pkg_prefix%
xcopy /y %home_dir%\Readme.txt %out_dir%
xcopy /y %home_dir%\ballmaster.exe %out_dir%
xcopy /y %home_dir%\ballmaster_editor.exe %out_dir%

REM zip up output
powershell.exe Compress-Archive -Path %out_dir%\* -DestinationPath %out_zip%

REM cleanup temp files
REM rmdir /s /q %out_dir%
