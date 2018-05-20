package com.psm2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.psm2018.R.id.list;

public class Result extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    ListView listview;
    TextView heartrate;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        heartrate = (TextView) findViewById(R.id.result);
        heartrate.setText(Common.currentUser.getHeartbeat() + " bpm");
        //reference.child(Common.currentUser.getName()).child("heartbeat").setValue(Common.currentUser.getHeartbeat());
//        heartrate = (TextView)findViewById(R.id.textView13);
//        heartrate.setText(Common.currentUser.getHeartbeat()+" bpm");

//        mDatabase = FirebaseDatabase.getInstance().getReference("Patient");
//        final DatabaseReference table_user = database.getReference("Patient");




    }
}