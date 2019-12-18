package com.example.bob.ui.Rating;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
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

public class RatingActivity extends AppCompatActivity {

    String User_Name;

    ListView Room_List;
    ArrayAdapter<String> m_Adapter, s_Adapter;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        String line;

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

        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void ShowMyChatList() {

        databaseReference.child("User").child(User_Name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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
}