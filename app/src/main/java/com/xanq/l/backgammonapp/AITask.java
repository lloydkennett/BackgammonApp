package com.xanq.l.backgammonapp;

import android.os.AsyncTask;

public class AITask extends AsyncTask<Void, Void, Play> {
    Board board;
    Dice dice;
    int player;
    InGame inGame;

    public AITask(Board board, Dice dice, int player, InGame inGame) {
        this.board = board;
        this.dice = dice;
        this.player = player;
        this.inGame = inGame;
    }

    @Override
    protected void onPreExecute(){
        inGame.drawProgressBar();
    }

    @Override
    protected Play doInBackground(Void...params){

        long optStart = System.currentTimeMillis();
        boolean unOpt = false;
        Play play = new ExpectiMiniMax().maxAB(board, 2, new Play(), dice.getDie1(), dice.getDie2(), player, -Double.MAX_VALUE, Double.MAX_VALUE);
        if (play.getMoves().isEmpty()) {

            unOpt=true;

            play = new ExpectiMiniMax().max(board, 2, new Play(), dice.getDie1(), dice.getDie2(), player);
        }
        long optTime = (System.currentTimeMillis() - optStart)/1000;

        /*long unoptStart = System.currentTimeMillis();
        Play play2 = new ExpectiMiniMax().max(board, 2, new Play(), dice.getDie1(), dice.getDie2(), player);
        long unoptTime = (System.currentTimeMillis() - unoptStart)/1000;
        System.out.println("Unoptimised AI: Time: "+unoptTime+"s Play: "+play2.toString());*/

        System.out.println("Optimised AI: Time: "+optTime+"s Play: "+play.toString()+"UnOpt: "+unOpt);
        System.out.println(board);
        return play;
    }

    @Override
    protected void onPostExecute(Play play) {
        inGame.hideProgressBar();
        if(play.getSize() != 0) {
            inGame.drawPlay(play, 500);
        } else {
            inGame.drawCancelableDialog("Dice: "+dice.getDie1()+" and "+dice.getDie2()+"" +
                    "\nNo playable moves. Resetting dice.");
        }
    }
}
