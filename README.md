# Set Game #

## Team Members ##
- Krishna Thiyagarajan
- Abhinav Jain
- Brenda So
- Ross Kaplan

## Goals ##

##### Login screen #####
- Sign up and sign in 

##### Scoring #####
- Click 3 cards
- If no set, cards glow red (maybe play sound indicating no set?)
- If right, get a point and cards move to players's deck (play another sound indicating set?)
- Cards need to be delesected manually (even after having selected 3 without a set)
- Vote for no sets possible
    - After first vote, 5-10 seconds to vote for no sets
    - Show how many people have voted
    - Unanimous
    - Consider option to say no in vote, with penalty if the player is then unable to find a set

##### Chat room #####
- Global chatroom
- Game chatroom

##### Lobby #####
- List of running games
- Global chat
- Option to create a new game
- No minimum restriction on number of players

##### Game server rules: #####
- Player who creates game is the leader
- Leader has option to start a new game when another player joins
- If leader chooses not to do so, current game continues and new player can spectate
- **ALTERNATELY**
    - No leader and new players join immediately (though at a disadvantage of score)
    - Rest functions as normal

##### Public and Private server #####
- Everything is public for now
- In public server, everyone can join
- In private server:
    - Either leader (game creater) approves join requests
    - Or new players need to be invitede to the game by current players
    - Or need password to join the game

## Timeline ##
##### Goals by February 5, 2017 #####
- *Frontend*
    - Simple sign-in/sign-up page
    - Field for username and password, toggle buttons for sign-in/sign-up
    - If toggled to sign-up, add field for confirm password
    - Throw error if both passwords don't match
    - Lead to "Lobby page" with username displayed and logout button, which returns to sign-in page.
    - Provision for following responses from backend:
        - User not exist (for sign-in)
        - User already exist (for sign-up)
        - Pass is incorrect
        - Valid Login (lead to lobby)
        - Valid Create => Valid Login (load to lobby) 

- *Backend*
    - Create a simple database with a list of usernames and corresponding passwords (plaintext for now)
    - Create a list of active users
    - Add provision for sign-in/sign-up requests from frontend with fitting response
    
##### Goals By February 20, 2017 #####
- *Frontend*
    - Move all window calls into the main client 
    - Consider main window with login and signup buttons, and then separate windows for the two.
    - Layout for board, layout for button, layout for chat, layout for game controls (e.g. no more cards)
    - Put name as a field in java

- *Backend*
    - Generate 12 initial cards
    - Add 3 cards when requested and track location
    - Check whether set is correctly identified
    - Sockets connection for backend and front end
    
##### Goals for February 27, 2017 #####
- *Frontend*
    - Modify addCard function to make card object
    - Make button "No more sets" workable
    - Cards should be clicable
    - Ability to check whether backend function to check whether they're the same
    - Fix login and sign up
    - Figure out server browser
    - Add chat to landing page
    - Find out how to use JSON to communicate with backend

- *Backend*
    - Add location to card object to the JSON passed to the frontend
    - Add scores to players scores if they correctly select cards
    - Set up true backend running on server where code sits
    
##### Goals for March 5, 2017 #####
- *Frontend*
    - Make a class tos end request to backend with ability communicate with other FE classes and unique Gameboard ID
    - Fix appearance of the game board
    - Make space for communication with backend
    
- *Backend*
    - Set up backend server on certain port for FE to talk on
        - Different port for every different game instance
        - Same port for all people in a particular game
    - Set up communication layer for socket comms between FE and BE
    - Set up communication protocol for functions to communicate properly

##### Goals for March 16, 2017 #####
- *Frontend*
    - Work on making frontend UI/UX user friendly
    - Work on chat
    - Merge game server browser
    
- *Backend*
    - Finish server-side JSON parsing
    - Implement switch statement for function calls with appropriate params
    - Update/create HashMap for gid --> uid, uid --> Socket
    - Get output of functions nad pass back as JSON to frontend
    - Provide example function calls
