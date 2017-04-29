Backend

1. Create a client exit function that removes the user socket (done)
2. Go over CommsProtocols.md again
3. Fix return values for all backend functions 
        final int GAME_DOES_NOT_EXIST = 1;
        final int GAME_FULL = 2;
        final int GENERAL_ERROR = -1;
        final int SUCCESS = 3;
        final int GAME_NAME_ALREADY_EXISTS = 4; (done)
4. Set game size limit to 4
5. Check for duplicate gamenames


Frontend

1. Add sounds for new player, etc.
2. Remove ALL System.out/System.err outputs (shows up on commandline in Linux)