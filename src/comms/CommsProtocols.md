# Set Game Communication Protocol

| Function Name   | Parameters                 | Returns     | Usage             | Direction |
|:---------------:|:--------------------------:|:----------: |:-----------------:|:---------:|
| `loginUser`     | (String login, String pass)| User Object | Sign in User      | C --> S    |
| `registerUser`  | (String login, String pass)| User Object | Register User     | C --> S    |
| `createGame`    | (int uid, String gameName) | Game Object | Makes game in DB  | C --> S    |
| `processSubmission`| (int c1, int c2, int c3) | Correctness | Check set | C --> S| 

| Error Values    | Meaning                          |
|:---------------:|:--------------------------------:|
|       0         | Function call was successful     |
|      -1         | Empty JSON received              |
|       1         | Corresponding function not found |