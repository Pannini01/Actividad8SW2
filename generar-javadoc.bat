@echo off
call mvn javadoc:javadoc
if errorlevel 1 exit /b %errorlevel%
echo Javadoc generado en target\site\apidocs\index.html
