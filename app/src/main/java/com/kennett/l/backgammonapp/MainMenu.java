package com.kennett.l.backgammonapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        builder = new AlertDialog.Builder(this);

    }
    public void aiClick(View view){
        Intent intent = new Intent(MainMenu.this, InGame.class);
        intent.putExtra("aiOpponent", true);
        startActivity(intent);
    }

    public void twoPlayerClick(View view){
        Intent intent = new Intent(MainMenu.this, InGame.class);
        intent.putExtra("aiOpponent", false);
        startActivity(intent);
    }

    public void howToPlay(View view){
        builder.setCancelable(false);
        builder.setView(R.layout.dialog_cancel);
        dialog = builder.create();
        dialog.show();

        String message = "-To start playing choose a game mode: (Single or Two-Player) and " +
                "roll the dice to begin.\n" +
                "-5 checkers are limited to each point\n"+
                "-This game does not currently include the doubling cube. (Future update)\n"+
                "-The computer can sometimes take a while to find moves, especially on double rolls. " +
                "Please be patient.\n\n" +
                "For more rules and strategies visit: www.bkgm.com";
        TextView text = dialog.findViewById(R.id.dialog_text);
        text.setText(message);

        Button button = dialog.findViewById(R.id.dialog_btn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                onResume();
            }
        });
    }

    public void exitClick(View view){
        finish();
    }
}
