# BackgammonApp
This is my final university project. It is a backgammon Android application with an AI opponent to play against. 

The app is on the Google Play store: https://play.google.com/store/apps/details?id=com.xanq.l.backgammonapp

The application makes use of two activities, one for the [main menu](../master/app/src/main/java/com/xanq/l/backgammonapp/MainMenu.java) and the other for the [game screen](../master/app/src/main/java/com/xanq/l/backgammonapp/InGame.java). It also uses a number of AsyncTasks to help manage the pace of the game (AI thinking process as well as just making the general application feel smoother)

To simulate the backgammon game, 4 classes are used.
- [Dice](../master/app/src/main/java/com/xanq/l/backgammonapp/Dice.java): simulates the two die
- [Board](../master/app/src/main/java/com/xanq/l/backgammonapp/Board.java): contains checker locations and returns different information about the board state.
- [Move](../master/app/src/main/java/com/xanq/l/backgammonapp/Move.java): the movement of a checker that matches one of the die.
- [Play](../master/app/src/main/java/com/xanq/l/backgammonapp/Play.java): a players entire turn, consisting of multiple moves depending on the die rolls.

The main focus of my project was building the AI.

The AI uses an expectimax game tree that searches two turns ahead of the current turn.  
Min/max nodes are pruned using alpha-beta pruning and chance nodes are pruned using a *-minimax algorithm (developed by Bruce Ballard in 1983)  
These pruning techniques significantly speed up the search times leading to a more engaging experience for the user. Moves are ordered to improve the effectiveness of the pruning.  
The evaluation function is a weighted linear function, based on a few basic backgammon strategies.  

The AI was implemented using a single class [ExpectiMiniMax](../master/app/src/main/java/com/xanq/l/backgammonapp/ExpectiMiniMax.java) as well as a two comparator classes that remove different plays that consists of the same moves (in different orders but still lead to the same board state afterwards) and order plays to improve pruning.


