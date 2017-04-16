# Set Game Communication Protocol

| Function Name   | Parameters                 | Returns     | Usage             | Direction |
|:---------------:|:--------------------------:|:----------: |:-----------------:|:---------:|
| `loginUser`     | (String login, String pass)| User Object | Sign in User      | C --> S    |
| `registerUser`  | (String login, String pass)| User Object | Register User     | C --> S    |
| `createGame`    | (int `UID`, String `gameName`) | Game Object | Makes game in DB  | C --> S    |
| `processSubmission`| (int c1, int c2, int c3)| Correctness | Check set | C --> S|
| `joinGame`	  | (int `UID`, int `GID`)		   | Game Object | Puts user into game| C --> S |
| `loggingOut`	  | (int `UID`)				   | Affirmitive | Disconnects user from server | C --> S |
| `updateChat`    | (String chatUserName, String chatMessage) | null | Gets a new chat message | S --> C|
| `sendChatMessage` | (int `UID`, String chatMessage) | Affirmitive | Sends a new chat message | C --> S |
| `getGameListing`  | (int `UID`)               |Returns array of GameListing Objects| To update the server browser| C --> S |


| Error Values    | Meaning                          |
|:---------------:|:--------------------------------:|
|       0         | Function call was successful     |
|      -1         | Empty JSON received              |
|       1         | Corresponding function not found |
