package com.psm2018;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psm2018.R;

import static android.R.attr.name;

public class RegisterPage extends AppCompatActivity {

    EditText userN,password,phonenum,email,heartrate,emer;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        password = (EditText) findViewById(R.id.password);
        userN = (EditText) findViewById(R.id.user);
        phonenum = (EditText) findViewById(R.id.phonenum);
        email = (EditText) findViewById(R.id.email);
        emer = (EditText)findViewById(R.id.emer);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Patient");



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(RegisterPage.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();

                table_user.addListenerForSingleValueEvent (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if already user phone
                        if(dataSnapshot.child(userN.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(RegisterPage.this,"Phone Number already register", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            final DatabaseReference table_heart = database.getReference("Patient");
                            String key = table_heart.child(userN.getText().toString()).child("heartbeat").push().getKey();
                            User user = new User(phonenum.getText().toString(),password.getText().toString(),email.getText().toString(),emer.getText().toString());
                            table_user.child(userN.getText().toString()).setValue(user);
                            table_heart.child(userN.getText().toString()).child("heartbeat").child(key).child("time").setValue("");
                            table_user.child(userN.getText().toString()).child("heartbeat").child(key).child("bpm").setValue("");
                            table_user.child(userN.getText().toString()).child("heartbeat").child(key).child("day").setValue("");
                            table_user.child(userN.getText().toString()).child("image").setValue("img");
                            Toast.makeText(RegisterPage.this,"Sign Up Successful!", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
