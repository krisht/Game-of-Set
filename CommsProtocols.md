# Set Game Communication Protocol

| Function Name   | Parameters                 | Usage             | Direction |
|:---------------:|:--------------------------:|:-----------------:|:---------:|
| `loginUser`     | (String `login`, String `pass`) | Sign in User      | C --> S    |
| `loginResponse` | (int `uid`, int `returnValue`)|  Response to sign in | S --> C |
| `registerUser`  | (String `login`, String `pass`)| Register User     | C --> S    |
| `registerResponse` | (int `uid`, int `retrunValue`)| Response to register | S --> C
| `createGame`    | (int `uid`, String `gameName`) |  Makes game in DB  | C --> S    |
| `createGameResponse` | (int `gid`) | Response to game creation | S --> C  |
| `userSubmits`| (int `uid`, int `gid`, int `c1`, int `c2`, int `c3`) | Check set | C --> S|
| `userSubmitsResponse` | bool `setCorrect`, int [] `posReplaced`, int [] `board`, int `uid`, int `scorechange`, int [] `scoreboard_uids`, int [] `scoreboard_scores` | Response to checkset | S --> C |
| `joinGame`	  | (int `uid`, int `gid`)		   |  Puts user into game| C --> S |
| `joinGameResponse` | bool `added` |  Response to joining game | S --> C | 
| `loggingOut`	  | (int `uid`)				   | Disconnects user from server | C --> S |
| `loggingOutResponse` | bool `loggedout` | Response to logging out | S --> C |
| `sendPublicMessage` | (int `uid`, String `msg`) | Sends a new chat message | C --> S |
| `updatePublicChat`    | (String `username`, String `msg`) | Gets a new chat message | S --> C|
| `sendGameMessage` | (int `uid`, int `gid` String `msg`) | Sends a new chat message to game chat| C --> S |
| `updateLocalChat`    | (String `username`, String `msg`, int `gid`) | Gets game chat messages| S --> C|
| `updateGameboard`| (int `uid`, int `gid`)| updates leaderboard and Gameboard | C --> S | 
|`endGame`|(int `gid`) |Ends game| S --> C|
|`noMoreSets`|(int `uid`, int `gid`) |User think there is no more sets in the game|C --> S|
|`noMoreSetsRequest`|(int[] `uid`) |Happens after one user says there are no more sets, other members will be asked to vote on whether there are any more sets|S --> C|
| `getGameListing`  | (int `uid`)  | Returns array of GameListing Objects To update the server browser| C --> S |


| Error Values    | Meaning                          |
|:---------------:|:--------------------------------:|
|      `0`        | Function call was successful     |
|      `-1`       | Empty JSON received              |
|      `1`        | Corresponding function not found |
