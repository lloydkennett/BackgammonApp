package com.kennett.l.backgammonapp;

import java.util.Comparator;

public class PlayScoreComparator implements Comparator<Play> {
    @Override
    public int compare(Play play1, Play play2){
        return Double.compare(play2.getMoveOrderScore(), play1.getMoveOrderScore());
    }

}
