package com.example.bob.ui.Rating;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.example.bob.RegisterDTO;
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
import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    String User_Name;

    ListView Rating_List;
    ListView Rating_List2;
    ArrayAdapter<String> room_Adapter;
    ArrayAdapter<String> user_Adapter;

    ArrayList<String> rlist;
    ArrayList<String> ulist;
    int list_state;

    TextView tv;
    EditText et;
    RatingBar rb;
    Button button;

    LinearLayout ll;

    RegisterDTO info;


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

        tv = (TextView)findViewById(R.id.rating_name);
        et = (EditText)findViewById(R.id.rating_reason);
        rb = (RatingBar)findViewById(R.id.rating_star);
        button = (Button)findViewById(R.id.rating_send);

        ll = (LinearLayout)findViewById(R.id.rating_input);
        tv.setText("!@#$");
        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            line = fin.readLine();  // 닉네임
            User_Name = line;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                DatabaseReference dr = databaseReference.child("User").child(tv.getText().toString());
                Map<String, Object> updates = new HashMap<>();
                updates.put(info.toString() + "/rating", 1);
                dr.updateChildren(updates);
            }
        });

        room_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rlist);
        user_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ulist);

        Rating_List = (ListView)findViewById(R.id.rating_list);
        Rating_List2 = (ListView)findViewById(R.id.rating_list2);

        Rating_List.setVisibility(View.VISIBLE);
        Rating_List2.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);

        showRoom();
    }

    private void showRoom() {
        databaseReference.child("User").child(User_Name).child("evaluation").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addRoom(dataSnapshot);

                Rating_List.setAdapter(room_Adapter);
                Rating_List2.setAdapter(user_Adapter);

                Rating_List.setOnItemClickListener(onListItemClick1);
                Rating_List2.setOnItemClickListener(onListItemClick2);
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
    }

    private void addRoom(DataSnapshot dataSnapshot) {
        if ( dataSnapshot.getChildrenCount() != 0 ) {
            rlist.add(dataSnapshot.getKey());
        }
    }
    private void addUser(DataSnapshot dataSnapshot) {
        if ( dataSnapshot.getChildrenCount() != 0 ) {
            String[] s = dataSnapshot.getValue().toString().split(",");
            String[] s2 = new String[s.length];

            for(int i=0; i<s.length; i++) {
                s2[i] = s[i].split("=")[1];

                if(i==s.length-1)
                    s2[i] = s2[i].replace("}", "");
            }
            for(int i=0; i<s2.length; i++) {
                ulist.add(s2[i]);
            }
            System.out.println(ulist);
        }
    }

    private AdapterView.OnItemClickListener onListItemClick1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            databaseReference.child("User").child(User_Name).child("evaluation").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    addUser(dataSnapshot);

                    Rating_List.setVisibility(View.GONE);
                    Rating_List2.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
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
        }
    };

    private AdapterView.OnItemClickListener onListItemClick2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            tv.setText(parent.getItemAtPosition(position).toString());

            Rating_List.setVisibility(View.GONE);
            Rating_List2.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);

            databaseReference.child("User").child(tv.getText().toString()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getValue().getClass().equals(java.util.HashMap.class) ) {
                        info = dataSnapshot.getValue(RegisterDTO.class);
                    }
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
        }
    };
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