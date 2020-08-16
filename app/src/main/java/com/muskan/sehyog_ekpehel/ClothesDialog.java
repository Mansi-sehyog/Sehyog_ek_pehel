package com.muskan.sehyog_ekpehel;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class ClothesDialog extends DialogFragment {
    private EditText mEditText;
    Button submitButton, CancelButton;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    public ClothesDialog() {
        //
    }

    public static ClothesDialog newInstance(String title) {
        ClothesDialog frag = new ClothesDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_clothes, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        submitButton = view.findViewById(R.id.DialogButton);
        CancelButton = view.findViewById(R.id.CancelButton);

        // Fetch arguments from bundle and set title
        String title = "Welcome for kind Help !";
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final String msg = mEditText.getText().toString().trim();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.isEmpty())
                {
                    sendSms("Help for Clothes");
                }
                else {
                    sendSms("Help for Clothes \nMessage :" + msg);
                }
                Toast.makeText(getActivity(), "Thank you for your help", Toast.LENGTH_SHORT).show();
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //startActivity(new Intent(getActivity(), ClothesFragment.class));
            }
        });


    }

    public void sendSms(String message){

        // Method that sends the sms notification of user request to us.
        String no1 = "9760691452";
        String no2 = "9634084595";
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(no1, null, message, pi,null);
        sms.sendTextMessage(no2, null, message, pi,null);

        addToDatabase(message);

    }

    private void addToDatabase(String message) {
        firebaseAuth = FirebaseAuth.getInstance();

        reference = getInstance().getReference("ClothesRequest");
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();

        //Saving the data in database
        DatabaseActivity databaseActivity = new DatabaseActivity(message);
        reference.child(user.getUid()).setValue(databaseActivity);
    }

    private Context getApplicationContext() {

        return getContext();
    }

}