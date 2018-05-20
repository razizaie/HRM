package com.psm2018;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class Menu extends Activity{

    ListView list;
    String[] itemname ={
            "Check Heart Rate",
            "Location Transmitter",
            "Profile",
            "History"
    };

    Integer[] imgid={
            R.drawable.check,
            R.drawable.location2,
            R.drawable.profile,
            R.drawable.history

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        System.out.println(Common.currentUser.getName());
        System.out.println(Common.currentUser.getPhonenum());
        System.out.println(Common.currentUser.getPassword());

        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(position==2){
                    Intent intent = new Intent(Menu.this,Profile.class);
                    startActivity(intent);
                }

                if(position==1){
                    Intent intent = new Intent(Menu.this,GPSTracker.class);
                    startActivity(intent);
                }

                if(position==0){
                    Intent intent = new Intent(Menu.this,HeartRateMonitor.class);
                    startActivity(intent);
                }

                if(position==3){
                    Intent intent = new Intent(Menu.this,History.class);
                    startActivity(intent);
                }




                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
}