package com.example.bob.ui.Rating;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.example.bob.ui.FoodStore.FoodStoreActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EventListener;

public class RatingActivity extends AppCompatActivity {

    String User_Name;

    ListView Rating_List;
    ListView Rating_List2;
    ArrayAdapter<String> room_Adapter;
    ArrayAdapter<String> user_Adapter;

    ArrayList<String> rlist;
    ArrayList<String> ulist;
    int list_state;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        String line;
        list_state = 0;
        rlist = new ArrayList<>();
        ulist = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            line = fin.readLine();  // 이메일
            line = fin.readLine();  // 이름
            line = fin.readLine();  // 닉네임
            User_Name = line;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        room_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rlist);
        user_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ulist);

        Rating_List = (ListView)findViewById(R.id.rating_list);
        Rating_List2 = (ListView)findViewById(R.id.rating_list2);
        showRoom();
    }

    private void showRoom() {
        databaseReference.child("User").child(User_Name).child("ChatRoom").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addRoom(dataSnapshot);

                Rating_List.setAdapter(room_Adapter);
                Rating_List2.setAdapter(user_Adapter);

                //Rating_List.setVisibility(View.GONE);
                Rating_List2.setVisibility(View.GONE);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        if (rlist.size() == 0) {
            rlist.add("평가할 수 있는 채팅방이 없습니다.");
        }
        if (ulist.size() == 0) {
            ulist.add("평가할 수 있는 채팅방이 없습니다2.");
        }
    }
    private void addRoom(DataSnapshot dataSnapshot) {
        rlist.clear();
        System.out.println(dataSnapshot.getValue());
        if ( dataSnapshot.getChildrenCount() == 0 ) {
            rlist.add(dataSnapshot.getValue().toString());
            System.out.println(rlist);
        }
    }

    /*
        databaseReference.child("Room").child(Select_Room).addChildEventListener(new ChildEventListener() {
        ArrayList<String> childList = new ArrayList<>();
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getChildrenCount() == 0) {
                        childList.add(dataSnapshot.getValue().toString());
                        //databaseReference.child("User").child(dataSnapshot.getValue().toString()).child("evaluation").push().setValue(User_Name);
                    }
                    System.out.println(childList.size());
                    System.out.println(childList);

        }
        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.button_foodstore :
                intent = new Intent(this, FoodStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.button_rating :
                intent = new Intent(this, RatingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}