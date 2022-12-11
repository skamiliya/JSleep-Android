package com.ShabrinaJSleepDN.jsleep_android;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ShabrinaJSleepDN.jsleep_android.model.Account;
import com.ShabrinaJSleepDN.jsleep_android.model.Invoice;
import com.ShabrinaJSleepDN.jsleep_android.model.Payment;
import com.ShabrinaJSleepDN.jsleep_android.request.BaseApiService;
import com.ShabrinaJSleepDN.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    private DatePickerDialog datePickerDialog;private Button dateButtonFrom, dateButtonTo, saveBookButton, payButton, cancelButton;private int ind = 0;
    protected static String from, to, fromSec, toSec;protected static String PayAmount;
    double roomPrice = DetailRoomActivity.sessionRoom.price.price;double accountBalance;
    long DaysNum = 0;

    protected TextView priceText; protected static String priceCurrency;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mApiService = UtilsApi.getApiService();mContext = this;

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        dateButtonFrom = findViewById(R.id.DatePickerFrom);dateButtonTo = findViewById(R.id.DatePickerTo);saveBookButton = findViewById(R.id.ButtonBook);
        payButton = findViewById(R.id.ButtonPay);cancelButton = findViewById(R.id.ButtonCancel);

        TextView balanceText = findViewById(R.id.BalanceWallet);

        saveBookButton.setVisibility(Button.VISIBLE);payButton.setVisibility(Button.GONE);cancelButton.setVisibility(Button.GONE);

        accountBalance = MainActivity.cookies.balance;

        if((DetailRoomActivity.currentPayment != null) && (DetailRoomActivity.currentPayment.status == Invoice.PaymentStatus.WAITING)){
            dateButtonFrom.setText(simpleDateFormat(DetailRoomActivity.currentPayment.from));dateButtonTo.setText(simpleDateFormat(DetailRoomActivity.currentPayment.to));
            dateButtonTo.setEnabled(false);dateButtonFrom.setEnabled(false);

            //Update Balance
            String balanceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(accountBalance);
            balanceText.setText(balanceCurrency);

            priceText = findViewById(R.id.Price);
            priceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(roomPrice * simpleCalcDays(DetailRoomActivity.currentPayment.from, DetailRoomActivity.currentPayment.to));
            PayAmount = priceCurrency;priceText.setText(priceCurrency);

            saveBookButton.setVisibility(Button.GONE);payButton.setVisibility(Button.VISIBLE);cancelButton.setVisibility(Button.VISIBLE);


            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestAcceptPayment(DetailRoomActivity.currentPayment.id);Intent move = new Intent(BookingActivity.this, SuccessPaymentActivity.class);
                    startActivity(move);
                }});

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestCancelPayment(DetailRoomActivity.currentPayment.id);Intent move = new Intent(BookingActivity.this, CancelPaymentActivity.class);startActivity(move);
                }
            });}

        else{
            dateButtonTo.setEnabled(true);dateButtonFrom.setEnabled(true);
            initDatePicker();

            from = getTodaysDate(0);to = getTodaysDate(1);

            fromSec = from;toSec = to;

            dateButtonFrom.setText(from);dateButtonTo.setText(to);


            dateButtonFrom.setOnClickListener
                    (
                            view -> {
                ind = 1;
                datePickerDialog.show();
            });
            dateButtonTo.setOnClickListener(
                    view -> {
                ind = 2;
                datePickerDialog.show();
            });

            saveBookButton.setOnClickListener(view -> {
                DaysNum = calcDays(from, to);
                requestBooking(
                        MainActivity.cookies.id, MainActivity.cookies.renter.id,
                        DetailRoomActivity.sessionRoom.id,
                        formatDate(from), formatDate(to));
            });

            updatePrice();
            String balanceCurrency = NumberFormat.getCurrencyInstance(new Locale
                    ("in", "ID")).format(accountBalance);

            balanceText.setText(balanceCurrency);}

    }

    private String getTodaysDate(int offset)
    {
        Calendar cal = Calendar.getInstance();cal.add(Calendar.DAY_OF_YEAR, offset);
        int year =cal.get(Calendar.YEAR);int month =cal.get(Calendar.MONTH);
        month = month + 1;
        int day =cal.get(Calendar.DAY_OF_MONTH);
        return
                makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
                    DatePicker datePicker,
                    int year,
                    int month,
                    int day)
            {
                month = month + 1;

                String date = makeDateString(day, month, year);
                if(ind == 1){
                    from = date;dateButtonFrom.setText(from);
                    updatePrice();

                }
                else if(ind == 2)
                {
                    String BuffTo = to;to = date;
                    if(calcDays(from, to) >= 1 )
                    {
                        dateButtonTo.setText(to);
                        updatePrice();
                    }else
                    {
                        to = BuffTo;
                        Toast.makeText(mContext,
                                "Min. 1 day of stay",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int tahun = cal.get(Calendar.YEAR);int bulan = cal.get(Calendar.MONTH);int hari = cal.get(Calendar.DAY_OF_MONTH);
        int Style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,
                Style, dateSetListener,
                tahun, bulan, hari);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 30L *24*60*60*1000);
    }
    private String makeDateString(int hari, int bulan, int tahun)
    {
        return getMonthFormat(bulan) + " " + hari + " " + tahun;
    }

    private String getMonthFormat(int month)
    {
        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return null;
    }

    public String formatDate(String date)
    {
        SimpleDateFormat
                simpleDF = new SimpleDateFormat("MMM dd yyyy");
        Date fDate = null;
        try
        {
            fDate = simpleDF.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        assert fDate != null;return sdfFormat.format(fDate);
    }

    public void updatePrice()
    {
        priceText = findViewById(R.id.Price);
        priceCurrency = NumberFormat.getCurrencyInstance(new Locale
                ("in", "ID")).format(roomPrice * calcDays(from, to));
        PayAmount = priceCurrency;
        priceText.setText(priceCurrency);
    }

    public long calcDays(String before,
                         String after)
    {
        SimpleDateFormat SimpleDF = new SimpleDateFormat("MMM dd yyyy");

        Date dateBefore = null;
        Date dateAfter = null;
        try
        {
            dateBefore = SimpleDF.parse(before);
            dateAfter = SimpleDF.parse(after);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        long DiffTime = Math.abs(dateAfter.getTime() - dateBefore.getTime());
        long DiffDay = TimeUnit.DAYS.convert(DiffTime, TimeUnit.MILLISECONDS);
        return DiffDay;
    }

    public long simpleCalcDays(Date before, Date after)
    {
        long DiffTime = Math.abs(after.getTime() - before.getTime());
        return TimeUnit.DAYS.convert(DiffTime,
                TimeUnit.MILLISECONDS);
    }

    public String simpleDateFormat(Date date)
    {
        SimpleDateFormat SimpleDF = new SimpleDateFormat("MMM dd,yyyy");
        assert date != null;
        return SimpleDF.format(date);
    }


    protected Payment requestBooking(
            int buyerId,
            int renterId,
            int roomId,
            String from,
            String to)
    {
        mApiService.createPayment(buyerId,
                renterId,
                roomId,
                from,
                to).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()){
                    Payment payment;
                    payment = response.body();
                    Toast.makeText(mContext, "Booking Successful", Toast.LENGTH_LONG).show();

                    Intent move = new Intent(BookingActivity.this, MainActivity.class);
                    startActivity(move);
                    requestAccount(MainActivity.cookies.id);
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                if(roomPrice * DaysNum  > accountBalance)
                {
                    Toast.makeText(mContext, "Go Top Up", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, "Date Already Booked", Toast.LENGTH_LONG).show();
                }
                Intent move = new Intent(BookingActivity.this, MainActivity.class);
                startActivity(move);
            }
        });return null;
    }

    protected Boolean requestAcceptPayment(int id)
    {
        mApiService.accept(id).enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                if(response.isSuccessful())
                {
                    Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_SHORT).show();
                    requestAccount(MainActivity.cookies.id);

                    Intent move = new Intent(BookingActivity.this, SuccessPaymentActivity.class);

                    startActivity(move);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Payment Failed", Toast.LENGTH_LONG).show();
            }
        });return null;
    }

    protected Boolean requestCancelPayment(int id)
    {
        mApiService.cancel(id).enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(mContext, "Payment Canceled", Toast.LENGTH_LONG).show();

                    requestAccount(MainActivity.cookies.id);

                    Intent move = new Intent(BookingActivity.this, CancelPaymentActivity.class);

                    startActivity(move);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                System.out.println(t.toString());

                Toast.makeText(mContext, "Oops Error", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }
    protected Account requestAccount(int id)
    {
        mApiService.getAccount(id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    Account account;MainActivity.cookies = response.body();
                    System.out.println(MainActivity.cookies.toString());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t)
            {
                System.out.println(t.toString());
                Toast.makeText(mContext, "no Account id=0", Toast.LENGTH_SHORT).show();
            }
        });return null;
    }
}