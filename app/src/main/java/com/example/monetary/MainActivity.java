package com.example.monetary;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String, Integer> flags = new HashMap<String, Integer>(){};
    HashMap<String, Integer> currencyIcons = new HashMap<String, Integer>(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flags.put("USD", R.drawable.united_states_of_america);
        flags.put("EUR", R.drawable.european_union);
        flags.put("GBP", R.drawable.united_kingdom);
        flags.put("JPY", R.drawable.japan);
        flags.put("CHF", R.drawable.sweden);

        currencyIcons.put("USD", R.drawable.us_dollar);
        currencyIcons.put("EUR", R.drawable.euro);
        currencyIcons.put("GBP", R.drawable.british_pound);
        currencyIcons.put("JPY", R.drawable.japanese_yen);
        currencyIcons.put("CHF", R.drawable.swiss_franc);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(new ProximitySensor(), proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private class Task extends AsyncTask<String, String, String> {
        @Override
        public String doInBackground(String... strings) {

            TabLayout tl = findViewById(R.id.tabLayout);
            int index = tl.getSelectedTabPosition();
            TabLayout.Tab tab = tl.getTabAt(index);

            assert tab != null;
            String code = (String) tab.getText();

            String uri = "https://economia.awesomeapi.com.br/last/" + code + "-BRL";

            return Connection.getDados(uri);
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView currencyName = findViewById(R.id.currencyName);
                        TextView currentQuote = findViewById(R.id.currentQuote);
                        TextView maximumQuote = findViewById(R.id.maximunQuote);
                        TextView minimumQuote = findViewById(R.id.minimunQuote);
                        ImageView flag = findViewById(R.id.flag);
                        ImageView price = findViewById(R.id.price);

                        try {

                            JSONObject result = new JSONObject(s);
                            JSONObject currency = result.getJSONObject(result.keys().next());

                            String code = currency.getString("code");
                            String name = currency.getString("name");
                            double quotation = currency.getDouble("bid");
                            double maximum = currency.getDouble("high");
                            double minimum = currency.getDouble("low");

                            flag.setImageResource(flags.get(code));
                            price.setImageResource(currencyIcons.get(code));

                            currencyName.setText(name);
                            currentQuote.setText("R$ " + quotation);
                            maximumQuote.setText("R$ " + maximum);
                            minimumQuote.setText("R$ " + minimum);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public class ProximitySensor implements SensorEventListener {

        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public final void onSensorChanged(SensorEvent event) {
            Task task = new Task();
            task.execute();
        }
    }

}