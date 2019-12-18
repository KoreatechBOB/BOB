package com.example.bob.ui.ChatList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoomActivity extends AppCompatActivity {
    String User_Name;
    String Room_Name;

    ListView Chat_List;
    ArrayList<HashMap<String, String>>list = new ArrayList<HashMap<String, String>>();
    SimpleAdapter m_Adapter;

    EditText Chat_Enter;
    Button Chat_Send;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Chat");

    ArrayList<String> uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Chat_Enter = findViewById(R.id.chat_enter);
        Chat_Send = findViewById(R.id.chat_send);
      
        Intent intent = getIntent();
        User_Name = intent.getStringExtra("UserName");
        Room_Name = intent.getStringExtra("RoomName");

        uname = new ArrayList<>();
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

    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String nick = "";
            int tmp=0;
            if(list.get(position).get("item2")!=null) {
                nick = list.get(position).get("item2");
                tmp = 0;
            }
            else if (list.get(position).get("item2")==null) {
                nick = list.get(position).get("item1");
                tmp = 1;
            }

            Intent intent;
            intent = new Intent(view.getContext(), ChatUserProfile.class);
            intent.putExtra("nickname",nick);
            intent.putExtra("tmp",tmp);
            startActivity(intent);
        }
    };

    private void addMessage(DataSnapshot dataSnapshot) {
        HashMap<String, String> item = new HashMap<String, String>();
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        if(chatDTO.getUserName().equals(User_Name)) {
            item.put("item2", chatDTO.getUserName());
            item.put("item4", chatDTO.getMessage());

            list.add(item);
        }
        else {
            item.put("item1", chatDTO.getUserName());
            item.put("item3", chatDTO.getMessage());

            list.add(item);
        }
        Chat_List.setSelection(m_Adapter.getCount() - 1);
    }

    private void removeMessage(DataSnapshot dataSnapshot) {
        HashMap<String, String> item = new HashMap<String, String>();
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        if(chatDTO.getUserName().equals(User_Name)) {
            item.put("item2", "");
            item.put("item4", "");

            list.remove(item);
        }
        else {
            item.put("item1", chatDTO.getUserName());
            item.put("item3", chatDTO.getMessage());

            list.remove(item);
        }
    }

    private void openChat(String chatName) {
        Chat_List = findViewById(R.id.chat_list);
        m_Adapter = new SimpleAdapter(this, list, R.layout.chat_listview, new String[] {"item1", "item2", "item3", "item4"}, new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4});
        Chat_List.setAdapter(m_Adapter);
        Chat_List.setOnItemClickListener(onClickListItem);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_room_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        return super.onOptionsItemSelected(item);
    }
}
