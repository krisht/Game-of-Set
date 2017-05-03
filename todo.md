Backend 

1. Add a timer to users in the socket. Whenever, User is accesssed or uid is accessed, update to current time in User object as last online.
  User cannot be inactive for more than x minutes (x=2?)

Frontend

1. Add sounds for new player, etc.
    * Sound for new chat message
    * Maybe special sound for different events
    * Sounds for: new player, player leaving, someone scored, you scored, incorrect set / not enough cards / too many cards 
2. If there is error connecting to server, display a connection error message and close the app
3. Remove ALL System.out/System.err outputs (shows up on commandline in Linux)

