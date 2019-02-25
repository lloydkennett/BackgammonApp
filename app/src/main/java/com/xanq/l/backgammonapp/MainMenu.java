package com.xanq.l.backgammonapp;

import android.content.DialogInterface;
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

    @Override
    public void onResume(){
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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

        String message = "-To start playing roll the dice to select starting player. " +
                "Once selected roll again to determine what moves you can make.\n" +
                "-Select the point you wish to move from first then the point you wish to move to.\n" +
                "-To bear off hold down on the point you wish to bear off from.\n\n" +
                "-For more rules and strategies visit: http://www.bkgm.com/";
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
