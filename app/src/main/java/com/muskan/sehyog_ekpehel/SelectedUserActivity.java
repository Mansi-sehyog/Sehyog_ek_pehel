package com.muskan.sehyog_ekpehel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class SelectedUserActivity extends AppCompatActivity {

    TextView tvName, tvNumber;
    ImageView imageView;
    FloatingActionButton floatingActionButton;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user);

        tvName = findViewById(R.id.textView_Name);
        tvNumber = findViewById(R.id.textView_Number);
        imageView = findViewById(R.id.imageView2);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();


        if (intent.getExtras() != null) {
            UserModel userModel = (UserModel) intent.getSerializableExtra("Volunteer");

//            tvUser.setText(userModel.getUserName());
            tvName.setText(userModel.getUserName());
            tvNumber.setText(userModel.getUserNumber());
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "tel:" + tvNumber.getText().toString();
                /*
                // For Testing purpose
                String phone = "tel:"+ "9927189790";
                */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(phone));
                if (ActivityCompat.checkSelfPermission(SelectedUserActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

    }
}
