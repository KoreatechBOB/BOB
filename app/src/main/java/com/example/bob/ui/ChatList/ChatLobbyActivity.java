package com.example.bob.ui.ChatList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.example.bob.ui.FoodStore.FoodStoreActivity;
import com.example.bob.ui.Rating.RatingActivity;
import com.google.firebase.database.ChildEventListener;
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
import java.util.ArrayList;
import java.util.Iterator;

public class ChatLobbyActivity extends AppCompatActivity {

    String User_Name;
    String Select_Room;
    boolean searchList;

    String Place = "", Menu = "";
    int Year = 0, Month = 0, Day = 0, Age_Start = 0, Age_End = 0, Hour = 0;

    Button Create, Search, Mine;

    ListView Room_List;
    ArrayAdapter<String> m_Adapter, s_Adapter;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference databaseReferenceUser = firebaseDatabase.getReference("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lobby);

        String line;

        Intent intent = getIntent();
        searchList = getIntent().getBooleanExtra("Search", false);

        if(searchList) {
            Place = intent.getStringExtra("Place");
            Menu = intent.getStringExtra("Menu");
            Year = intent.getIntExtra("Year", 1);
            Month = intent.getIntExtra("Month", 1);
            Day = intent.getIntExtra("Day", 1);
            Age_Start = intent.getIntExtra("Age_Start", 1);
            Age_End = intent.getIntExtra("Age_End", 1);
            Hour = intent.getIntExtra("Hour", 0);
            ShowChatList();
        }

        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            line = fin.readLine();  // 아이디
            User_Name = line;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        s_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Room_List = findViewById(R.id.chat_room_list);

        ShowMyChatList();

        if(!searchList)
            Room_List.setAdapter(m_Adapter);
        else {
            Room_List.setAdapter(s_Adapter);
        }

        Room_List.setOnItemClickListener(onListItemClick);

        Create = findViewById(R.id.chat_create);
        Search = findViewById(R.id.chat_search);
        Mine = findViewById(R.id.chat_my_list);

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiviateCreate();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateSearch();
            }
        });

        Mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Room_List.setAdapter(m_Adapter);
            }
        });
    }

    private void ShowChatList() {

        databaseReference.child("Room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
                ChatRoomDTO info;

                for(DataSnapshot room : dataSnapshot.getChildren()) {
                    if(room.getValue().getClass().equals(java.util.HashMap.class)) {
                        info = room.getValue(ChatRoomDTO.class);
                        if(info.getPlace().equals(Place) && info.getMenu().equals(Menu) && Integer.valueOf(info.getAgeStart()) == Age_Start && Integer.valueOf(info.getAgeEnd()) == Age_End) {
                            if(info.getYear() > Year || (info.getYear() == Year && info.getMonth() > Month) || (info.getYear() == Year && info.getMonth() == Month && info.getDay() > Day) || (info.getYear() == Year && info.getMonth() == Month && info.getDay() == Day && Integer.valueOf(info.getHour()) > Hour)) {
                            s_Adapter.add(info.getName());
                            }
                        }
                    }
                }
                Room_List.setSelection(s_Adapter.getCount() - 1);
            }

            @Override
            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onChildRemoved( DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    private void ShowMyChatList() {

        databaseReference.child("User").child(User_Name).child("ChatRoom").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
                if(!dataSnapshot.hasChild("name"))
                    m_Adapter.add(dataSnapshot.getValue(String.class));
                Room_List.setSelection(m_Adapter.getCount() - 1);
            }

            @Override
            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onChildRemoved( DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
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
            case R.id.button_userinfo :
                intent = new Intent(this, ChatUserProfile.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Select_Room = parent.getItemAtPosition(position).toString();
            databaseReferenceUser.addListenerForSingleValueEvent(AddChatRoom);
            databaseReference.addListenerForSingleValueEvent(AddUserRoom);
            ActivateRoom(parent.getItemAtPosition(position).toString());
        }
    };

    private ValueEventListener AddChatRoom = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot info : dataSnapshot.child(User_Name).child("ChatRoom").getChildren()) {
                if (info.getValue().getClass().equals(String.class) && info.getValue().toString().equals(Select_Room)) {
                    databaseReferenceUser.removeEventListener(this);
                    return;
                }
            }
            databaseReferenceUser.child(User_Name).child("ChatRoom").push().setValue(Select_Room);
            databaseReferenceUser.removeEventListener(this);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener AddUserRoom = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot info : dataSnapshot.child("Room").child(Select_Room).getChildren()) {
                if (info.getValue().getClass().equals(String.class) && info.getValue().toString().equals(User_Name)) {
                    databaseReference.removeEventListener(this);
                    return;
                }
            }
            databaseReference.child("Room").child(Select_Room).push().setValue(User_Name);
            databaseReference.removeEventListener(this);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void ActivateRoom(String Room) {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("UserName", User_Name);
        intent.putExtra("RoomName", Room);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void ActiviateCreate() {
        Intent intent = new Intent(this, ChatRoomCreateActivity.class);
        intent.putExtra("UserName", User_Name);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void ActivateSearch() {
        Intent intent = new Intent(this, ChatRoomSearchActivity.class);
        intent.putExtra("UserName", User_Name);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}