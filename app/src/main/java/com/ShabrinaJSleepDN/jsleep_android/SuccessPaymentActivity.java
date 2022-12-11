package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SuccessPaymentActivity extends AppCompatActivity {

    Context mContext;TextView paidAmount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_success_payment);
        try
        {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        paidAmount = findViewById(R.id.Wallet);
        mContext = this;
        paidAmount.setText(BookingActivity.priceCurrency);

        Toast.makeText(mContext, "To Main Menu in 5s", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed
                (
                new Runnable() {
                    public void run()
                    {
                        Intent move = new Intent(SuccessPaymentActivity.this, MainActivity.class);
                        startActivity(move);
                    }
                }, 5000);
    }
}