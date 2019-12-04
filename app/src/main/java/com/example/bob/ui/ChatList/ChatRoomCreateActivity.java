package com.example.bob.ui.ChatList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.example.bob.RegisterDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Iterator;

public class ChatRoomCreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Name;
    EditText Age_Start, Age_End;
    TextView Date;
    Button Create, Cancel, DateButton;

    String Hour = "", Min = "", Place = "", Menu = "";
    int Year, Month, Day;
    String User_Name;
    Spinner Hour_Sp, Min_Sp, Place_Sp, Menu_Sp;

    String[] Hours = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "17", "!8", "19", "20", "21", "22", "23"};
    String[] Mins = new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    String[] Places = new String[] {"수원시, 서울시"};
    String[] Menus = new String[] {"분식, 양식"};

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_create);

        Intent intent = getIntent();
        User_Name = intent.getStringExtra("UserName");

        Name = findViewById(R.id.room_name);
        Age_Start = findViewById(R.id.room_age_start);
        Age_End = findViewById(R.id.room_age_end);
        Create = findViewById(R.id.room_create);
        Cancel = findViewById(R.id.room_cancel);
        DateButton = findViewById(R.id.room_date);
        Date = findViewById(R.id.room_date_selected);
        Hour_Sp = findViewById(R.id.room_hour);
        Min_Sp = findViewById(R.id.room_min);
        Place_Sp = findViewById(R.id.room_place);
        Menu_Sp = findViewById(R.id.room_menu);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Hours);
        Hour_Sp.setAdapter(hourAdapter);
        Hour_Sp.setOnItemSelectedListener(this);

        ArrayAdapter<String> minAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_spinner_dropdown_item, Mins);
        Min_Sp.setAdapter(minAdapter);
        Min_Sp.setOnItemSelectedListener(this);

        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Places);
        Place_Sp.setAdapter(placeAdapter);
        Place_Sp.setOnItemSelectedListener(this);

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_spinner_dropdown_item, Menus);
        Menu_Sp.setAdapter(menuAdapter);
        Menu_Sp.setOnItemSelectedListener(this);

        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment();
                dpf.setTextView(Date);
                dpf.show(getFragmentManager(), "datePicker");
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().equals("") || Hour.equals("") || Min.equals("") || Place.equals("") || Menu.equals("") || Date.getText().toString().equals("") || Age_Start.getText().toString().equals("") || Age_End.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "빈 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                else {
                    databaseReference.addListenerForSingleValueEvent(checkRoom);
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        TextView DateFrag;

        public void setTextView(TextView date) {
            DateFrag = date;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();

            return new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            DateFrag.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            DateFrag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == Hour_Sp) {
            Hour = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), Hour, Toast.LENGTH_SHORT).show();
        }
        else if(parent == Min_Sp) {
            Min = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), Min, Toast.LENGTH_SHORT).show();
        }
        else if(parent == Place_Sp) {
            Place = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), Place, Toast.LENGTH_SHORT).show();
        }
        else if(parent == Menu_Sp) {
            Menu = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), Menu, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == Hour_Sp) {
            Hour = "";
        }
        else if(parent == Min_Sp) {
            Min = "";
        }
        else if(parent == Place_Sp) {
            Place = "";
        }
        else if(parent == Menu_Sp) {
            Menu = "";
        }
    }

    private ValueEventListener checkRoom = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

            while(child.hasNext()) {
                if(Name.getText().toString().equals(child.next().getKey())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 채팅방 입니다.", Toast.LENGTH_SHORT).show();
                    databaseReference.removeEventListener(this);
                    return;
                }
            }
            ChatRoomDTO info = new ChatRoomDTO(Name.getText().toString(), Place, Hour, Min, Age_Start.getText().toString(), Age_End.getText().toString(), Menu);
            databaseReference.child(Name.getText().toString()).push().setValue(info);

            Toast.makeText(getApplicationContext(), "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show();

            CreateRoom();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void CreateRoom() {

        Intent intent = new Intent(this, ChatRoomActivity.class);

        intent.putExtra("UserName", User_Name);
        intent.putExtra("RoomName", Name.getText().toString());

        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
