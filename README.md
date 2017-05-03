# A Game of Set #

## Download Game Here ##


<div style="text-align:center">
<a href="https://github.com/krisht/SoftwareSetGame/raw/master/GameOfSet.jar">
  <img src="https://raw.githubusercontent.com/krisht/GameOfSet/master/src/frontend/images/SET.png" alt="Download me!" width="100">
</a>
</div>


## Usage ##

### Linux ###
- ``` java -jar /path/to/folder/GameOfSet.jar ```

### Windows ###
- Double click and run using JVM


## Landing Page Help

### Quick Start ###

Either create a game by clicking the **CREATE GAME** button or join a game by selecting a game and clicking the **JOIN GAME** button!

### FAQ ###
1. **How do I create a game?**
* Just click the **CREATE GAME** button! Don't forget to make a name for the game!

2. **How do I join a game?**
* Click on a game you want to join, and click the **JOIN GAME** button!

3. **My friend created a game and I can't see the game! What should I do?**
* Your board is probably not refreshed! Click **REFRESH** button to refresh the game!

4. **Why can't I join a game?**
* There are multiple reasons as to why you can't join a game, including:
  * There are already 4 people in a game! Each game only takes a maximum of four people. Either choose a game with less than 4 people or create a new game!
  * The game you're trying to join no longer exists. Refresh the game listing by clicking the blue **REFRESH** button!

## Gameboard Help

### What is a SET?

A set consists of three cards satisfying all of these conditions:
* They all have the same number or have three different numbers.
* They all have the same symbol or have three different symbols.
* They all have the same shading or have three different shadings.
* They all have the same color or have three different colors.

The rules of Set are summarized by: 
> If you can sort a group of three cards into "two of \_\_\_\_ and one of \_\_\_\_", then it is not a set.

For example, these three cards form a set:

![alt text](https://raw.githubusercontent.com/krisht/GameOfSet/master/src/bin/red_striped_three_curvy.bmp)
![alt text](https://raw.githubusercontent.com/krisht/GameOfSet/master/src/bin/red_striped_two_rect.bmp)
![alt text](https://raw.githubusercontent.com/krisht/GameOfSet/master/src/bin/red_striped_one_diam.bmp)

And so do these:

![alt text](https://raw.githubusercontent.com/krisht/GameOfSet/master/src/bin/red_striped_one_rect.bmp)
![alt text](https://raw.githubusercontent.com/krisht/GameOfSet/master/src/bin/green_solid_two_diam.bmp)
![alt text](https://raw.githubusercontent.com/krisht/GameOfSet/master/src/bin/purple_hollow_three_curvy.bmp)

Given any two cards from the deck, there is one and only one other card that forms a set with them.

A player is given a point if their submitted set is correct and don't get a point if the submitted set is incorrect.


## Game Screen Shots ##

<div style="text-align:center">

<img src="https://raw.githubusercontent.com/krisht/GameOfSet/master/imgs/login.png" width="400">
<img src="https://raw.githubusercontent.com/krisht/GameOfSet/master/imgs/registration.png" width="400">
<img src="https://raw.githubusercontent.com/krisht/GameOfSet/master/imgs/gameplay.gif" width="800">

</div>




## Team Members ##
- Backend:
  - Krishna Thiyagarajan
  - Ross Kaplan
- Frontend: 
  - Abhinav Jain
  - Brenda So
