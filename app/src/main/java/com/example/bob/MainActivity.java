package com.example.bob;

import android.content.Intent;
import android.os.Bundle;

import com.example.bob.ui.ChatList.ChatLobbyActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            wait(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, ChatLobbyActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
