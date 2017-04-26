Backend

1. Create a leave game function that removes the user from a game
2. Create a client exit function that removes the user socket
3. Go over CommsProtocols.md again
4. Fix return values for all backend functions
        final int GAME_DOES_NOT_EXIST = 1;
        final int GAME_FULL = 2;
        final int GENERAL_ERROR = -1;
        final int SUCCESS = 3;
        final int GAME_NAME_ALREADY_EXISTS = 4;
5. Set game size limit to 4
6. Check for duplicate gamenames


Frontend

1. Overiding finalize to inform server of users leaving games, both on exit and on close
2. GUI
3. Implemented remaining functions in comms protocols
4. Add sounds for new player, etc.
5. Remove ALL System.out/System.err outputs (shows up on commandline in Linux)