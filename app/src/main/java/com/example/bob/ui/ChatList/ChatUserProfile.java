package com.example.bob.ui.ChatList;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bob.R;
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
import java.net.URI;

public class ChatUserProfile extends AppCompatActivity{

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("User");
    FirebaseStorage storage = FirebaseStorage.getInstance();

    TextView Nickname, Sangme;
    RatingBar ratingBar;
    String User_id;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String nick = getIntent().getStringExtra("nickname");
        String line;
        showProfile();

        image = (ImageView)findViewById(R.id.user_image);

        Nickname = findViewById(R.id.user_nick);
        Sangme = findViewById(R.id.user_comment);
        ratingBar = findViewById(R.id.user_rating);

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


    }

    private void showProfile() {
        final String nick = getIntent().getStringExtra("nickname");
        final int tmp = getIntent().getIntExtra("tmp",0);

        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot info : dataSnapshot.getChildren()){
                            if(User_id.equals(info.getKey())==true&&tmp==0) {
                                DataSnapshot data = info.getChildren().iterator().next();
                                if (nick.equals(data.child("nick").getValue())) {
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
                            else if(User_id.equals(info.getKey())==true&&tmp==1){

                            }
                            else if(User_id.equals(info.getKey())==false&&tmp==0){

                            }
                            for(int i = 0; i<info.getChildrenCount();i++) {
                                if(User_id.equals(info.getKey()) == false && tmp == 1) {
                                    DataSnapshot data = info.getChildren().iterator().next();
                                    if (nick.equals(data.child("nick").getValue())) {
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
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }



}