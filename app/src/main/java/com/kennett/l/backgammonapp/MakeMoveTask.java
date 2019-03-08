package com.kennett.l.backgammonapp;

import android.os.AsyncTask;
import java.util.ArrayList;

public class MakeMoveTask extends AsyncTask <Void, Void, Play> {
    private Board board;
    private Dice dice;
    private int player, pointId;
    private ArrayList<Play> possiblePlays;
    private InGame inGame;

    public MakeMoveTask(Board board, int player, Dice dice, ArrayList<Play> possiblePlays, int pointId, InGame inGame) {
        this.board = board;
        this.player = player;
        this.dice = dice;
        this.possiblePlays = possiblePlays;
        this.pointId = pointId;
        this.inGame = inGame;
    }

    @Override
    protected void onPreExecute(){
        inGame.drawProgressBar();
    }

    @Override
    protected Play doInBackground(Void... params){
        for (int i=0; i < possiblePlays.size(); i++) {
            for (int j = 0; j < possiblePlays.get(i).getSize(); j++) {
                if (pointId == possiblePlays.get(i).getMove(j).getEnd()) {
                    return possiblePlays.get(i);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Play play) {
        inGame.drawSelections(false);
        possiblePlays.clear();
        inGame.hideProgressBar();
        if (play != null) {
            inGame.drawPlay(play, 500);
        } else if (board.isPointSelectable(pointId, player) && board.getCheckersAtPoint(board.getBar(player)) == 0){
            new StartMoveTask(board, player, dice, possiblePlays, pointId, inGame).execute();
        }
    }
}
