package com.psm2018;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultAb extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView heartrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_ab);

        AlertDialog alertDialog = new AlertDialog.Builder(
                ResultAb.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("ABNORMAL HEART BEAT!");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to send your location?");

        // Setting Icon to Dialog
//        alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(ResultAb.this,GPSTracker.class);
                startActivity(intent);

            }
        });

        alertDialog.setButton2("Cancel",new DialogInterface.OnClickListener(){
          public  void onClick(DialogInterface dialog, int which){
              finish();
          }
        });

        // Showing Alert Message
        alertDialog.show();


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Patient");

        System.out.println(Common.currentUser.getName());

        heartrate = (TextView)findViewById(R.id.textView10);
        heartrate.setText(Common.currentUser.getHeartbeat()+" bpm");
        //reference.child(Common.currentUser.getName()).child("heartbeat").setValue(Common.currentUser.getHeartbeat());




    }


}
