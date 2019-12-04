package com.example.bob.ui.ChatList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoomActivity extends AppCompatActivity {

    String User_Name;
    String Room_Name;

    ListView Chat_List, My_Chat_List;
    ArrayList<HashMap<String, String>>list = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>>my_list = new ArrayList<HashMap<String, String>>();
    SimpleAdapter m_Adapter;
    SimpleAdapter my_Adapter;

    EditText Chat_Enter;
    Button Chat_Send;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Chat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Chat_Enter = findViewById(R.id.chat_enter);
        Chat_Send = findViewById(R.id.chat_send);

        Intent intent = getIntent();
        User_Name = intent.getStringExtra("UserName");
        Room_Name = intent.getStringExtra("RoomName");

        openChat(Room_Name);

        Chat_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Chat_Enter.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO(User_Name, Chat_Enter.getText().toString());
                databaseReference.child(Room_Name).push().setValue(chat);
                Chat_Enter.setText("");
            }
        });
    }

    private void addMessage(DataSnapshot dataSnapshot) {
        HashMap<String, String> item = new HashMap<String, String>();
        HashMap<String, String> my_item = new HashMap<String, String>();
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        if(chatDTO.getUserName().equals(User_Name)) {
            my_item.put("item1", chatDTO.getUserName());
            my_item.put("item2", chatDTO.getMessage());

            my_list.add(my_item);

            item.put("item1", "");
            item.put("item2", "");

            list.add(item);
        }
        else {
            my_item.put("item1", "");
            my_item.put("item2", "");

            my_list.add(my_item);

            item.put("item1", chatDTO.getUserName());
            item.put("item2", chatDTO.getMessage());

            list.add(item);
        }
    }

    private void removeMessage(DataSnapshot dataSnapshot) {
        HashMap<String, String> item = new HashMap<String, String>();
        HashMap<String, String> my_item = new HashMap<String, String>();
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        if(chatDTO.getUserName().equals(User_Name)) {
            my_item.put("item1", chatDTO.getUserName());
            my_item.put("item2", chatDTO.getMessage());

            my_list.remove(my_item);

            item.put("item1", "");
            item.put("item2", "");

            list.remove(item);
        }
        else {
            my_item.put("item1", "");
            my_item.put("item2", "");

            my_list.remove(my_item);

            item.put("item1", chatDTO.getUserName());
            item.put("item2", chatDTO.getMessage());

            list.remove(item);
        }
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅

        m_Adapter = new SimpleAdapter(this, list, R.layout.chat_listview, new String[] {"item1", "item2"}, new int[]{R.id.text1, R.id.text2});
        Chat_List = findViewById(R.id.chat_list);
        Chat_List.setAdapter(m_Adapter);

        my_Adapter = new SimpleAdapter(this, my_list, R.layout.chat_listview, new String[] {"item1", "item2"}, new int[]{R.id.text1, R.id.text2});
        My_Chat_List = findViewById(R.id.my_chat_list);
        My_Chat_List.setAdapter(my_Adapter);
        //Chat_List.setOnItemClickListener(onListItemClick);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child(Room_Name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
