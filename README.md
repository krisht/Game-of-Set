# Set Game

## Team Members
- Krishna Thiyagarajan
- Abhinav Jain
- Brenda So
- Ross Kaplan

## Goals

##### Login screen
- Sign up and sign in 

##### Scoring
- Click 3 cards
- If no set, cards glow red (maybe play sound indicating no set?)
- If right, get a point and cards move to palyer's deck
- Cards need to be delesected manually (even after having selected 3 without a set)
- Vote for no sets possible
    - After first vote, 5-10 seconds to vote for no sets
    - Show how many people have voted
    - Unanimous
    - Consider option to say no in vote, with penalty if the player is then unable to find a set

##### Chat room
- Global chatroom
- Game chatroom

##### Lobby
- List of running games
- Global chat
- Option to create a new game
- No minimum restriction on number of players

##### Game server rules:
- Player who creates game is the leader
- Leader has option to start a new game when another player joins
- If leader chooses not to do so, current game continues and new player can spectate
- **ALTERNATELY**
    - No leader and new players join immediately (though at a disadvantage of score)
    - Rest functions as normal

##### Public and Private server
- Everything is public for now
- In public server, everyone can join
- In private server:
    - Either leader (game creater) approves join requests
    - Or new players need to be invitede to the game by current players
    - Or need password to join the game

## Timeline
##### February 5, 2017
- Frontend
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

- Backend
    - Create a simple database with a list of usernames and corresponding passwords (plaintext for now)
    - Create a list of active users
    - Add provision for sign-in/sign-up requests from frontend with fitting response
