package com.psm2018;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Profile extends AppCompatActivity {

//    FirebaseDatabase database;
//    DatabaseReference reference;

    TextView user;
    TextView password;
    TextView phonenum,email,emerg;

    private Button button3,button4;
    private ImageView imageView3;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 7;

    FirebaseStorage storage;
    StorageReference storageReference;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_heart = database.getReference("Patient");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

//        StorageReference storageRef = storage.getReference();
//        StorageReference imagesRef = storageRef.child("images");

//        database = FirebaseDatabase.getInstance();
//        reference = database.getReference("Patient");
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        phonenum = (TextView)findViewById(R.id.phonenum);
        phonenum.setText(Common.currentUser.getPhonenum());

//        password = (TextView)findViewById(R.id.password);
//        password.setText(Common.currentUser.getPassword());

        email = (TextView)findViewById(R.id.email);
        email.setText(Common.currentUser.getEmail());

        emerg = (TextView)findViewById(R.id.emer);
        emerg.setText(Common.currentUser.getEmer());

        button3 = (Button) findViewById(R.id.button3);
//        button4 = (Button) findViewById(R.id.button4);

        imageView3 = (ImageView) findViewById(R.id.imageView3);

        System.out.println(Common.currentUser.getImg());
        String xsv =Common.currentUser.getImg();
        if (xsv.equalsIgnoreCase("img"))
        {
            Toast toast = Toast.makeText(Profile.this,"No Profile Image", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/psm2018-674f4.appspot.com/o/images%2Fdefault_avatar_male.png?alt=media&token=d8be5242-11f3-49e4-b40d-5383bbc4f2c2")
                    .into(imageView3);

        }
        else
        {
            Picasso.get()
                    .load(Common.currentUser.getImg())
                    .into(imageView3);
        }

        emerg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

showUpdateDialog();


            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateDialog2();


            }
        });

        phonenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateDialog3();


            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                uploadImage();
//            }
//        });
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess( UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                            Toast.makeText(Profile.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this,"Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                                           {
                                               @Override
                                               public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                                                   double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                                   progressDialog.setMessage("Uploaded"+(int)progress+"%");
                                               }
                                           });
        }
    }

//    @Override
//    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
//        {
//            filePath = data.getData();
//            try{
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                imageView3.setImageBitmap(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String useruid = user.getUid();
        table_heart.child(Common.currentUser.getName()).child("image").setValue(Common.currentUser.getImg());
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            StorageReference filepath = storageReference.child("image").child(filePath.getLastPathSegment());
            filepath.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    table_heart.child(Common.currentUser.getName()).child("image").setValue(downloadUrl);
                    imageView3.setImageURI(filePath);
                }
            });
        }
    }


private void showUpdateDialog(){
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.update_dialog,null);
    dialogBuilder.setView(dialogView);
    final EditText editTextName = (EditText)dialogView.findViewById(R.id.editTextName);
    final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
    dialogBuilder.setTitle("Updating Emergency Contact");
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();
    buttonUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String emerg =  editTextName.getText().toString().trim();


            updateEmerg(emerg);
            alertDialog.dismiss();

        }


    });

}

    private void showUpdateDialog2(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog2,null);
        dialogBuilder.setView(dialogView);
        final EditText editTextEmail = (EditText)dialogView.findViewById(R.id.editTextEmail);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate2);
        dialogBuilder.setTitle("Updating Email");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =  editTextEmail.getText().toString().trim();


                updateEmail(email);
                alertDialog.dismiss();

            }


        });

    }

    private void showUpdateDialog3(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog3,null);
        dialogBuilder.setView(dialogView);
        final EditText editTextPho = (EditText)dialogView.findViewById(R.id.editTextPho);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate3);
        dialogBuilder.setTitle("Updating Phone Number");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonenum =  editTextPho.getText().toString().trim();


                updatePhonenum(phonenum);
                alertDialog.dismiss();

            }


        });

    }

private boolean updateEmerg(String emerg){

    Common.currentUser.setEmer(emerg);
//    Common.currentUser.setPhonenum(phone);
//    Common.currentUser.setEmail(email);
    table_heart.child(Common.currentUser.getName()).child("emer").setValue(emerg);
//    table_heart.child(Common.currentUser.getName()).child("phonenum").setValue(phone);
//    table_heart.child(Common.currentUser.getName()).child("email").setValue(email);

    return true;

}

    private boolean updateEmail(String email){

        Common.currentUser.setEmail(email);
//    Common.currentUser.setPhonenum(phone);
//    Common.currentUser.setEmail(email);
        table_heart.child(Common.currentUser.getName()).child("email").setValue(email);
//    table_heart.child(Common.currentUser.getName()).child("phonenum").setValue(phone);
//    table_heart.child(Common.currentUser.getName()).child("email").setValue(email);

        return true;

    }

    private boolean updatePhonenum(String phonenum){

        Common.currentUser.setPhonenum(phonenum);
//    Common.currentUser.setPhonenum(phone);
//    Common.currentUser.setEmail(email);
        table_heart.child(Common.currentUser.getName()).child("phonenum").setValue(phonenum);
//    table_heart.child(Common.currentUser.getName()).child("phonenum").setValue(phone);
//    table_heart.child(Common.currentUser.getName()).child("email").setValue(email);

        return true;

    }



}