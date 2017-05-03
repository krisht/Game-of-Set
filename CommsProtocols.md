# Set Game Communication Protocol

| Function Name           | Parameters                 | Usage             | Direction |
|:-----------------------:|:--------------------------:|:-----------------:|:---------:|
| `loginUser`             | (String `login`, String `pass`) | Sign in User      | C --> S    |
| `loginResponse`         | (int `uid`, int `returnValue`)|  Response to sign in | S --> C |
| `registerUser`          | (String `login`, String `pass`)| Register User     | C --> S    |
| `registerResponse`      | (int `uid`, int `retrunValue`)| Response to register | S --> C
| `joinGame`              | (int `uid`, int `gid`)    | Request to join given game    | C --> S|
|`joinGameResponse`       | (int `uid`, int`gid`, `returnValue`)| Reponse to join given game| S-->C|
| `createGame`            | (int `uid`, String `gameName`) |  Makes game in DB  | C --> S    |
| `createGameResponse`    | (int `uid` int `gid`, int `returnValue`) | Response to game creation | S --> C  |
| `updateGameResponse`    | (int `gid`, gameboard, String `gameName`, leaderboard), JSONArray `nomoresets` (array of 1s and 0s of whether corresponding player has clicked nomoresets) | Updates the gameboard with every change | S --> C |
| `userSubmits`           | (int `uid`, int `gid`, int `c1`, int `c2`, int `c3`) | Check set | C --> S|
| `userSubmitsResponse`   | int `uid`, int `returnValue`, String `username`) | Response to checkset | S --> C |
| `loggingOut`	          | (int `uid`)	| Disconnects user from server | C --> S |
| `loggingOutResponse`    | int `uid`, int `returnValue` | Response to logging out | S --> C |
| `playerScore`           | (int `uid`)   | requests total score of player    | C --> S|
| `playerScoreResponse`   | (int `score`) | returns the users total score | S --> C |
| `sendPublicMessage`     | (int `uid`, String `msg`) | Sends a new chat message | C --> S |
| `updatePublicChat`      | (String `username`, String `msg`) | Gets a new chat message | S --> C|
| `sendGameMessage`       | (int `uid`, int `gid` String `msg`) | Sends a new chat message to game chat| C --> S |
| `updateGameChat`        | (String `username`, String `msg`, int `gid`) | Gets game chat messages| S --> C|
| `leaveGame`             | (int `uid`, int `gid`) |For user to leave game| C --> S|
| `leaveGameResponse`     | (int `uid`, int `gid`, int `returnValue` |Response to user leaving game| C --> S|
| `noMoreSets`            | (int `uid`, int `gid`) |User think there is no more sets in the game|C --> S|
| `getGameListing`        | (int `uid`)  | Returns array of GameListing Objects To update the server browser| C --> S |
| `getGameListingResponse`| (JSONArray `gamesList`(int `gid`, String `gameName`, String `username1`, String `username2`, String `username3`, String `username4`)) | Returns games and players in games| S--> C|
| `gameOverResponse`      | (int `gid`) | Right after sending updateboard after nomoresets or usersubmits | S --> C |
| `nomoresetsResponse`    | (int `gid`, String `username`) | Right after someone in the game sends a nomoresets request; sent to all users in the game | S --> C |

