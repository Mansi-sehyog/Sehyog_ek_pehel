package com.muskan.sehyog_ekpehel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VolunteerActivity extends AppCompatActivity implements UsersAdapter.SelectedUser{

    RecyclerView recyclerView;

    List<UserModel> userModelList = new ArrayList<>();
    String[] names ={ "Muskan", "Mansi","Khushi","Manak"};
    String[] numbers ={"9760691452","9634084595", "5454545479", "567788094"};

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;

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
        setContentView(R.layout.activity_volunteer);

        recyclerView = findViewById(R.id.recyclerView);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



        for (int i = 0; i < names.length; i++ )
        {
            String name = names[i];
            String number = numbers[i];
            UserModel userModelName = new UserModel(name, number);
            userModelList.add(userModelName);
        }


        UsersAdapter usersAdapter = new UsersAdapter( userModelList, this);
        recyclerView.setAdapter(usersAdapter);

    }

    @Override
    public void selectedUser(UserModel userModel) {
        Intent intent = new Intent(VolunteerActivity.this, SelectedUserActivity.class);
        intent.putExtra("Volunteer",userModel);
        startActivity(intent);

    }


}