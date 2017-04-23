Backend

1. User already logged in has to be implemented
2. Create a leave game function that removes the user from a game
3. Create a client exit function that removes the user socket
4. Go over CommsProtocols.md again
5. Fix return values for all backend functions
        final int GAME_DOES_NOT_EXIST = 1;
        final int GAME_FULL = 2;
        final int GENERAL_ERROR = -1;
        final int SUCCESS = 3;
6. Set game size limit to 4
7. Fix returnValue for join game and create game
8. Check for duplicate gamenames


Frontend

1. Overiding finalize to inform server of users leaving games, both on exit and on close
2. GUI
3. Implemented remaining functions in comms protocols