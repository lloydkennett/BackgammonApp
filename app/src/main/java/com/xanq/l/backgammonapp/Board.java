package com.xanq.l.backgammonapp;

import java.util.Arrays;

public class Board {
    int[] points;
    int wHome, bHome;

    public Board(){
        points = new int[26];
        setupNewGame();
    }

    public void setupNewGame(){
        points[1] = -2;
        points[6] = 5;
        points[8] = 3;
        points[12] = -5;
        points[13] = 5;
        points[17] = -3;
        points[19] = -5;
        points[24] = 2;
        wHome = 0;
        bHome = 0;
    }

    public Board(Board board){
        points = Arrays.copyOf(board.getPoints(), board.getPoints().length);
        wHome = board.getwHome();
        bHome = board.getbHome();
    }

    public void moveHome(int start, int player){
        points[start] -= player;
        if(player == 1){
            wHome += player;
        } else {
            bHome += player;
        }
    }

    public void movePiece(int start, int end, int player){
        points[start] -= player;
        points[end] += player;
    }

    public void hitOpponent(int end, int player){
        points[end] -= -player;
        points[getBar(-player)] += -player;
    }

    public boolean isPointPossible(int point){
        return point >= 0 && point <= 25;
    }

    public boolean isPointHittable(int point, int player){
        return isPointOnBoard(point) && points[point] == -player;
    }

    public boolean isPointAvailable(int point, int player){
        return isPointOnBoard(point) && points[point] * player < 5 && points[point] * player >= -1;
    }

    public boolean isPointOnBoard(int point){
        return point >= 1 && point <= 24;
    }


    public boolean isPointSelectable(int point, int player){
        return points[point] * player > 0;
    }

    public boolean isBearingOff(int player){
        int count = 0;
        for(int i=getBar(-player)+(6*player); i!=getBar(-player); i-=player){
            if(isPointSelectable(i, player)){
                count += points[i];
            }
        }
        return count + getHome(player) == 15*player;
    }

    public boolean checkWin(int player){
        return (player == 1 && wHome == 15) || (player == -1 && bHome == -15);
    }

    public int getFurthestFromHome(int player, int die) {
        for(int i=getBar(-player)+(6*player); i!=getBar(-player); i-=player) {
            if (isPointSelectable(i, player)) {
                if (die >= (i - getBar(-player)) * player) {
                    return i;
                }
            }
        }
        return 0;
    }

    public int getHome(int player){
        return player == 1 ? wHome : bHome;
    }

    public int getwHome(){
        return wHome;
    }

    public int getbHome(){
        return bHome;
    }

    public int getCheckersAtPoint(int i){
        return points[i];
    }

    public int getBar(int player){
        return player == 1 ? 25 : 0;
    }

    public int[] getPoints(){
        return points;
    }

    public String toString(){
        String string = "";
        for(int i=0; i<=25; i++){
            if(getCheckersAtPoint(i) != 0){
                string += i+": "+getCheckersAtPoint(i)+"   ";
            }
        }
        string += "wHome: "+wHome+" - bHome: "+bHome;
        return string;
    }
}
