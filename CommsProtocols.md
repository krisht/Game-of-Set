# Set Game Communication Protocol

| BE Status | FE Status | Function Name           | Parameters                 | Usage             | Direction |
|:---------:|:---------:|:-----------------------:|:--------------------------:|:-----------------:|:---------:|
| Done      | Done      | `loginUser`             | (String `login`, String `pass`) | Sign in User      | C --> S    |
| Done      | Done      | `loginResponse`         | (int `uid`, int `returnValue`)|  Response to sign in | S --> C |
| Done      | Done      | `registerUser`          | (String `login`, String `pass`)| Register User     | C --> S    |
| Done      | Done      | `registerResponse`      | (int `uid`, int `retrunValue`)| Response to register | S --> C
| Done      | Done      | `joinGame`              | (int `uid`, int `gid`)    | Request to join given game    | C --> S|
| Done      | Done      |`joinGameResponse`       | (int `uid`, int`gid`, `returnValue`)| Reponse to join given game| S-->C|
| Done      | Done      | `createGame`            | (int `uid`, String `gameName`) |  Makes game in DB  | C --> S    |
| Done      | Done      | `createGameResponse`    | (int `uid` int `gid`, int `returnValue`) | Response to game creation | S --> C  |
| Done      | Done      | `updateGameResponse`    | (int `gid`, gameboard, String `gameName`, leaderboard), JSONArray `nomoresets` (array of 1s and 0s of whether corresponding player has clicked nomoresets) | Updates the gameboard with every change | S --> C |
| Done      | Done      | `userSubmits`           | (int `uid`, int `gid`, int `c1`, int `c2`, int `c3`) | Check set | C --> S|
| Done      | Done      | `userSubmitsResponse`   | int `uid`, int `returnValue` | Response to checkset | S --> C |
| Done      | Done      | `loggingOut`	          | (int `uid`)	| Disconnects user from server | C --> S |
| Done      |           | `loggingOutResponse`    | int `uid`, int `returnValue` | Response to logging out | S --> C |
| Done      | Done      | `playerScore`           | (int `uid`)   | requests total score of player    | C --> S|
| Done      | Done      | `playerScoreResponse`   | (int `score`) | returns the users total score | S --> C |
| Done      |           | `sendPublicMessage`     | (int `uid`, String `msg`) | Sends a new chat message | C --> S |
| Done      |           | `updatePublicChat`      | (String `username`, String `msg`) | Gets a new chat message | S --> C|
| Done      |           | `sendGameMessage`       | (int `uid`, int `gid` String `msg`) | Sends a new chat message to game chat| C --> S |
| Done      |           | `updateGameChat`        | (String `username`, String `msg`, int `gid`) | Gets game chat messages| S --> C|
| Done      |           | `leaveGame`             | (int `uid`, int `gid`) |For user to leave game| C --> S|
| Done      | Done      | `leaveGameResponse`     | (int `uid`, int `gid`, int `returnValue` |Response to user leaving game| C --> S|
| Done      |           | `noMoreSets`            | (int `uid`, int `gid`) |User think there is no more sets in the game|C --> S|
| Done      | Done      | `getGameListing`        | (int `uid`)  | Returns array of GameListing Objects To update the server browser| C --> S |
| Done      | Done      | `getGameListingResponse`| (JSONArray `gamesList`(int `gid`, String `gameName`, String `username1`, String `username2`, String `username3`, String `username4`)) | Returns games and players in games| S--> C|
| Done      |           | `gameOverResponse`      | (int `gid`) | Right after sending updateboard after nomoresets or usersubmits | S --> C |


| Error Values    | Meaning                          |
|:---------------:|:--------------------------------:|
|      `0`        | Function call was successful     |
|      `-1`       | Empty JSON received              |
|      `1`        | Corresponding function not found |
