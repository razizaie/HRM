package com.psm2018;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.w3c.dom.Text;

public class SignIn extends AppCompatActivity {
    EditText userN,password;
    Button bSignIn,button2;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v){
                Intent intent = new Intent(SignIn.this,RegisterPage.class);
                startActivity(intent);
            }
        });

        password = (EditText) findViewById(R.id.password);
        userN = (EditText) findViewById(R.id.user);
        bSignIn = (Button) findViewById(R.id.bSignIn);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Patient");


        bSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();

                table_user.addListenerForSingleValueEvent (new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if user not exist in database
                        if (dataSnapshot.child(userN.getText().toString()).exists()) {
                            //Get User Information
                            mDialog.dismiss();
                            String pass = dataSnapshot.child(userN.getText().toString()).child("password").getValue(String.class);
                            User user = new User();
                            user.setName(userN.getText().toString());
                            if (pass.equals(password.getText().toString())) {
                                {
                                    Intent homeIntent = new Intent(SignIn.this,Mainmenu.class);
                                    String phone = dataSnapshot.child(userN.getText().toString()).child("phonenum").getValue(String.class);
                                    String email = dataSnapshot.child(userN.getText().toString()).child("email").getValue(String.class);
                                    String password = dataSnapshot.child(userN.getText().toString()).child("password").getValue(String.class);
                                    String emer = dataSnapshot.child(userN.getText().toString()).child("emer").getValue(String.class);
                                    String img = dataSnapshot.child(userN.getText().toString()).child("image").getValue(String.class);
//                                    user.setPhonenum(phone);
//                                    user.setName(email);
//                                    user.setPassword(password);
                                    user.setall(phone,password,email,emer,img);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(SignIn.this, "Incorrect Password !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User doesn't exist in Database!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){
                    }
                });
            }
        });

    }
}
