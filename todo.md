Backend

1. Send a `nomoresetsResponse` as defined in the commsprotocols.md file.

Frontend

1. Add sounds for new player, etc.
    * Sound for new chat message
    * Maybe special sound for different events
    * Sounds for: new player, player leaving, someone scored, you scored, incorrect set / not enough cards / too many cards 
2. Add word system to all messages in chat
3. Put "no more sets" clicks into chat
4. Remove ALL System.out/System.err outputs (shows up on commandline in Linux)
5. Remove all popups and move them to chat
6. Add chat line for no-more-sets
7. On leave game, close the gameboard and put the leaving score in global chat
8. On game over, close the game and put the final scores in global chat