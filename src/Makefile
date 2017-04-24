#test: User.java
#	javac DBComm.java User.java
#	java -cp "../lib/*:./" User DBComm 

client: frontend/ClientConn.java
	javac -classpath .:../lib/* frontend/ClientConn.java
	java -cp "../lib/*:./" frontend.ClientConn

server: backend/*.java
	git pull origin master
	javac -classpath .:../lib/* backend/ServerConn.java backend/ServerThread.java
	java -cp "../lib/*:./" backend.ServerConn 2>&1 | tee logs/logfile.txt; echo "-- file contents --" && cat logs/logfile.txt

clean: 
	rm -f backend/*.class frontend/*.class backend/.*.swp frontend/.*.swp

frontend: frontend/Login.java
	javac -classpath .:../lib/* frontend/ClientConnThreaded.java  frontend/Friends.java  frontend/GameBoard_Front.java  frontend/GameListing.java  frontend/LandingPage.java  frontend/LandingPage_New.java  frontend/Login.java  frontend/LoginPage.java  frontend/Main.java  frontend/test.java
	java -cp "../lib/*:./" frontend.Login
