package com.psm2018;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstracker);

        final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final LocationListener mlocListener = new MyLocationListener();

        final Location location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateWithNewLocation(location);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        SendSms(location);

        if (Build.VERSION.SDK_INT >= 21) {
            // Call some material design APIs here
        } else {
            // Implement this feature without material design
        }
    }




    public class MyLocationListener implements LocationListener

    {

        public void onLocationChanged(final Location location) {
//            updateWithNewLocation(location);
//            SendSms(location);
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }


    public void SendSms(Location location) {
        String front = "6";
        String num = Common.currentUser.getEmer().toString();
//        String phoneNo = "60122937090";
        String phoneNo = front+num;
        Toast toast = Toast.makeText(GPSTracker.this,phoneNo, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
        phoneNo = phoneNo.replace("+", "").replace(" ", "");

        double lattitude = location.getLatitude();
        double longitude = location.getLongitude();
        String link = "Heart Rate Monitor have detected that this contact have abnormal heart rate at this location :"+"http://maps.google.com/maps?q=loc:" + String.format("%f,%f", lattitude, longitude);



        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, link, null, null);

//        Intent sendIntent = new Intent("android.intent.action.MAIN");
//        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
//        sendIntent.putExtra("jid", phoneNo +"@s.whatsapp.net");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.setPackage("com.whatsapp");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
//        finish();

//        Intent sendIntent = new Intent("android.intent.action.MAIN");
//        sendIntent.putExtra("jid", phoneNo + "@s.whatsapp.net");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.setPackage("com.whatsapp");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
    }


    private void updateWithNewLocation(final Location location) {
        String latLongString;
        TextView myLocationText;
        myLocationText = (TextView) findViewById(R.id.myLocationText);
        String addressString = "No address found";

        if (location != null) {
            final double lat = location.getLatitude();
            final double lng = location.getLongitude();

            latLongString = "Lat:" + lat + "\nLong:" + lng;
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();
            final Geocoder gc = new Geocoder(this, Locale.getDefault());


            try {
                final List<Address> addresses = gc.getFromLocation(latitude, longitude, 4);
                final StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    final Address address = addresses.get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                }
                addressString = sb.toString();
            } catch (final IOException e) {
            }
        } else {
            latLongString = "No location found";
        }
        myLocationText.setText("Your Current Position is:\n" +
                latLongString + "\n" + addressString);


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // TODO Auto-generated method stub
//        MenuInflater inf = getMenuInflater();
//        inf.inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        String tv1;
//        TextView myLocationText;
//        myLocationText = (TextView)findViewById(R.id.myLocationText);
//        tv1= myLocationText.getText().toString();
//        switch (item.getItemId()) {
//            case R.id.sms_location:
//
//
//                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.putExtra("sms_body", tv1);
//                sendIntent.setType("vnd.android-dir/mms-sms");
//                startActivity(sendIntent);
//
//                return(true);
//            case R.id.email_location:
//        /* Create the Intent */
//                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//
//        /* Fill it with Data */
//                emailIntent.setType("plain/text");
//                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
//                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
//                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, tv1);
//                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                return(true);
//        }
//        return super.onOptionsItemSelected(item);
//
//
//
//}
}
