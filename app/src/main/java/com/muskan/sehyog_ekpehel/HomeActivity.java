package com.muskan.sehyog_ekpehel;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView button;
    ImageView imageview;
    Spinner spinner;

    String[] appPermission = {
      Manifest.permission.INTERNET,
      Manifest.permission.READ_SMS,
      Manifest.permission.SEND_SMS,
      Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Food");
        getFragment(new FoodFragment());

        button = findViewById(R.id.bottom_view);
        spinner = findViewById(R.id.spinner);
        imageview = findViewById(R.id.imageView);

        if (checkRequestPermission())
        {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

         button.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.navigation_clothes)
                {
                    getSupportActionBar().setTitle("CLOTHES");
                    getFragment(new ClothesFragment());
                }
                else if(item.getItemId() == R.id.navigation_food)
                {
                    getSupportActionBar().setTitle("FOOD");
                    getFragment(new FoodFragment());
                }
                else if(item.getItemId() == R.id.navigation_donate)
                {
                    getSupportActionBar().setTitle("DONATE");
                    getFragment(new DonateFragment());
                }
                return false;
            }
        });

    }

    // Function to check and request permission
    private boolean checkRequestPermission() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : appPermission){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return  true;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setMessage("Do you want to exit ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
                onDestroy();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setCancelable(false);
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    private void getFragment(Fragment fragment) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=manager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.menu_profile)
        {
            //Directs to the profile page.

            Intent i=new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.menu_volunteer)
        {
            //Directs to the details of Volunteers.

            Intent intent = new Intent(HomeActivity.this, VolunteerActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.menu_about_us)
        {
            //Directs to the About Us page.

            Dialog dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.activity_about);
            dialog.show();
            return  true;
        }
        else if(id == R.id.menu_rate_us)
        {
            //Directs to the Rate us page.

            Intent i = new Intent(HomeActivity.this,RateActivity.class);
            startActivity(i);
            Toast.makeText(this, "Please Rate us", Toast.LENGTH_SHORT).show();
            return  true;
        }
        else if (id == R.id.menu_share)
        {
            //To share the application link (if uploaded on playStore)
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                e.toString();
            }
            return true;
        }
        else if (id == R.id.menu_logout)
        {
            //This logout the current logged in session and directs you to the login page, next time when app is opened you need to login again.

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Successful logout", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.menu_feeds)
        {
            Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
            startActivity(intent);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


}
