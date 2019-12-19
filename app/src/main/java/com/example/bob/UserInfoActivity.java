package com.example.bob;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bob.ui.ChatList.ChatLobbyActivity;
import com.example.bob.ui.FoodStore.FoodStoreActivity;
import com.example.bob.ui.Rating.RatingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    String User_Name;
    TextView Nickname;
    EditText Sangme,Password;
    RatingBar ratingBar;
    ImageView image;
    Uri selectedImageUri;
    String User_id;
    int count = 0;
    RegisterDTO info;
    String key;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("User");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);


        Button change = findViewById(R.id.change);
        image = (ImageView)findViewById(R.id.user_image);
        Nickname = findViewById(R.id.user_nick);
        Sangme = findViewById(R.id.user_comment);
        ratingBar = findViewById(R.id.user_rating);



        String line;
        showInfo();

        try {
            FileInputStream fis = openFileInput("Login.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader fin = new BufferedReader(new InputStreamReader(in));

            line = fin.readLine();  // 이메일
            User_id = line;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        databaseReference.addListenerForSingleValueEvent(checkChange);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
                count++;
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>0) {
                    String filename = Nickname.getText().toString();
                    final StorageReference storageRef = storage.getReferenceFromUrl("gs://bob-project-5ab9a.appspot.com").child("Writeclassimage/" + filename);
                    storageRef.putFile(selectedImageUri);
                }

                info.setComment(Sangme.getText().toString());
                databaseReference.child(User_id).child(key).setValue(info);
                Back();
                Toast.makeText(getApplicationContext(),"수정이 완료되었습니다",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showInfo(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot info : dataSnapshot.getChildren()){
                    if(User_id.equals(info.getKey())){
                        DataSnapshot data = info.getChildren().iterator().next();
                        Nickname.setText(data.child("nick").getValue().toString());

                        StorageReference storageRef = storage.getReferenceFromUrl("gs://bob-project-5ab9a.appspot.com").child("Writeclassimage/"+data.child("nick").getValue().toString());
                        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    Glide.with(getApplicationContext())
                                            .load(task.getResult())
                                            .into(image);
                                }
                            }
                        });
                        Sangme.setText(data.child("comment").getValue().toString());
                        ratingBar.setRating(Float.parseFloat(data.child("rating").getValue().toString())/Float.parseFloat(data.child("num").getValue().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    private ValueEventListener checkChange = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            info = dataSnapshot.child(User_id).getChildren().iterator().next().getValue(RegisterDTO.class);
            key = dataSnapshot.child(User_id).getChildren().iterator().next().getKey();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.button_chatlist :
                startActivity(new Intent(this, ChatLobbyActivity.class));
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.button_foodstore :
                intent = new Intent(this, FoodStoreActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.button_rating :
                intent = new Intent(this, RatingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void Back() {
        startActivity(new Intent(this, ChatLobbyActivity.class));
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
