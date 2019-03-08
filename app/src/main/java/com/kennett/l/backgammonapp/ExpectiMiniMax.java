package com.kennett.l.backgammonapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class ExpectiMiniMax {

    private int[][] diceRolls;

    public ExpectiMiniMax(){
        diceRolls = new int[][]{{1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {3, 4}, {3, 5}, {3, 6}, {4, 5}, {4, 6}, {5, 6}, {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}};
    }

    /*Backgammon move generator based on the algorithm described by Hans J. Berliner, 1977.
    * http://www.bkgm.com/articles/Berliner/BKG-AProgramThatPlaysBackgammon/index.html
    * Works by recursively building ai moves based on 3 different game states and adds the move to
    * a list once generated. Any duplicate ai moves are then removed from the list*/
    public ArrayList<Play> findPlays(Board board, int player, int die1, int die2){
        ArrayList<Play> plays = new ArrayList<>();
        if(die1 == die2) {
            getAllPlays(board, player, die1, die2, die1, new Play(), plays, board.getBar(player), 3);
            return plays;
        } else {
            getAllPlays(board, player, die1, die2, die1, new Play(), plays, board.getBar(player), 1);
            TreeSet<Play> noDupes = new TreeSet<>(new PlayMoveComparator());
            noDupes.addAll(plays);
            plays = new ArrayList<>(noDupes);
            Collections.sort(plays, new PlayScoreComparator());
            return plays;
        }
    }

    public void getAllPlays(Board board, int player, int die1, int die2, int currentDie, Play play, ArrayList<Play> plays, int start, int depth){
        for(int i=start; i!=board.getBar(-player); i-=player){
            if (board.isPointSelectable(i, player)) {
                if (i == board.getBar(player)) {
                    Move move = new Move(i, i-currentDie*player, player);
                    if (move.isPossible(board, currentDie)) {
                        Board newBoard = new Board(board);
                        Play newPlay = new Play(play.getMoves());
                        newPlay.addMove(move);
                        move.move(newBoard);
                        if (depth > 0) {
                            getAllPlays(newBoard, player, die1, die2, die2, newPlay, plays, start, depth - 1);
                        } else {
                            newPlay.setMoveOrderScore(getNonTerminalScore(newBoard, player));
                            plays.add(newPlay);
                        }
                    } else if (die1 != die2 && die1 == currentDie && depth > 0){
                        Move move2 = new Move(i, i-die2*player, player);
                        if (move2.isPossible(board, die2)) {
                            Board newBoard = new Board(board);
                            Play newPlay = new Play(play.getMoves());
                            newPlay.addMove(move2);
                            move2.move(newBoard);
                            if (depth > 0) {
                                getAllPlays(newBoard, player, die1, die2, die1, newPlay, plays, start, depth - 1);
                            } else {
                                newPlay.setMoveOrderScore(getNonTerminalScore(newBoard, player));
                                plays.add(newPlay);
                            }
                        }
                    } else {
                        if(play.getSize() != 0){
                            play.setMoveOrderScore(getNonTerminalScore(board, player));
                            plays.add(play);
                        }
                    }
                } else if (board.getCheckersAtPoint(board.getBar(player)) == 0) {
                    Move move = new Move(i, player);
                    if (board.isBearingOff(player) && move.isHomePossible(board, currentDie)) {
                        Board newBoard = new Board(board);
                        Play newPlay = new Play(play.getMoves());
                        newPlay.addMove(move);
                        move.move(newBoard);
                        if (depth > 0) {
                            if (newBoard.checkWin(player)) {
                                newPlay.setMoveOrderScore(getNonTerminalScore(newBoard, player));
                                plays.add(newPlay);
                            } else if (die1 == die2) {
                                getAllPlays(newBoard, player, die1, die2, die2, newPlay, plays, i, depth - 1);
                            } else {
                                getAllPlays(newBoard, player, die1, die2, die2, newPlay, plays, start, depth - 1);
                            }
                        } else {
                            newPlay.setMoveOrderScore(getNonTerminalScore(newBoard, player));
                            plays.add(newPlay);
                        }
                    }
                    move = new Move(i, i-currentDie*player, player);
                    if (move.isPossible(board, currentDie)) {
                        Board newBoard = new Board(board);
                        Play newPlay = new Play(play.getMoves());
                        newPlay.addMove(move);
                        move.move(newBoard);
                        if (depth > 0) {
                            if (die1 == die2) {
                                getAllPlays(newBoard, player, die1, die2, die2, newPlay, plays, i, depth - 1);
                            } else {
                                getAllPlays(newBoard, player, die1, die2, die2, newPlay, plays, start, depth - 1);
                            }
                        } else {
                            newPlay.setMoveOrderScore(getNonTerminalScore(newBoard, player));
                            plays.add(newPlay);
                        }
                    } else if (die1 != die2 && die1 == currentDie && depth > 0) {
                        move = new Move(i, i-die2*player, player);
                        if (move.isPossible(board, die2)) {
                            Board newBoard = new Board(board);
                            Play newPlay = new Play(play.getMoves());
                            newPlay.addMove(move);
                            move.move(newBoard);
                            if (depth > 0) {
                                if (die1 == die2) {
                                    getAllPlays(newBoard, player, die1, die2, die1, newPlay, plays, i, depth - 1);
                                } else {
                                    getAllPlays(newBoard, player, die1, die2, die1, newPlay, plays, start, depth - 1);
                                }
                            } else {
                                newPlay.setMoveOrderScore(getNonTerminalScore(newBoard, player));
                                plays.add(newPlay);
                            }
                        }
                    }
                }
            }
        }
    }

    /*Chance node pruning using star algorithm published by Bruce W. Ballard , 1983.
    * The *-Minimax Search Procedure for Trees Containing Chance Nodes
    * Creates bounds for chance node using alpha, beta, total number of successors (21),
    * highest possible successor value (win), lowest possible successor value (lose).
    * Every time a child returns its value, the bounds are updated using that value in the calculate.*/
    public Play starChance(Board board, int depth, Play currentPlay, int player, boolean maxNext, double alpha, double beta) {
        if (depth < 0 || board.checkWin(player) || board.checkWin(-player)) {
            currentPlay.setScore(getBoardScore(board, player));
            return currentPlay;
        }

        double lBound = -1500000;
        double uBound = 1500000;

        double probabilityLeft = 36;
        double averageSoFar = 0;

        for(int i=0; i<diceRolls.length; i++){
            double probability = diceRolls[i][0] != diceRolls[i][1] ? 2 : 1;
            probabilityLeft -= probability;

            double aBound = (alpha - (uBound * probabilityLeft / 36d) - averageSoFar) / (probability / 36d);
            double bBound = (beta - (lBound * probabilityLeft / 36d) - averageSoFar) / (probability / 36d);

            double newAlpha = aBound > lBound ? aBound : lBound;
            double newBeta = bBound < uBound ? bBound : uBound;

            Play newPlay = callNext(board, depth, currentPlay, diceRolls[i][0], diceRolls[i][1], -player, newAlpha, newBeta, maxNext);
            averageSoFar += newPlay.getScore() * probability / 36d;

            if (newPlay.getScore() <= aBound) {
                averageSoFar += uBound * probabilityLeft / 36d;
                currentPlay.setScore(averageSoFar);
                return currentPlay;
            }
            if (newPlay.getScore() >= bBound) {
                averageSoFar += lBound * probabilityLeft / 36d;
                currentPlay.setScore(averageSoFar);
                return currentPlay;
            }
        }
        currentPlay.setScore(averageSoFar);
        return currentPlay;
    }

    public Play callNext(Board board, int depth, Play currentPlay, int die1, int die2, int player, double alpha, double beta, boolean maxNext) {
        if (maxNext) {
            return maxAB(board, depth, currentPlay, die1, die2, player, alpha, beta);
        } else {
            return minAB(board, depth, currentPlay, die1, die2, player, alpha, beta);
        }
    }

    public Play maxAB(Board board, int depth, Play currentPlay, int die1, int die2, int player, double alpha, double beta) {
        if (depth < 0 || board.checkWin(player) || board.checkWin(-player)) {
            currentPlay.setScore(getBoardScore(board, player));
            return currentPlay;
        }
        Play bestPlay = new Play(-Double.MAX_VALUE);
        ArrayList<Play> plays = findPlays(board, player, die1, die2);
        for (int i=0; i<plays.size(); i++) {
            Board newBoard = new Board(board);
            plays.get(i).makeMoves(newBoard);
            Play newPlay = starChance(newBoard, depth - 1, plays.get(i), player, false, alpha, beta);
            if (newPlay.getScore() > alpha) {
                alpha = newPlay.getScore();
                bestPlay = newPlay;
            }
            if (alpha >= beta) {
                return bestPlay;
            }
        }
        return bestPlay;
    }

    public Play minAB(Board board, int depth, Play currentPlay, int die1, int die2, int player, double alpha, double beta) {
        if (depth < 0 || board.checkWin(player) || board.checkWin(-player)) {
            currentPlay.setScore(getBoardScore(board, player));
            return currentPlay;
        }
        Play bestPlay = new Play(Double.MAX_VALUE);
        ArrayList<Play> plays = findPlays(board, player, die1, die2);
        Collections.reverse(plays);
        for (int i=0; i<plays.size(); i++) {
            Board newBoard = new Board(board);
            plays.get(i).makeMoves(newBoard);
            Play newPlay = starChance(newBoard, depth - 1, plays.get(i), player, true, alpha, beta);
            if (newPlay.getScore() < beta) {
                beta = newPlay.getScore();
                bestPlay = newPlay;
            }
            if(alpha >= beta){
                return bestPlay;
            }
        }
        return bestPlay;
    }

    public Play chance(Board board, int depth, Play currentPlay, int player, boolean maxNext) {
        if (depth < 0 || board.checkWin(player) || board.checkWin(-player)) {
            currentPlay.setScore(getBoardScore(board, player));
            return currentPlay;
        }
        double weightedAverage = 0;
        for(int i=0; i<diceRolls.length; i++){
            Play newMove;
            if (maxNext) {
                newMove = max(board, depth, currentPlay, diceRolls[i][0], diceRolls[i][1], -player);
            } else {
                newMove = min(board, depth, currentPlay, diceRolls[i][0], diceRolls[i][1], -player);
            }
            if (diceRolls[i][0] == diceRolls[i][1]) {
                weightedAverage += (1d / 36d) * newMove.getScore();
            } else {
                weightedAverage += (1d / 16d) * newMove.getScore();
            }
        }
        currentPlay.setScore(weightedAverage);
        return currentPlay;
    }

    public Play max(Board board, int depth, Play currentPlay, int die1, int die2, int player) {
        if (depth < 0 || board.checkWin(player) || board.checkWin(-player)) {
            currentPlay.setScore(getBoardScore(board, player));
            return currentPlay;
        }
        Play bestPlay = new Play(-Double.MAX_VALUE);
        ArrayList<Play> plays = findPlays(board, player, die1, die2);
        for (int i=0; i<plays.size(); i++) {
            Board newBoard = new Board(board);
            plays.get(i).makeMoves(newBoard);
            Play newPlay = chance(newBoard, depth - 1, plays.get(i), player, false);
            if (newPlay.getScore() > bestPlay.getScore()) {
                bestPlay = newPlay;
            }
        }
        return bestPlay;
    }

    public Play min(Board board, int depth, Play currentPlay, int die1, int die2, int player){
        if (depth < 0 || board.checkWin(player) || board.checkWin(-player)) {
            currentPlay.setScore(getBoardScore(board, player));
            return currentPlay;
        }
        Play bestPlay = new Play(Double.MAX_VALUE);
        ArrayList<Play> plays = findPlays(board, player, die1, die2);
        for (int i=0; i < plays.size(); i++) {
            Board newBoard = new Board(board);
            plays.get(i).makeMoves(newBoard);
            Play newPlay = chance(newBoard, depth-1, plays.get(i), player, true);
            if (newPlay.getScore() < bestPlay.getScore()) {
                bestPlay = newPlay;
            }
        }
        return bestPlay;
    }

    public double getNonTerminalScore(Board board, int player){
        double score = 0;
        score += Math.abs(board.getCheckersAtPoint(board.getBar(-player))*10000);
        score -= Math.abs(board.getCheckersAtPoint(board.getBar(player))*10000);
        return score;
    }

    public double getBoardScore(Board board, int player){
        double score = 0;
        score += board.getHome(player)*player*100000;
        score -= board.getHome(-player)*-player*100000;

        score -= board.getCheckersAtPoint(board.getBar(player))*player*10000;
        score += board.getCheckersAtPoint(board.getBar(-player))*-player*10000;

        score -= getSingleCheckersCount(board, player)*7500;
        score += getSingleCheckersCount(board, -player)*7500;

        score -= get3orMoreOnPoint(board, player)*5000;
        score += get3orMoreOnPoint(board, -player)*5000;

        int primeCountPlayer = getPrimeCount(board, player);
        if (primeCountPlayer > 2) {
            score += primeCountPlayer * 5000;
        }
        int primeCountOpponent = getPrimeCount(board, -player);
        if (primeCountOpponent > 2) {
            score -= primeCountOpponent * 5000;
        }
        return score;
    }

    public int getPrimeCount(Board board, int player){
        int bestLength = 0;
        int currentLength = 0;
        boolean found = false;

        for(int i=1; i<=24; i++){
            if(board.getCheckersAtPoint(i)*player > 1){
                currentLength++;
                found = true;
            } else {
                if (found) {
                    found = false;
                    if (currentLength > bestLength) {
                        bestLength = currentLength;
                    }
                    currentLength = 0;
                }
            }
        }
        return bestLength;
    }

    public int getSingleCheckersCount(Board board, int player){
        int count = 0;
        for(int i=1; i<=24; i++){
            if (board.getCheckersAtPoint(i) == player) {
                count++;
            }
        }
        return count;
    }

    public int get3orMoreOnPoint(Board board, int player){
        int count = 0;
        for(int i=1; i<=24; i++){
            if (board.getCheckersAtPoint(i)*player > 2) {
                count++;
            }
        }
        return count;
    }
}