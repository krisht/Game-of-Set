#test: User.java
#	javac DBComm.java User.java
#	java -cp "../lib/*:./" User DBComm 

client: frontend/ClientConn.java
	javac -classpath .:../lib/* frontend/ClientConn.java
	java -cp "../lib/*:./" frontend.ClientConn

server: backend/ServerConn.java backend/ServerThread.java
	javac -classpath .:../lib/* backend/ServerConn.java backend/ServerThread.java
	java -cp "../lib/*:./" backend.ServerConn 2>&1 | tee logs/logfile.txt; echo "-- file contents --" && cat logs/logfile.txt

clean: 
	rm -f backend/*.class frontend/*.class
