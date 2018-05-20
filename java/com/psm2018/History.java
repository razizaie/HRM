package com.psm2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.R.layout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class History extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView listview,times,day;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Patient");

        listview = (ListView)findViewById(R.id.history);
        times = (ListView) findViewById(R.id.time);
        day = (ListView) findViewById(R.id.day);
        final TextView heartss = (TextView) findViewById(R.id.text);
        final DatabaseReference table_heart = database.getReference("Patient");


        String key = table_heart.child(Common.currentUser.getName()).child("heartbeat").push().getKey();



        ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.child(Common.currentUser.getName()).child("heartbeat").getValue();
                System.out.println(dataSnapshot.child(Common.currentUser.getName()).child("heartbeat").getValue());
//                System.out.println(td);
                List<Object> values = new ArrayList<>(td.values());
                List<Object> bpm = new ArrayList<>();
                List<Object> time = new ArrayList<>();
                List<Object> days = new ArrayList<>();
                ArrayAdapter arrayAdapterBPM = new ArrayAdapter(History.this, layout.simple_list_item_1, bpm);
                ArrayAdapter arrayAdapterTime = new ArrayAdapter(History.this, layout.simple_list_item_1, time);
                ArrayAdapter arrayAdapterDay = new ArrayAdapter(History.this, layout.simple_list_item_1, days);


                try {
                    JSONArray gotd = new JSONArray(td.values());
                    JSONObject got = new JSONObject();
                    for (int i = 0; i < gotd.length(); i++) {
                        got = gotd.getJSONObject(i);
                        System.out.println(got.getString("bpm"));
                        System.out.println(got.getString("time"));
                        bpm.add(i, got.getString("bpm"));
                        time.add(i, got.getString("time"));
                        days.add(i,got.getString("day"));

                        arrayAdapterBPM.notifyDataSetChanged();
//                        arrayAdapterTime.notifyDataSetChanged();
                    }
//                    Collections.sort(bpm,Collections.reverseOrder());

                    Collections.sort(time,Collections.reverseOrder());
//                    Collections.reverseOrder((Comparator<Object>) bpm);
                    System.out.println(bpm);
                    System.out.println(time);

                    day.setAdapter(arrayAdapterDay);
                    listview.setAdapter(arrayAdapterBPM);
                    times.setAdapter(arrayAdapterTime);


//                    Common.currentUser.setHeartbeat(got.getString("bpm"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                System.out.println(values);
                //notifyDataSetChanged();


            }


            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long bpm) {


//                String key = table_heart.child(Common.currentUser.getName()).child("heartbeat").push().getKey();
//                (HashMap<String, Object>)(dataSnapshot.child(Common.currentUser.getName()).child("heartbeat").removeValue();
//



            }
        });






    }


}
