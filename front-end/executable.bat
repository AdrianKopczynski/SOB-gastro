cd ..
cd .\back-end\target\
start cmd.exe /k java -jar .\SOZ-Gastro-0.0.1-SNAPSHOT.jar
cd ..
cd ..
cd .\front-end\
timeout 6
py .\windowManager.py