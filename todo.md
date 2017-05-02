Backend 

1. Send a `nomoresetsResponse` as defined in the commsprotocols.md file.
2. Add a timer to users in the socket. Whenever, User is accesssed or uid is accessed, update to current time in User object as last online.
  User cannot be inactive for more than x minutes (x=2?)

Frontend

1. Add sounds for new player, etc.
    * Sound for new chat message
    * Maybe special sound for different events
    * Sounds for: new player, player leaving, someone scored, you scored, incorrect set / not enough cards / too many cards 
2. Add word system to all messages in chat
3. Change error message if no game is selected during join game
3. Put "no more sets" clicks into chat
4. Remove ALL System.out/System.err outputs (shows up on commandline in Linux)
5. Remove all popups and move them to chat
6. Add chat line for no-more-sets
7. On leave game, close the gameboard and put the leaving score in global chat
8. On game over, close the game and put the final scores in global chat
9. If there is error connecting to server, display a connection error message and close the app
