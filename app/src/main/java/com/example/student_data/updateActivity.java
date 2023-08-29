package com.example.student_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class updateActivity extends AppCompatActivity {

    EditText nameText,emailText;
    Button updateButton;
    String key;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db = FirebaseFirestore.getInstance();
        key = getIntent().getStringExtra("key");

        nameText = findViewById(R.id.nameET);
        emailText = findViewById(R.id.emailET);
        updateButton = findViewById(R.id.submitUBTN);
        
        
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String email = emailText.getText().toString();
                db.collection("user").document(key).
                        update("name",name,"email",email).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(updateActivity.this, "Update Successfully✌️", Toast.LENGTH_SHORT).show();
                            Intent getMainActivity = new Intent(updateActivity.this, MainActivity.class);
                            startActivity(getMainActivity);

                        }else{
                            Toast.makeText(updateActivity.this, "Error!🌋", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Log.e("update Key" ,key);

        getAllData(key);
    }

    public void getAllData(String key){
        db.collection("user").document(key).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    StudentData person = value.toObject(StudentData.class);
                    nameText.setText(person.getName());
                    emailText.setText(person.getEmail());
                }
            }
        });
    }
}