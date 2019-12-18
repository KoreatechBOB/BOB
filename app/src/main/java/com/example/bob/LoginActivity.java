package com.example.bob;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bob.ui.ChatList.ChatLobbyActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    EditText id, pass;
    Button login, regi;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = findViewById(R.id.login_id);
        pass = findViewById(R.id.login_pass);
        login = findViewById(R.id.login);
        regi = findViewById(R.id.login_register);

        /*
        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            String line = fin.readLine();

            if (line != null) {
                ActivateMain();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

         */

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.addListenerForSingleValueEvent(checkLogin);
            }
        });
    }

    public void ActivateRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    public void ActivateMain() {
        startActivity(new Intent(this, ChatLobbyActivity.class));
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private ValueEventListener checkLogin = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot LoginData : dataSnapshot.getChildren()) {
                if(id.getText().toString().equals(LoginData.getKey())) {
                    DataSnapshot data = LoginData.getChildren().iterator().next();
                    RegisterDTO info = data.getValue(RegisterDTO.class);
                    if(pass.getText().toString().equals(data.child("pass").getValue())) {
                        try {
                            FileOutputStream fos = openFileOutput("Login.txt", MODE_PRIVATE);
                            fos.write(id.getText().toString().getBytes());
                            fos.write("\n".getBytes());
                            fos.write(info.getName().getBytes());
                            fos.write("\n".getBytes());
                            fos.write(info.getNick().getBytes());
                            fos.write("\n".getBytes());
                            fos.write(info.getYear().getBytes());
                            fos.write("\n".getBytes());
                            fos.write(info.getSex().getBytes());
                            fos.write("\n".getBytes());
                            fos.write(info.getComment().getBytes());
                            fos.write("\n".getBytes());
                            fos.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        ActivateMain();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
