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

import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ChatRoomSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Name;
    EditText Age_Start, Age_End;
    TextView Date;
    Button Search, Cancel, DateButton;

    String Hour = "00", Place = "수원시", Menu = "분식";
    int Year, Month, Day;
    String User_Name;
    Spinner Hour_Sp, Place_Sp, Menu_Sp;

    String[] Hours = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "17", "!8", "19", "20", "21", "22", "23"};
    String[] Places = new String[] {"수원시", "서울시"};
    String[] Menus = new String[] {"분식", "양식"};

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_search);

        Intent intent = getIntent();
        User_Name = intent.getStringExtra("UserName");

        Name = findViewById(R.id.room_name);
        Age_Start = findViewById(R.id.room_age_start);
        Age_End = findViewById(R.id.room_age_end);
        Search = findViewById(R.id.room_search);
        Cancel = findViewById(R.id.room_cancel);
        DateButton = findViewById(R.id.room_date);
        Date = findViewById(R.id.room_date_selected);
        Hour_Sp = findViewById(R.id.room_hour);
        Place_Sp = findViewById(R.id.room_place);
        Menu_Sp = findViewById(R.id.room_menu);

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Hours);
        Hour_Sp.setAdapter(hourAdapter);
        Hour_Sp.setOnItemSelectedListener(this);

        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Places);
        Place_Sp.setAdapter(placeAdapter);
        Place_Sp.setOnItemSelectedListener(this);

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_spinner_dropdown_item, Menus);
        Menu_Sp.setAdapter(menuAdapter);
        Menu_Sp.setOnItemSelectedListener(this);

        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoomCreateActivity.DatePickerFragment dpf = new ChatRoomCreateActivity.DatePickerFragment();
                dpf.setTextView(Date);
                dpf.show(getFragmentManager(), "datePicker");
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Hour.equals("") || Place.equals("") || Menu.equals("") || Date.getText().toString().equals("") || Age_Start.getText().toString().equals("") || Age_End.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "빈 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                else if(Integer.valueOf(Age_Start.getText().toString()) > Integer.valueOf(Age_End.getText().toString()))
                    Toast.makeText(getApplicationContext(), "나이 제한이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                else {
                    getDate();
                    SearchRoom();
                }
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
        }
        else if(parent == Place_Sp) {
            Place = parent.getItemAtPosition(position).toString();
        }
        else if(parent == Menu_Sp) {
            Menu = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == Hour_Sp) {
            Hour = "00";
        }
        else if(parent == Place_Sp) {
            Place = "수원시";
        }
        else if(parent == Menu_Sp) {
            Menu = "분식";
        }
    }

    private void SearchRoom() {
        Intent intent = new Intent(this, ChatLobbyActivity.class);

        intent.putExtra("Search", true);
        intent.putExtra("Place", Place);
        intent.putExtra("Hour", Integer.valueOf(Hour));
        intent.putExtra("Age_Start", Integer.valueOf(Age_Start.getText().toString()));
        intent.putExtra("Age_End", Integer.valueOf(Age_End.getText().toString()));
        intent.putExtra("Menu", Menu);
        intent.putExtra("Year", Year);
        intent.putExtra("Month", Month);
        intent.putExtra("Day", Day);

        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void getDate() {

        String date = Date.getText().toString();
        String[] dateString = date.split("/");

        Year = Integer.valueOf(dateString[0]);
        Month = Integer.valueOf(dateString[1]);
        Day = Integer.valueOf(dateString[2]);
    }
}
