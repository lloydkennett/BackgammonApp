package com.xanq.l.backgammonapp;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class InGame extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private boolean aiOpponent;
    private Dice dice;
    private Board board;
    private ArrayList<Play> possiblePlays;
    private int currentPlayer;
    private int aiColour;
    private ConstraintLayout layout;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private ProgressBar progressBar;
    private MakeMoveTask makeMoveTask;
    private AITask AITask;
    private StartMoveTask startMoveTask;

    @Override
    public void onClick(View view){
        if(board.isPointPossible(Integer.parseInt((String)view.getTag()))){
            pointClick(view);
        }
    }

    @Override
    public boolean onLongClick(View view){
        pointHold(view);
        return true;
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        addPointListeners();
    }

    public void drawProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        removePointListeners();
    }

    public void drawCancelableDialog(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dice.completeReset();
                drawDice();
                setupNextTurn();
                goFullScreen();
            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowLayout = window.getAttributes();
        windowLayout.gravity = Gravity.LEFT;
        dialog.show();
        window.setLayout(1400, 700);
    }

    public void drawExitDialog(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void removePointListeners(){
        for(int i=0; i<=25; i++){
            String pointString = "point" + i;
            ImageView point = findViewById(getResources().getIdentifier(pointString, "id", getPackageName()));
            point.setOnClickListener(null);
            point.setOnLongClickListener(null);
        }
    }

    public void addPointListeners(){
        for(int i=0; i<=25; i++){ //CHANGE THIS BBACK TO i=0; i<=25 to handle bar checkers as points
            String pointString = "point" + i;
            ImageView point = findViewById(getResources().getIdentifier(pointString, "id", getPackageName()));
            point.setOnClickListener(this);
            if(i >= 1 & i<= 6){
                point.setOnLongClickListener(this);
            }
            if(i >= 19 & i<= 24){
                point.setOnLongClickListener(this);
            }
        }
    }

    public void removeCheckers() {
        int checkerCount = 2141165218;
        for (int i = 1; i <= 24; i++) {
            int checkers = Math.abs(board.getCheckersAtPoint(i));
            for (int j = 0; j < checkers; j++) {
                ImageView checker = findViewById(checkerCount);
                layout.removeView(checker);
                checkerCount++;
            }
        }
    }

    public void drawCheckers() {
        int checkerCount = 2141165218;
        for (int i = 1; i < 25; i++) {
            String pointString = "point" + i;
            int pointId = getResources().getIdentifier(pointString, "id", getPackageName());
            int vertConstraint = pointId;
            int checkers = Math.abs(board.getCheckersAtPoint(i));
            for (int j = 0; j < checkers; j++) {
                ConstraintSet set = new ConstraintSet();
                ImageView checkerView = new ImageView(this);
                checkerView.setId(checkerCount);
                checkerCount++;
                if (board.getCheckersAtPoint(i) > 0) {
                    checkerView.setImageResource(R.drawable.counter1);
                } else if (board.getCheckersAtPoint(i) < 0) {
                    checkerView.setImageResource(R.drawable.counter2);
                }
                checkerView.setAdjustViewBounds(true);
                checkerView.setMaxHeight(100);
                checkerView.setMaxWidth(100);

                layout.addView(checkerView);
                set.clone(layout);
                set.applyTo(layout);

                set.connect(checkerView.getId(), ConstraintSet.LEFT, pointId, ConstraintSet.LEFT, 0);
                set.connect(checkerView.getId(), ConstraintSet.RIGHT, pointId, ConstraintSet.RIGHT, 0);

                if (i > 0 && i < 13 && j == 0) {
                    set.connect(checkerView.getId(), ConstraintSet.BOTTOM, vertConstraint, ConstraintSet.BOTTOM, 0);
                    set.applyTo(layout);
                } else if (i > 0 && i < 13 && j < 5) {
                    set.connect(checkerView.getId(), ConstraintSet.BOTTOM, vertConstraint, ConstraintSet.TOP, 0);
                    set.applyTo(layout);
                } else if (i >= 13 && i <= 24 && j == 0) {
                    set.connect(checkerView.getId(), ConstraintSet.TOP, vertConstraint, ConstraintSet.TOP, 0);
                    set.applyTo(layout);
                } else if (i >= 13 && i <= 24 && j < 5) {
                    set.connect(checkerView.getId(), ConstraintSet.TOP, vertConstraint, ConstraintSet.BOTTOM, 0);
                    set.applyTo(layout);
                }
                vertConstraint = checkerView.getId();
            }
        }
        drawBarCheckers(0);
        drawBarCheckers(25);
    }

    public void drawBarCheckers(int point) {
        ImageView bar = findViewById(getResources().getIdentifier("point" + point, "id", getPackageName()));
        TextView barTxt = findViewById(getResources().getIdentifier("point" + point + "Text", "id", getPackageName()));
        if (board.getCheckersAtPoint(point) != 0) {
            bar.setVisibility(View.VISIBLE);
            String checkers = Integer.toString(Math.abs(board.getCheckersAtPoint(point)));
            barTxt.setText(checkers);
        } else {
            bar.setVisibility(View.INVISIBLE);
            barTxt.setText("");
        }

    }

    public void drawSelection(boolean toShow, int id){
        ImageView selectView = findViewById(getResources().getIdentifier("select"+id, "id", getPackageName()));
        if (toShow) {
            selectView.setVisibility(View.VISIBLE);
        } else {
            selectView.setVisibility(View.INVISIBLE);
        }
    }

    public void drawSelections(boolean toShow){
        if(possiblePlays.size() > 0) {
            drawSelection(toShow, possiblePlays.get(0).getMove(0).getStart());
            for(int i=0; i<possiblePlays.size(); i++) {
                drawSelection(toShow, possiblePlays.get(i).getMove(possiblePlays.get(i).getSize()-1).getEnd());
            }
        }
    }

    public void drawRollBtn(){
        if(!dice.isPlayable()) {
            TextView button = findViewById(R.id.btn_dice);
            button.setVisibility(View.VISIBLE);
        }
    }

    public void pointHold(View view) {
        if(dice.isPlayable() && board.isBearingOff(currentPlayer) && aiColour != currentPlayer){
            int pointId = Integer.parseInt(view.getTag().toString());
            Move move = new Move(pointId, currentPlayer);
            if(move.isHomePossible(board, dice.getDie1(), dice.getDie2())){
                drawSelections(false);
                possiblePlays.clear();
                removeCheckers();
                move.move(board);
                drawCheckers();
                move.resetDiceBearingOff(dice);
                drawDice();
                possiblePlays.clear();
                setupNextTurn();
            }
        }
    }

    public void pointClick(View view) {
        int pointId = Integer.parseInt(view.getTag().toString());
        if (dice.isPlayable() && aiColour != currentPlayer) {
            if (possiblePlays.size() == 0) {
                if ((board.isPointSelectable(pointId, currentPlayer) && !board.isPointSelectable(board.getBar(currentPlayer), currentPlayer)) ||
                        board.isPointSelectable(board.getBar(currentPlayer), currentPlayer) && pointId == board.getBar(currentPlayer)) {
                    drawSelections(false);
                    possiblePlays.clear();
                    startMoveTask = new StartMoveTask(board, currentPlayer, dice/*dice.getDie1(), dice.getDie2()*/, possiblePlays, pointId, this);
                    startMoveTask.execute();
                }
            } else if (possiblePlays.get(0).getMove(0).getStart() == pointId) {
                drawSelections(false);
                possiblePlays.clear();
            } else {
                makeMoveTask = new MakeMoveTask(board, currentPlayer, dice, possiblePlays, pointId, this, aiOpponent);
                makeMoveTask.execute();
            }
        }
    }

    public void drawMove(final Move move, int time, final boolean lastMove){
        drawSelection(true, move.getStart());
        if(move.getEnd() != -1){
            drawSelection(true, move.getEnd());
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                removeCheckers();
                move.move(board);
                drawCheckers();
                move.resetDice(dice);
                drawDice();
                drawSelection(false, move.getStart());
                if(move.getEnd() != -1){
                    drawSelection(false, move.getEnd());
                }
                if (lastMove) {
                    hideProgressBar();
                    if (dice.resetUnplayableDie(board, currentPlayer)) {
                        drawCancelableDialog("No playable moves. Resetting dice");
                    } else {
                        setupNextTurn();
                    }
                }
            }
        }, time);
    }

    public void drawPlay(final Play play, int time){
        drawProgressBar();
        for(int i=0; i<play.getSize(); i++) {
            if (i == play.getSize() - 1) {
                drawMove(play.getMove(i), (i+1)*time, true);
            } else {
                drawMove(play.getMove(i), (i+1)*time, false);
            }
        }
    }

    public void setupNextTurn(){
        checkWin();
        if(dice.getDie1() == 0 && dice.getDie2() == 0){
            drawSelections(false);
            possiblePlays.clear();
            currentPlayer = -currentPlayer;
            if(aiColour == currentPlayer) {
                runAi();
            } else {
                drawRollBtn();
            }
        }
    }

    public void runAi(){
        dice.roll();
        drawDice();
        AITask = new AITask(board, dice, currentPlayer, this);
        AITask.execute();
    }

    public void checkWin(){
        if (board.checkWin(1)) {
            drawExitDialog("White wins! Return to home menu");
        } else if (board.checkWin(-1)) {
            drawExitDialog("Black lose! Return to home menu");
        }
    }

    public void rollClick(View view){
        view.setVisibility(View.INVISIBLE);
        dice.roll();
        if(aiColour == currentPlayer) {
            runAi();
        }

        if(aiColour == 0 && aiOpponent){
            if (dice.getDie1() > dice.getDie2()) {
                aiColour = -1;
                currentPlayer = -1;
                drawCancelableDialog("Player is white.\nComputer is black.\n\nPlayer moves first.");
            } else if (dice.getDie1() < dice.getDie2()) {
                aiColour = 1;
                currentPlayer = -1;
                drawCancelableDialog("Player is black.\nComputer is white.\n\nComputer moves first.");
            } else {
                drawDice();
                drawCancelableDialog("Dice are the same.\nPlease re-roll to assign colours.");
            }
        } else {
            if (dice.resetUnplayableDie(board, currentPlayer)) {
                drawCancelableDialog("No playable moves. \n\nResetting dice.");
            } else {
                drawDice();
            }
            drawDice();
        }
    }

    public void drawDie(ImageView imageView, int die){
        imageView.setVisibility(View.VISIBLE);
        if(die == 1) {
            imageView.setImageResource(R.drawable.dice_one);
        } else if(die == 2) {
            imageView.setImageResource(R.drawable.dice_two);
        } else if(die == 3) {
            imageView.setImageResource(R.drawable.dice_three);
        } else if(die == 4) {
            imageView.setImageResource(R.drawable.dice_four);
        } else if(die == 5) {
            imageView.setImageResource(R.drawable.dice_five);
        } else if(die == 6) {
            imageView.setImageResource(R.drawable.dice_six);
        } else {
            imageView.setVisibility(View.INVISIBLE);;
        }
    }

    public void drawDice(){
        ImageView die = findViewById(R.id.die1);
        drawDie(die, dice.getDie1());
        ImageView die2 = findViewById(R.id.die2);
        drawDie(die2, dice.getDie2());
    }

    public void optionClick(View view){
        finish();
    }

    public void stopThread(AsyncTask thread){
        if(thread != null && thread.getStatus() != AsyncTask.Status.FINISHED){
            thread.cancel(true);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopThread(AITask);
        stopThread(makeMoveTask);
        stopThread(startMoveTask);
        removeCheckers();
        dialog = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        currentPlayer = 1;
        aiColour = 0;
        aiOpponent = getIntent().getExtras().getBoolean("aiOpponent");
        dice = new Dice();
        board = new Board();
        possiblePlays = new ArrayList<>();
        layout = findViewById(R.id.constraintLayout);
        builder = new AlertDialog.Builder(this);
        progressBar = findViewById(getResources().getIdentifier("progressBar", "id", getPackageName()));
        addPointListeners();
        drawCheckers();
        drawDice();

    }

    @Override
    public void onResume() {
        super.onResume();
        goFullScreen();
    }

    public void goFullScreen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}