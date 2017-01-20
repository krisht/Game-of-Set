# SoftwareSetGame

# Goals

Login screen
-- sign up and sign in 

Scoring
-- click 3 cards
-- if no set, cards glow red
-- if right, get a point and cards move to palyer's deck
-- cards need to be delesected manually (even after having selected 3 without a set)

Vote for no sets possible
-- after first vote, 5-10 seconds to vote for no sets
-- show how many people have voted
-- unanimous
-- consider option to say no in vote, with penalty if the player is then unable to find a set

Chat room
-- global chatroom
-- game chatroom

Lobby
-- list of running games
-- global chat
-- option to create a new game
-- no minimum restriction on number of players

Game server rules:
-- player who creates game is the leader
-- leader has option to start a new game when another player joins
-- if leader chooses not to do so, current game continues and new player can spectate
-- ALTERNATELY
      - no leader and new players join immediately (though at a disadvantage of score)
      - rest functions as normal

public and private server
-- everything is public for now
-- in public server, everyone can join
-- in private server:
      - either leader (game creater) approves join requests
      - or new players need to be invitede to the game by current players
      - or need password to join the game

TIMELINE

5th February:


Frontend:

-- simple sign-in/sign-up page
-- field for username and password, toggle buttons for sign-in/sign-up
-- if toggled to sign-up, add field for confirm password
-- throw error if both passwords don't match
-- lead to "Lobby page" with username displayed and logout button, which returns to sign-in page.
-- provision for following responses from backend:
    User not exist (for sign-in)
    User already exist (for sign-up)
    PWD is incorrect
    Valid Login (lead to lobby)
    Valid Create -> Valid Login (load to lobby) 

Backend:

-- create a simple database with a list of usernames and corresponding passwords (plaintext for now)
-- create a list of active users
-- add provision for sign-in/sign-up requests from frontend with fitting response
