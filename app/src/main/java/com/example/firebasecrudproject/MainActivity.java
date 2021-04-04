package com.example.firebasecrudproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private Button button;

    ImageButton imageButton;
    private EditText editText;
    private TextView textView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("notes");

    private NotesAdapter notesAdapter;
    private List<Notes> notes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton = findViewById(R.id.floats);

        readData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });


    }

    private void showDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        imageButton = dialog.findViewById(R.id.imageButtonId);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    final EditText editText = dialog.findViewById(R.id.editTextId);




        button = dialog.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText())) {

                    editText.setError("This field can not be empty");
                } else {

                    addDataToFirebase(editText.getText().toString());

                }
            }
        });


        dialog.show();

    }


    private void addDataToFirebase(String text) {


        // Write a message to the database


        String id = myRef.push().getKey();
        Notes notes = new Notes(id, text);

        myRef.child(id).setValue(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Toast.makeText(MainActivity.this, "Data has been stored successfully", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void readData() {


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                notes.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Notes value = dataSnapshot1.getValue(Notes.class);
                    notes.add(value);

                }

                notesAdapter = new NotesAdapter(MainActivity.this, notes);

                recyclerView.setAdapter(notesAdapter);
                //For Updation and deletion

                deleteAndEditClicks();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }

    //Edit and Delete

    private void deleteAndEditClicks() {

        notesAdapter.setOnCallBack(new NotesAdapter.OnCallBack() {
            @Override
            public void onButtonDeleteClick(Notes notes) {
                deleteNotes(notes);
            }

            @Override
            public void onButtonEditClick(Notes notes) {
                editNotes(notes);

            }
        });
    }



    private void deleteNotes(Notes notes) {

        myRef.child(notes.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(),"Data Has Been Deleted " +notes.getText(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void editNotes(Notes notes) {


    }


}