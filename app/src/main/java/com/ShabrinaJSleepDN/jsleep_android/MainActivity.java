package com.ShabrinaJSleepDN.jsleep_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import com.ShabrinaJSleepDN.jsleep_android.model.Room;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputStream filepath = null;

        ArrayList<Room> ListRoom = new ArrayList<>();
        ArrayList<String> listId = new ArrayList<>();

        Gson gison = new Gson();
        try {
            filepath = getAssets().open("randomRoomList.json");

            BufferedReader baca = new BufferedReader(new InputStreamReader(filepath));

            Room[] buff = gison.fromJson(baca, Room[].class);

            Collections.addAll(ListRoom, buff);

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Room r : ListRoom) {

            listId.add(r.name);
        }
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listId);

        ListView listView = findViewById(R.id.ListViewId);

        listView.setAdapter(adapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        return
                super.onCreateOptionsMenu(menu);
    }

    public String loadJSONFromAsset() {
        try {
            InputStream is = MainActivity.this.getAssets().open("randomRoomList.json");

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            return
                    json;
        } catch (IOException ex) {
            ex.printStackTrace();
            return
                    null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.person_button:

                Intent inte = new Intent(MainActivity.this, MainAboutMeActivity.class);

                startActivity(inte);

                return
                        true;
            default:
                return
                        super.onOptionsItemSelected(item);
        }
    }
}



