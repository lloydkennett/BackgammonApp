package com.xanq.l.backgammonapp;

public class Move {
    private int start;
    private int end;
    private int player;

    public Move(int start, int player) {
        this.start = start;
        this.player = player;
        end = -1;
    }

    public Move(int start, int end, int player) {
        this.start = start;
        this.end = end;
        this.player = player;
    }

    public void move(Board board) {
        if (board.isPointHittable(end, player)) {
            board.hitOpponent(end, player);
        }
        if (end == -1) {
            board.moveHome(start, player);
        } else {
            board.movePiece(start, end, player);
        }
    }

    public void resetDice(Dice dice){
        if(end == -1){
            resetDiceBearingOff(dice);
        } else {
            if (dice.getDie1() == getMoveSize()) {
                dice.resetDie1();
            } else if (dice.getDie2() == getMoveSize()) {
                dice.resetDie2();
            }
        }
    }

    public void resetDiceBearingOff(Dice dice){
        int moveSize = getMoveSize();
        if (dice.getDie1() - moveSize >= 0 && dice.getDie2() - moveSize >= 0){
            if (dice.getDie1() - moveSize <= dice.getDie2() - moveSize) {
                dice.resetDie1();
            } else {
                dice.resetDie2();
            }
        } else if (dice.getDie1() - moveSize >= 0 && dice.getDie2() - moveSize < 0) {
            dice.resetDie1();
        } else if (dice.getDie1() - moveSize < 0 && dice.getDie2() - moveSize >= 0) {
            dice.resetDie2();
        }
    }

    public boolean isDiePossible(int die){
        return die != 0 && die == getMoveSize();
    }

    public boolean isPossible(Board board, int die){
        return board.isPointPossible(end) && board.isPointAvailable(end, player) && isDiePossible(die);
    }

    public boolean isHomePossible(Board board, int die1, int die2){
        return board.isBearingOff(player) && (board.getFurthestFromHome(player, die1) == start || board.getFurthestFromHome(player, die2) == start);
    }

    public boolean isHomePossible(Board board, int die){
        return board.isBearingOff(player) && board.getFurthestFromHome(player, die) == start;
    }

    public int getMoveSize(){
        if(end == -1) {
            return player == 1 ? start : 25-start;
        }
        return (start-end) * player;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }

    public int getPlayer(){
        return player;
    }

    public String toString(){
        return start+" -> "+end;
    }
}
