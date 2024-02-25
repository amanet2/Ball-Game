REM run build process to generate jar
set build_script=C:\Code\Ball-Game\debug\build.bat
call %build_script%

REM we have to set our variables here or they will be overwritten by previously called script
set home_dir=C:\Code\Ball-Game
set out_dir=C:\Code\pkg_ballmaster
set out_zip=C:\Code\pkg_ballmaster.zip
set pkg_prefix=pkg
set pkg_dir=%home_dir%\%pkg_prefix%
set java_prefix=bin\jdk-20.0.1
set java_dir=%home_dir%\%java_prefix%
set gcc_home=C:\mingw64\bin\gcc.exe
set game_c=%home_dir%\ballmaster.c
set game_editor_c=%home_dir%\ballmaster_editor.c
set game_exe=%home_dir%\ballmaster.exe
set game_editor_exe=%home_dir%\ballmaster_editor.exe
set readme_path=%home_dir%\Readme.txt

REM cleanup existing files
if exist %out_zip% del %out_zip%
if exist %out_dir% rmdir /s /q %out_dir%
if exist %game_exe% del %game_exe%
if exist %game_editor_exe% del %game_editor_exe%

REM create executables
%gcc_home% -o %game_exe% %game_c%
%gcc_home% -o %game_editor_exe% %game_editor_c%

REM copy files to output dir
mkdir %out_dir%
xcopy /y /e /i %java_dir% %out_dir%\%java_prefix%
xcopy /y /e /i %pkg_dir% %out_dir%\%pkg_prefix%
xcopy /y %readme_path% %out_dir%
xcopy /y %game_exe% %out_dir%
xcopy /y %game_editor_exe% %out_dir%

REM zip up output
powershell.exe Compress-Archive -Path %out_dir%\* -DestinationPath %out_zip%

REM cleanup temp files
del %game_exe%
del %game_editor_exe%
rmdir /s /q %out_dir%
