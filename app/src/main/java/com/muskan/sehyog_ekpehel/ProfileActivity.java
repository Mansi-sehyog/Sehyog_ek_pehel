package com.muskan.sehyog_ekpehel;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //for on backPressed arrow
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //data members
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    EditText num_edit,name_edit,locality_edit;
    CircleImageView profileImage;
    Button updateButton;

    String currentUserId;
    final static int galleryPick=1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        UserProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile images");
        final FirebaseUser user = mAuth.getCurrentUser();

        name_edit= this.<EditText>findViewById(R.id.name_edit);
        num_edit= this.<EditText>findViewById(R.id.num_edit);
        locality_edit=this.<EditText>findViewById(R.id.locality_edit);
        updateButton= this.<Button>findViewById(R.id.updateButton);
        loadingBar=new ProgressDialog(this);
        profileImage= this.<CircleImageView>findViewById(R.id.profile_image);

        num_edit.setText(user.getPhoneNumber());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent GalleryIntent=new Intent();
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                GalleryIntent.setType("image/*");
                startActivityForResult(GalleryIntent,galleryPick);
            }
        });

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("Name")) {
                        String name = dataSnapshot.child("Name").getValue().toString();
                        name_edit.setText(name);
                    }
                    if (dataSnapshot.hasChild("Number")) {
                        String number = dataSnapshot.child("Number").getValue().toString();
                        num_edit.setText(number);
                    }
                    if (dataSnapshot.hasChild("Locality")) {
                        String locality = dataSnapshot.child("Locality").getValue().toString();
                        locality_edit.setText(locality);
                    }
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Update your profile!!! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null)
        {
            Uri Imageuri=data.getData();

            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK)
            {
                loadingBar.setTitle("saving info..");
                loadingBar.setMessage("please wait..Updating your profile image");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri=result.getUri();

                StorageReference filePath=UserProfileImageRef.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileActivity.this,"store successfully to firebase...",Toast.LENGTH_SHORT).show();

                            Task<Uri> result=task.getResult().getMetadata().getReference().getDownloadUrl();

                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl=uri.toString();
                                    UsersRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                       /* Intent selfintent=new Intent(Profile.this,Profile.class);
                                                        startActivity(selfintent);*/
                                                       profileImage.setVisibility(View.GONE);
                                                        Toast.makeText(ProfileActivity.this,"profile image stored.",Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }

                                                    else
                                                    {
                                                        String msg=task.getException().getMessage();
                                                        Toast.makeText(ProfileActivity.this,"error occured..:" + msg,Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }

                                                }
                                            });

                                }
                            });


                        }

                    }
                });
            }

            else
            {
                Toast.makeText(ProfileActivity.this,"error occur : image not cropped..",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }
    }

    private void saveUserInformation() {
        String Name=name_edit.getText().toString();
        String Number=num_edit.getText().toString();
        String Locality=locality_edit.getText().toString();

        loadingBar.setTitle("saving info..");
        loadingBar.setMessage("please wait..");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        HashMap userMap= new HashMap();
        userMap.put("Name",Name);
        userMap.put("Number",Number);
        userMap.put("Locality",Locality);


        UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    sendUserToHome();
                    Toast.makeText(ProfileActivity.this,"Profile Updated successfully..",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
                else
                {
                    String message= task.getException().getMessage();
                    Toast.makeText(ProfileActivity.this,"Error occurred...." + message,Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void sendUserToHome()
    {

        Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

