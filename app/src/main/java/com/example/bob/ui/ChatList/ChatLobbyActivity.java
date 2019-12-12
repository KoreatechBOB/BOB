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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatLobbyActivity extends AppCompatActivity {

    String User_Name;

    Button Create, Search;

    ListView Room_List;
    ArrayAdapter<String> m_Adapter;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lobby);

        String line;

        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Room_List = findViewById(R.id.chat_room_list);
        Room_List.setAdapter(m_Adapter);
        Room_List.setOnItemClickListener(onListItemClick);

        ShowChatList();
        MakeRoomList();

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
        Toast.makeText(getApplicationContext(), User_Name, Toast.LENGTH_SHORT).show();

        Create = findViewById(R.id.chat_create);
        Search = findViewById(R.id.chat_search);

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiviateCreate();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ActivateSearch();
            }
        });
    }

    private void ShowChatList() {

        databaseReference.child("Room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
                m_Adapter.add(dataSnapshot.getKey());
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
        switch(item.getItemId()) {
            case R.id.button_foodstore :
                Intent intent = new Intent(this, FoodStoreActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivateRoom(parent.getItemAtPosition(position).toString());
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
    }

    private void ActiviateSearch() {
        Intent intent = new Intent(this, ChatRoomSearchActivity.class);
        intent.putExtra("UserName", User_Name);
        startActivity(intent);
    }

    private void MakeRoomList() {
        try {
            FileOutputStream fos = openFileOutput("RoomList.txt", MODE_PRIVATE);
            for(int i = 0; i < m_Adapter.getCount(); i++) {
                fos.write(m_Adapter.getItem(i).getBytes());
                fos.write("\n".getBytes());
            }
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
}