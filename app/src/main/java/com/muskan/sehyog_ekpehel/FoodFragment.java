package com.muskan.sehyog_ekpehel;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class FoodFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {


    Spinner spinner;
    Button button;
    private FragmentActivity myContext;

    private  int mDay, mMonth, mYear;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    Calendar cal = Calendar.getInstance();

    public FoodFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_food, container, false);
        spinner= view.<Spinner>findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        button = view.findViewById(R.id.food_button);


        final Button pickDate = view.findViewById(R.id.pickDate);
        final TextView textView = view.findViewById(R.id.date);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textView.setText(sdf.format(myCalendar.getTime()));
            }


        };

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);

                                textView.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();

        reference = getInstance().getReference("FoodRequests");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quantity = spinner.getSelectedItem().toString().trim();
                String madeOn = textView.getText().toString().trim();


                if(quantity.equals("select quantity"))
                {
                    Toast.makeText(getContext(), " Invalid Data.... \n Please select valid quantity !!!", Toast.LENGTH_SHORT).show();
                }
                else if(madeOn.equals("01/01/2020"))
                {
                    Toast.makeText(getContext(), " Invalid Data.... \n Please select valid date !!!", Toast.LENGTH_SHORT).show();
                }
                else if(quantity.equals("select quantity") || madeOn.equals("01/01/2020"))
                {
                    Toast.makeText(getContext(), " Invalid Data....", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String id = user.getUid();
                    //Saving the data in database
                    DatabaseActivity databaseActivity = new DatabaseActivity(id, quantity, madeOn);
                    reference.child(user.getUid()).setValue(databaseActivity);

                    //calling sms service
                    sendSms("quantity : " +quantity+ "\nMade On : " +madeOn);

                    Toast.makeText(getActivity(), "Request Successful !!", Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;

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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Context getApplicationContext() {

        return getContext();
    }

}
