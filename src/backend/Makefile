test: User.java
	javac DBComm.java User.java
	java -cp "../../lib/*:./" User DBComm 

client: ClientConn.java
	javac -classpath .:../../lib/* ClientConn.java
	java -cp "../../lib/*:./" ClientConn

server: ServerConn.java ServerThread.java
	javac -classpath .:../../lib/* ServerConn.java ServerThread.java
	java -cp "../../lib/*:./"  ServerConn
