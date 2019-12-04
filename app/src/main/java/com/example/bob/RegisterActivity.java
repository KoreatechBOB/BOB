package com.example.bob;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {

    EditText name, nick, id, pass, year, month, day, sex, comment;

    int isMale = 0;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button regi = findViewById(R.id.regi);
        Button cancel = findViewById(R.id.regi_cancel);

        name = findViewById(R.id.regi_name);
        nick = findViewById(R.id.regi_nickname);
        id = findViewById(R.id.regi_id);
        pass = findViewById(R.id.regi_pass);
        year = findViewById(R.id.birth_year);
        month = findViewById(R.id.birth_month);
        day = findViewById(R.id.birth_day);
        comment = findViewById(R.id.regi_comment);

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().equals("") || nick.getText().equals("") || id.getText().equals("") || pass.getText().equals("") || year.getText().equals("") || month.getText().equals("") || day.getText().equals("") || comment.getText().equals("") || isMale == 0) {
                    Toast.makeText(getApplicationContext(), "모든 칸을 입력해야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(checkRegister);
                }
                    /*
                    try{
                        // 임시로 모든 회원들의 개인정보는 어플에 저장되어있음
                        // 보안상 절대 해서는 안되는 일이지만 테스트용이므로 일단 진행

                        FileOutputStream fos = openFileOutput("User.txt", MODE_APPEND);

                        fos.write(email.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(pass.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(name.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(nick.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(year.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(month.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(day.getText().toString().getBytes());
                        fos.write("\n".getBytes());

                        if(isMale == 1)
                            fos.write("Male".getBytes());
                        else if(isMale == 2)
                            fos.write("Female".getBytes());
                        fos.write("\n".getBytes());
                        fos.write(comment.getText().toString().getBytes());

                        fos.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                     */
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateLogin();
            }
        });
    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch(view.getId()) {
            case R.id.regi_male:
                if(checked)
                    isMale = 1;
                break;
            case R.id.regi_female:
                if(checked)
                    isMale = 2;
                break;
            default:
                break;
        }
    }

    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

            while(child.hasNext()) {
                if(id.getText().toString().equals(child.next().getKey())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    databaseReference.removeEventListener(this);
                    return;
                }
            }
            RegisterDTO info = new RegisterDTO(id.getText().toString(), pass.getText().toString(), name.getText().toString(), nick.getText().toString(), year.getText().toString(), month.getText().toString(), day.getText().toString(), (isMale==1)?"Male":"Female", comment.getText().toString(), 0, 0);
            databaseReference.child(id.getText().toString()).push().setValue(info);

            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 다시 로그인하시기 바랍니다.", Toast.LENGTH_SHORT).show();
            ActivateLogin();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void ActivateLogin() {
        //startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
