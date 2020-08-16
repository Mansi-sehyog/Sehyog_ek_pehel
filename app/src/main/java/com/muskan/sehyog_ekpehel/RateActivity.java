package com.muskan.sehyog_ekpehel;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class RateActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    RatingBar ratingBar;


    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    String currentUserId;

    WebView webView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

       /*
       //Link to playStore Rating Page

        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://console.firebase.google.com/u/0/project/sehyog-74610/database/sehyog-74610/data");
*/
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        ratingBar = findViewById(R.id.ratingBar);
        button = findViewById(R.id.rating_button);
        textView = findViewById(R.id.rating_textView);

        mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        final FirebaseUser user = mAuth.getCurrentUser();



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(RateActivity.this, String.valueOf(rating), Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(String.valueOf(ratingBar.getRating()));
                Toast.makeText(RateActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();

                addToDatabase(ratingBar.getRating());

            }
        });
    }
    private void addToDatabase(float rating) {
        firebaseAuth = FirebaseAuth.getInstance();

        reference = getInstance().getReference("Ratings");
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        //Saving the data in database
        DatabaseActivity databaseActivity = new DatabaseActivity(id, rating);
        reference.child(user.getUid()).setValue(databaseActivity);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("rating")) {
                        String rating = dataSnapshot.child("rating").getValue().toString();
                        textView.setText("Your rating is : "+rating);
                        ratingBar.setRating(Float.parseFloat(rating));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}