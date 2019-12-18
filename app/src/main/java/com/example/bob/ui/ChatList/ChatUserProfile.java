package com.example.bob.ui.ChatList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.example.bob.ui.FoodStore.FoodStoreActivity;
import com.example.bob.ui.Rating.RatingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatUserProfile extends AppCompatActivity{

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("User");

    TextView Nickname, Sangme;
    RatingBar ratingBar;
    String User_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String nick = getIntent().getStringExtra("nickname");
        String line;
        showProfile();

        Nickname = findViewById(R.id.user_nick);
        Sangme = findViewById(R.id.user_comment);

        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            line = fin.readLine();  // 이메일
            User_id = line;
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void showProfile() {
        final String nick = getIntent().getStringExtra("nickname");
        final int tmp = getIntent().getIntExtra("tmp",0);
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot info : dataSnapshot.getChildren()){
                            if(User_id.equals(info.getKey())==true&&tmp==0) {
                                DataSnapshot data = info.getChildren().iterator().next();
                                if (nick.equals(data.child("nick").getValue())) {
                                    Nickname.setText(data.child("nick").getValue().toString());
                                    Sangme.setText(data.child("comment").getValue().toString());
                                }
                            }
                            else if(User_id.equals(info.getKey())==true&&tmp==1){

                            }
                            else if(User_id.equals(info.getKey())==false&&tmp==0){

                            }
                            for(int i = 0; i<info.getChildrenCount();i++) {
                                if(User_id.equals(info.getKey()) == false && tmp == 1) {
                                    DataSnapshot data = info.getChildren().iterator().next();
                                    if (nick.equals(data.child("nick").getValue())) {
                                        Nickname.setText(data.child("nick").getValue().toString());
                                        Sangme.setText(data.child("comment").getValue().toString());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.button_chatlist :
                startActivity(new Intent(this, ChatLobbyActivity.class));
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.button_foodstore :
                intent = new Intent(this, FoodStoreActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.button_rating :
                intent = new Intent(this, RatingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}