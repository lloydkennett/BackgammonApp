package com.kennett.l.backgammonapp;

import android.os.AsyncTask;
import java.util.ArrayList;

public class StartMoveTask extends AsyncTask <Void, Void, Boolean> {
    Board board;
    int player, start;
    Dice dice;
    ArrayList<Play> possiblePlays;
    InGame inGame;

    public StartMoveTask(Board board, int player, Dice dice, ArrayList<Play> possiblePlays, int start, InGame inGame) {
        this.board = board;
        this.player = player;
        this.dice = dice;
        this.possiblePlays = possiblePlays;
        this.start = start;
        this.inGame = inGame;
    }

    @Override
    protected Boolean doInBackground(Void... params){
        if(dice.getDie1() != 0 && (board.isPointAvailable(start - dice.getDie1() * player, player))){
            Play play = new Play();
            play.addMove(new Move(start, start - dice.getDie1() * player, player));
            possiblePlays.add(play);
        } else if (dice.getDie1() != 0 && board.isBearingOff(player) && board.getFurthestFromHome(player, dice.getDie1()) == start) {
            Play play = new Play();
            play.addMove(new Move(start, -1, player));
            possiblePlays.add(play);
        }
        if(dice.getDie2() != 0 && dice.getDie1() != dice.getDie2() && board.isPointAvailable(start - dice.getDie2() * player, player)){
            Play play = new Play();
            play.addMove(new Move(start, start - dice.getDie2() * player, player));
            possiblePlays.add(play);
        } else if (dice.getDie2() != 0 && board.isBearingOff(player) && board.getFurthestFromHome(player, dice.getDie2()) == start) {
            Play play = new Play();
            play.addMove(new Move(start, -1, player));
            possiblePlays.add(play);
        }
        if(possiblePlays.size() != 0 && dice.getDie1() != 0 && dice.getDie2() != 0 && board.isPointAvailable(start - (dice.getDie1() + dice.getDie2()) * player, player) && board.getCheckersAtPoint(board.getBar(player)) * player < 2){
            if(board.isPointHittable(start - dice.getDie2() * player, player) || !board.isPointAvailable(start - dice.getDie1() * player, player)){
                Play play = new Play();
                play.addMove(new Move(start, start - dice.getDie2() * player, player));
                play.addMove(new Move(start - dice.getDie2() * player, start - (dice.getDie1() + dice.getDie2()) * player, player));
                possiblePlays.add(play);
            } else {
                Play play = new Play();
                play.addMove(new Move(start, start - dice.getDie1() * player, player));
                play.addMove(new Move(start - dice.getDie1() * player, start - (dice.getDie1() + dice.getDie2()) * player, player));
                possiblePlays.add(play);
            }
        }
        if(possiblePlays.size() >= 2 && dice.isDouble() && board.isPointAvailable(start - 3 * dice.getDie1() * player, player)){
            Play play = new Play();
            play.addMove(new Move(start, start - dice.getDie1() * player, player));
            play.addMove(new Move(start - dice.getDie1() * player, start - 2 * dice.getDie1() * player, player));
            play.addMove(new Move (start - 2 * dice.getDie1() * player, start - 3 * dice.getDie1() * player, player));
            possiblePlays.add(play);
        }
        if(possiblePlays.size() >= 3 && dice.isBothDouble() && board.isPointAvailable(start - 4 * dice.getDie1() * player, player)){
            Play play = new Play();
            play.addMove(new Move(start, start - dice.getDie1() * player, player));
            play.addMove(new Move(start - dice.getDie1() * player, start - 2 * dice.getDie1() * player, player));
            play.addMove(new Move(start - 2 * dice.getDie1() * player, start - 3 * dice.getDie1() * player, player));
            play.addMove(new Move (start - 3 * dice.getDie1() * player, start - 4 * dice.getDie1() * player, player));
            possiblePlays.add(play);
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean backgroundFinished) {
        if(possiblePlays.size() != 0){
            inGame.drawSelections(true);
        }
    }
}
