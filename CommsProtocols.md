# Set Game Communication Protocol

| BE Status | FE Status | Function Name   | Parameters                 | Usage             | Direction |
|:---------:|:---------:|:---------------:|:--------------------------:|:-----------------:|:---------:|
| Done | Done | `loginUser`     | (String `login`, String `pass`) | Sign in User      | C --> S    |
| Done | Done | `loginResponse` | (int `uid`, int `returnValue`)|  Response to sign in | S --> C |
| Done | Done | `registerUser`  | (String `login`, String `pass`)| Register User     | C --> S    |
| Done | Done | `registerResponse` | (int `uid`, int `retrunValue`)| Response to register | S --> C
| Done | Done (-gui)| `joinGame`    | (int `uid`, int `gid`)    | Request to join given game    | C --> S|
| Done | Done |`joinGameResponse`| (gameboard, leaderboard, int `returnValue`)| Reponse to join given game| S-->C|
| Done | Done | `createGame`    | (int `uid`, String `gameName`) |  Makes game in DB  | C --> S    |
| Done | Done (-gui) | `createGameResponse` | (int `gid`, String `gameName`, gameboard, leaderboard, int `returnValue`) | Response to game creation | S --> C  |
| | | `userSubmits`| (int `uid`, int `gid`, int `c1`, int `c2`, int `c3`) | Check set | C --> S|
| | | `userSubmitsResponse` | bool `setCorrect`, int [] `posReplaced`, int [] `board`, int `uid`, int `scorechange`, int [] `scoreboard_uids`, int [] `scoreboard_scores` | Response to checkset | S --> C |
| | | `loggingOut`	  | (int `uid`)	| Disconnects user from server | C --> S |
| | | `loggingOutResponse` | bool `loggedout` | Response to logging out | S --> C |
| Done | Done | `playerScore` | (int `uid`)   | requests total score of player    | C --> S|
| Done | Done | `playerScoreResponse` | (int `score`) | returns the users total score | S --> C |
| | | `sendPublicMessage` | (int `uid`, String `msg`) | Sends a new chat message | C --> S |
| | | `updatePublicChat`    | (String `username`, String `msg`) | Gets a new chat message | S --> C|
| | | `sendGameMessage` | (int `uid`, int `gid` String `msg`) | Sends a new chat message to game chat| C --> S |
| | | `updateGameChat`    | (String `username`, String `msg`, int `gid`) | Gets game chat messages| S --> C|
| | |`leaveGame`|(int `uid`, int `gid`) |For user to leave game| C --> S|
| | |`noMoreSets`|(int `uid`, int `gid`) |User think there is no more sets in the game|C --> S|
| | |`noMoreSetsRequest`|(int[] `uid`) |Happens after one user says there are no more sets, other members will be asked to vote on whether there are any more sets|S --> C|
| Done | Done | `getGameListing`  | (int `uid`)  | Returns array of GameListing Objects To update the server browser| C --> S |
|  Done | Done (-gui) |`getGameListingResponse` | (JSONArray `gamesList`(int `gid`, String `gameName`, String `username1`, String `username2`, String `username3`, String `username4`)) | Returns games and players in games| S--> C|


| Error Values    | Meaning                          |
|:---------------:|:--------------------------------:|
|      `0`        | Function call was successful     |
|      `-1`       | Empty JSON received              |
|      `1`        | Corresponding function not found |
