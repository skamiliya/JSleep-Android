package com.ShabrinaJSleepDN.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ShabrinaJSleepDN.jsleep_android.model.Payment;
import com.ShabrinaJSleepDN.jsleep_android.model.Facility;
import com.ShabrinaJSleepDN.jsleep_android.model.Invoice;
import com.ShabrinaJSleepDN.jsleep_android.model.Room;
import com.ShabrinaJSleepDN.jsleep_android.request.BaseApiService;
import com.ShabrinaJSleepDN.jsleep_android.request.UtilsApi;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRoomActivity extends AppCompatActivity
{
    protected static Room RoomSession;
    protected static Payment paymentCurrent;
    List<Facility> facilityList = MainActivity.rooms.get(MainActivity.roomIndex).facility;
    CheckBox checkboxAC, checkboxBalcony, checkboxBathtub, checkboxFitnessCenter, checkboxRefrigerator, checkboxRestaurant, checkboxSwimPool, checkboxWifi;
    Button rentButton, payButton, cancelButton;
    CardView afterRent;
    TextView bookSuccess, bookFailed;
    BaseApiService mApiService;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        mApiService = UtilsApi.getApiService();
        mContext = this;
        RoomSession = MainActivity.rooms.get(MainActivity.roomIndex);
        TextView nameDetail = findViewById(R.id.NamePerson);TextView bedDetail = findViewById(R.id.detail_bedType);
        TextView sizeDetail = findViewById(R.id.detail_size);TextView priceDetail = findViewById(R.id.detail_price);
        TextView addressDetail = findViewById(R.id.AddressPerson);
        bookSuccess = findViewById(R.id.detail_successBook);

        checkboxAC = findViewById(R.id.cb_AC);

        checkboxBalcony = findViewById(R.id.cb_balcony);

        checkboxBathtub = findViewById(R.id.cb_bathtub);

        checkboxFitnessCenter = findViewById(R.id.cb_fitnessCenter);

        checkboxRefrigerator = findViewById(R.id.cb_refrigerator);

        checkboxRestaurant = findViewById(R.id.cb_restaurant);

        checkboxSwimPool = findViewById(R.id.cb_swimmingPool);

        checkboxWifi = findViewById(R.id.cb_wifi);

        fillCheckbox();

        payButton = findViewById(R.id.detail_confirmPay);
        payButton.setVisibility(Button.GONE);
        rentButton = findViewById(R.id.detail_rentButton);
        rentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(DetailRoomActivity.this, BookingActivity.class);
                startActivity(move);
            }
        });

        paymentCurrent = requestGetPayment(MainActivity.cookies.id, MainActivity.cookies.renter.id, RoomSession.id);


        String price = NumberFormat.getCurrencyInstance(new Locale("in",
                "ID")).format(RoomSession.price.price);
        String address = RoomSession.address + ", " + RoomSession.city;

        System.out.println(RoomSession);

        nameDetail.setText(RoomSession.name);
        bedDetail.setText(RoomSession.bedType.toString());

        sizeDetail.setText(RoomSession.size + "m\u00B2");
        priceDetail.setText(price);
        addressDetail.setText(address);


    }

    public void mainLogic(){
        System.out.println(paymentCurrent);
        payButton.setVisibility(Button.GONE);rentButton.setVisibility(Button.VISIBLE);bookSuccess.setVisibility(TextView.INVISIBLE);

        if(paymentCurrent != null && paymentCurrent.status == Invoice.PaymentStatus.WAITING){
            rentButton.setVisibility(Button.GONE);
            payButton.setVisibility(Button.VISIBLE);
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent move = new Intent(DetailRoomActivity.this, BookingActivity.class);
                    startActivity(move);
                }
            });
        }
        else if(paymentCurrent != null && paymentCurrent.status == Invoice.PaymentStatus.SUCCESS){
            payButton.setVisibility(Button.GONE);rentButton.setVisibility(Button.GONE);bookSuccess.setVisibility(TextView.VISIBLE);
        }
    }

    public void fillCheckbox()
    {
        for(Facility facility: facilityList){
            if (facility == Facility.AC){

                checkboxAC.setChecked(true);
            }
            else if(facility == Facility.Balcony){

                checkboxBalcony.setChecked(true);

            }else if(facility == Facility.Bathtub) {

                checkboxBathtub.setChecked(true);

            }else if(facility == Facility.FitnessCenter){

                checkboxFitnessCenter.setChecked(true);

            }else if(facility == Facility.Refrigerator){

                checkboxRefrigerator.setChecked(true);

            }else if(facility == Facility.Restaurant){

                checkboxRestaurant.setChecked(true);

            }else if(facility == Facility.SwimmingPool){

                checkboxSwimPool.setChecked(true);

            }else if(facility == Facility.WiFi){

                checkboxWifi.setChecked(true);

            }
        }
    }

    protected Boolean requestAcceptPayment(int id)
    {
        mApiService.accept(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_SHORT).show();

                    Intent move = new Intent(DetailRoomActivity.this, MainActivity.class);

                    startActivity(move);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {

                System.out.println(t.toString());

                Toast.makeText(mContext, "Payment Failed", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    protected Boolean requestCancelPayment(int id)
    {
        mApiService.cancel(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){

                    Toast.makeText(mContext, "Payment Canceled", Toast.LENGTH_LONG).show();

                    Intent move = new Intent(DetailRoomActivity.this, MainActivity.class);

                    startActivity(move);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                System.out.println(t.toString());

                Toast.makeText(mContext, "Error!!", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    protected Payment requestGetPayment(int buyerId,
                                        int renterId,
                                        int roomId)
    {
        mApiService.getPayment(buyerId, renterId, roomId).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()){
                    paymentCurrent = response.body();
                    System.out.println(paymentCurrent);
                    mainLogic();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t)
            {
                System.out.println(t.toString());
                mainLogic();
            }
        });return null;
    }
}