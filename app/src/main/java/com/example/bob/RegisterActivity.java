package com.example.bob;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {

    EditText name, nick, id, pass, year, month, day, sex, comment;
    ImageView image;
    Uri selectedImageUri = Uri.parse("");
    String downloadUrl;
    int isMale = 0;
    int count =0;



    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("User");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button regi = findViewById(R.id.regi);
        Button cancel = findViewById(R.id.regi_cancel);
        Button pic = findViewById(R.id.select_pic);

        image = (ImageView)findViewById(R.id.user_image);

        name = findViewById(R.id.regi_name);
        nick = findViewById(R.id.regi_nickname);
        id = findViewById(R.id.regi_id);
        pass = findViewById(R.id.regi_pass);
        year = findViewById(R.id.birth_year);
        month = findViewById(R.id.birth_month);
        day = findViewById(R.id.birth_day);
        comment = findViewById(R.id.regi_comment);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
                count++;
            }
        });
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().equals("") || nick.getText().equals("") || id.getText().equals("") || pass.getText().equals("") || year.getText().equals("") || month.getText().equals("") || day.getText().equals("") || comment.getText().equals("") || isMale == 0) {
                    Toast.makeText(getApplicationContext(), "모든 칸을 입력해야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(checkRegister);
                    if(count>0) {
                        String filename = nick.getText().toString();
                        final StorageReference storageRef = storage.getReferenceFromUrl("gs://bob-project-5ab9a.appspot.com").child("Writeclassimage/" + filename);
                        storageRef.putFile(selectedImageUri);
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateLogin();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            image.setImageURI(selectedImageUri);

        }
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
            RegisterDTO info = new RegisterDTO(id.getText().toString(), pass.getText().toString(), name.getText().toString(), nick.getText().toString(), year.getText().toString(), month.getText().toString(), day.getText().toString(), (isMale==1)?"Male":"Female", comment.getText().toString(), "0", "0");
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
