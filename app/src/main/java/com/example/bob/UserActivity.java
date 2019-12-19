package com.example.bob;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_user);

        String line;

        Button revise, close;
        EditText nick, comment;

        // revise = findViewById(R.id.user_revise);
        //close = findViewById(R.id.user_close);
        nick = findViewById(R.id.user_nick);
        comment = findViewById(R.id.user_comment);

        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            for (int i = 0; i < 9; i++) {
                line = fin.readLine();

                if (i == 3)
                    nick.setText(line);
                else if (i == 8)
                    comment.setText(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        });



 */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
