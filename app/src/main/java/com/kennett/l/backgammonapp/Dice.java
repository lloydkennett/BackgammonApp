package com.kennett.l.backgammonapp;

import java.util.Random;

public class Dice {
    private int die1;
    private int die2;
    private boolean double1;
    private boolean double2;
    private Random rand;

    public Dice(){
        rand = new Random();
    }

    public void roll(){
        die1 = rand.nextInt(6)+1;
        die2 = rand.nextInt(6)+1;
        if (die1 == die2) {
            double1 = true;
            double2 = true;
        }
    }

    public boolean resetUnplayableDie(Board board, int player){
        if(!isPlayable()){
            return false;
        }
        if (board.getCheckersAtPoint(board.getBar(player))*player > 0) {
            if ((board.isPointAvailable(board.getBar(player) + (die1 * -player), player)&& die1 != 0 ||
                    (board.isPointAvailable(board.getBar(player) + (die2 * -player), player) && die2 != 0))) {
                return false;
            }
        } else {
            for (int i=board.getBar(player)-player; i!=board.getBar(-player); i-=player) {
                if (board.isPointSelectable(i, player)) {
                    Move moveDie1 = new Move(i,  i + (die1 * -player), player);
                    Move moveDie2 = new Move(i,  i + (die2 * -player), player);
                    if ((die1 != 0 && (moveDie1.isPossible(board, die1) || moveDie1.isHomePossible(board, die1, die2))) ||
                            (die2 != 0 && (moveDie2.isPossible(board, die2) || moveDie2.isHomePossible(board, die1, die2)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void resetDie1(){
        if (double1) {
            double1 = false;
        } else if(double2) {
            double2 = false;
        } else {
            die1 = 0;
        }
    }

    public void resetDie2(){
        if (double1) {
            double1 = false;
        } else if(double2) {
            double2 = false;
        } else {
            die2 = 0;
        }
    }

    public void completeReset(){
        resetDie1();
        resetDie1();
        resetDie1();
        resetDie2();
    }

    public boolean isDouble(){
        return double1 || double2;
    }

    public boolean isBothDouble() {
        return double1 && double2;
    }

    public boolean isPlayable(){
        return die1 != 0 || die2 != 0;
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

}
