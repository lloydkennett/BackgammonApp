package com.kennett.l.backgammonapp;

import java.util.ArrayList;

public class Play {
    private ArrayList<Move> moves;
    private double score;
    private double moveOrderScore;

    public Play(){
        score = 0;
        moveOrderScore = 0;
        moves = new ArrayList<>();
    }

    public Play(double score){
        this.score = score;
        moveOrderScore = 0;
        moves = new ArrayList<>();
    }

    public Play(ArrayList<Move> moves){
        this.moves = new ArrayList<>();
        for(int i=0; i<moves.size(); i++){
            this.moves.add(new Move(moves.get(i).getStart(), moves.get(i).getEnd(), moves.get(i).getPlayer()));
        }
    }

    public void makeMoves(Board board) {
        for (int i = 0; i < moves.size(); i++) {
            moves.get(i).move(board);
        }
    }

    public void setScore(double score){
        this.score = score;
    }

    public double getScore(){
        return score;
    }

    public void setMoveOrderScore(double moveOrderScore){
        this.moveOrderScore = moveOrderScore;
    }

    public double getMoveOrderScore(){
        return moveOrderScore;
    }

    public int getSize(){
        return moves.size();
    }

    public void addMove(Move move){
        moves.add(move);
    }

    public ArrayList<Move> getMoves(){
        return moves;
    }

    public Move getMove(int i){
        return moves.get(i);
    }

    public String toString(){
        String string = score + "\t";
        for(int i=0; i<moves.size(); i++){
            string += (moves.get(i).toString()+"\t");
        }
        return string;
    }
}