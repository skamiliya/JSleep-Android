package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CancelPaymentActivity extends AppCompatActivity
{
    Context mContext;TextView returnAmount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_payment);
        mContext = this;
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        returnAmount = findViewById(R.id.PaymentBack);

        returnAmount.setText(BookingActivity.priceCurrency);

        Toast.makeText(mContext, "To Main Menu in 5s", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable()
                {
                    public void run()
                    {
                        Intent move = new Intent(CancelPaymentActivity.this, MainActivity.class);startActivity(move);
                    }
                }, 5000);
    }
}