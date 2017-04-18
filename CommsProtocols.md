# Set Game Communication Protocol

| Function Name   | Parameters                 | Return Keys     | Usage             | Direction |
|:---------------:|:--------------------------:|:----------: |:-----------------:|:---------:|
| `loginUser`     | (String `login`, String `pass`)| int `uid`, int `returnValue`, String `fCall` | Sign in User      | C --> S    |
| `loginResponse` | (int `uid`, int `loginResp`)|             | Response to sign in | S --> C |
| `registerUser`  | (String `login`, String `pass`, String `name`)| int `uid`, int `returnValue`, String `fCall`| Register User     | C --> S    |
| `registerResponse` | (int `registerResp`)   |                | Response to register | S --> C
| `createGame`    | (int `uid`, String `gameName`(optional)) | int `gid`, String `gamename`, String `fCall` | Makes game in DB  | C --> S    |
| `userSubmits`| (int uid, int gid, int c1, int c2, int c3)| bool `setCorrect`, int [] `posReplaced`, int [] `board`, int `uid`, int `scorechange`, int [] `scoreboard_uids`, int [] `scoreboard_scores`, String `fCall`| Check set | C --> S|
| `joinGame`	  | (int `uid`, int `gid`)		   | bool `added`, String `fCall` | Puts user into game| C --> S |
| `loggingOut`	  | (int `uid`)				   | Affirmitive | Disconnects user from server | C --> S |
| `updateChat`    | (String chatUserName, String chatMessage) | null | Gets a new chat message | S --> C|
| `sendPublicMessage` | (int `uid`, String `msg`) | int `uid`, String `msg`, String `fCall` | Sends a new chat message | C --> S |
| `updateLocalChat`    | (String chatUserName, String chatMessage, int `gid`) | null | Gets a new chat message for local chatbox during a game| S --> C|
| `sendGameMessage` | (int `uid`, int `gid` String `msg`) | int `gid`, int `uid`, String `msg`, String `fCall`| Sends a new chat message to local chatbox during a game| C --> S |
| `updateGameboard`| (int `uid`, int `gid`)| Gameboard and Leaderboard | updates leaderboard and Gameboard | C --> S | 
|`endGame`|(int `gid`)|ends the game|null| Notifies users when there is either no more sets or no more cards|S --> C|
|`noMoreSets`|(int `uid`, int `gid`)|null|User think there is no more sets in the game|C --> S|
|`noMoreSetsRequest`|(int[] `uid`)|null|Happens after one user says there are no more sets, other members will be asked to vote on whether there are any more sets|S --> C|
| `getGameListing`  | (int `uid`) |Returns array of GameListing Objects| To update the server browser| C --> S |


| Error Values    | Meaning                          |
|:---------------:|:--------------------------------:|
|       0         | Function call was successful     |
|      -1         | Empty JSON received              |
|       1         | Corresponding function not found |
