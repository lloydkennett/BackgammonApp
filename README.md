# BackgammonApp
This is my final university project. It is a backgammon Android application with an AI opponent to play against. 

The app is on the [Google Play store](https://play.google.com/store/apps/details?id=com.kennett.l.expectigammon&hl=en_GB).

The application makes use of two activities, one for the [main menu](../master/app/src/main/java/com/kennett/l/backgammonapp/MainMenu.java) and the other for the [game screen](../master/app/src/main/java/com/kennett/l/backgammonapp/InGame.java).  
It also uses 3 AsyncTasks to help manage the pace and timings of the game:
- [AITask](../master/app/src/main/java/com/kennett/l/backgammonapp/AITask.java): Runs the AI.
- [StartMoveTask](../master/app/src/main/java/com/kennett/l/backgammonapp/StartMoveTask.java): Creates a list of all possible plays given a starting point and the dice roll.
- [MakeMoveTask](../master/app/src/main/java/com/kennett/l/backgammonapp/MakeMoveTask.java): Tells the in game activity to draw the desired play if it is within the list of possible plays created by  StartMoveTask.

To simulate the backgammon game, 4 classes are used.
- [Dice](../master/app/src/main/java/com/kennett/l/backgammonapp/Dice.java): Simulates the two die.
- [Board](../master/app/src/main/java/com/kennett/l/backgammonapp/Board.java): Contains checker locations and returns a variety information about the board state.
- [Move](../master/app/src/main/java/com/kennett/l/backgammonapp/Move.java): The movement of a checker that matches one of the die.
- [Play](../master/app/src/main/java/com/kennett/l/backgammonapp/Play.java): A players entire turn, consisting of multiple moves depending on the die rolls.

The main focus of my project was building the AI.

The AI uses an expectimax game tree that searches two turns ahead of the current turn.  
Min/max nodes are pruned using alpha-beta pruning and chance nodes are pruned using a *-minimax algorithm (developed by Bruce Ballard in 1983)  
These pruning techniques significantly speed up the search times leading to a more engaging experience for the user. Moves are ordered to improve the effectiveness of the pruning.  
The evaluation function is a weighted linear function, based on a few basic backgammon strategies.  

The AI was implemented using a single class [ExpectiMiniMax](../master/app/src/main/java/com/kennett/l/backgammonapp/ExpectiMiniMax.java) as well as a two comparator classes:
- [PlayMoveComparator](../master/app/src/main/java/com/kennett/l/backgammonapp/PlayMoveComparator.java): Remove different plays that consists of the same moves (plays can have the same moves but in different orders, this still lead to the same board state afterwards so they are essentially duplicate plays).
- [PlayScoreComparator](../master/app/src/main/java/com/kennett/l/backgammonapp/PlayScoreComparator.java): Used to order plays to improve pruning.

