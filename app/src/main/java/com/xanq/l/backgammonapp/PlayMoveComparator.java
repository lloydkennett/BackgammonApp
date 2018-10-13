package com.xanq.l.backgammonapp;

import java.util.Comparator;

public class PlayMoveComparator implements Comparator<Play>{
    @Override
    public int compare(Play play1, Play play2){
        if((play1.getSize() == 1 && play2.getSize() == 1 && sameMove(play1.getMove(0), play2.getMove(0)) ||
                (sameMove(play1.getMove(0), play2.getMove(0)) &&  sameMove(play1.getMove(1), play2.getMove(1))) ||
                (sameMove(play1.getMove(1), play2.getMove(0)) &&  sameMove(play1.getMove(0), play2.getMove(1))))){
            return 0;
        }
        return 1;
    }

    public boolean sameMove(Move move1, Move move2){
        if(move1.getStart() == move2.getStart() && move1.getEnd() == move2.getEnd() && move1.getPlayer() == move2.getPlayer()){
            return true;
        }
        return false;
    }
}
