package com.psm2018;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

public class Mainmenu extends AppCompatActivity {

 ImageView rellay_chat,rellay_gallery,rellay_weather,imageView13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        imageView13= findViewById(R.id.imageView13);
        rellay_chat = findViewById(R.id.rellay_chat);
        rellay_gallery = findViewById(R.id.rellay_gallery);
        rellay_weather = findViewById(R.id.rellay_weather);


        rellay_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainmenu.this, HeartRateMonitor.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        rellay_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainmenu.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        rellay_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainmenu.this, History.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


         final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        // This tap target will target the back button, we just need to pass its containing toolbar
                        TapTarget.forView(findViewById(R.id.rellay_chat), "Check Heart Rate", "Monitor your heart rate!").id(1)
                 .outerCircleColor(R.color.red)    ,
                        TapTarget.forView(findViewById(R.id.rellay_gallery), "Profile", "View your information!") .id(2)
                 .outerCircleColor(R.color.blabla2)    ,
                        TapTarget.forView(findViewById(R.id.rellay_weather), "History", "View your heart rate history!") .id(3)
                                .outerCircleColor(R.color.blabla)

                );

//imageView13.setOnTouchListener(new View.OnTouchListener() {
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//
//        return true;
//
//    }
//});

imageView13.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        sequence.start();
    }});
    }
}
