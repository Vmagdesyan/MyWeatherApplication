package com.example.myweatherapplication.launch;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myweatherapplication.ItemFromAdapter;
import com.example.myweatherapplication.R;
import com.example.myweatherapplication.dataBase.WorkWithDataBase;
import com.example.myweatherapplication.utils.API;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private MyAsyncTask myATask = new MyAsyncTask();
    private TextView cityV;
    private TextView tempV;
    private TextView weatherV;
    private ListView listWeather;
    private ImageView imageOfWeather;
    SharedPreferences mSettings;

    final static int CITY_COL_INDEX = 1;
    final static int WEATHER_COL_INDEX = 3;
    final static int ICO_COL_INDEX = 4;
    final static int TEMP_COL_INDEX = 1;

    final static String DATES_TIME = "date and time";
    final static String ICON = "icon";
    final static String TEMP = "temp";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CITY = "City";
    public String city = "Rostov-on-Don";
    public interface OnItemPressed {
        void itemPressed(int position, JSONObject jSon);
    }
    public OnItemPressed listener;

    public void setOnItemClickListener(OnItemPressed listener) {
        this.listener = listener;
    }

    public void removeOnItemClickListener() {
        this.listener = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        cityV = (TextView) rootView.findViewById(R.id.city);
        tempV = (TextView) rootView.findViewById(R.id.temp);
        weatherV = (TextView) rootView.findViewById(R.id.weather);
        imageOfWeather = (ImageView) rootView.findViewById(R.id.imageOfWeather);
        listWeather = (ListView) rootView.findViewById(R.id.listWeather);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            city = mSettings.getString(APP_PREFERENCES_CITY,
                    "Rostov-on-Don");
        myATask.execute(city);

        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            API.ApiResponse ap = API.execute("forecast", API.HttpMethod.GET, "q", params[0], "lang", "ru", "type", "like", "units",
                    "metric", "APPID", "bb1c655f7a4998d7f39918e3058d0699");
            JSONObject result;
            result = ap.getJson();
            return result;
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            super.onPostExecute(result);
            WorkWithDataBase workWithDataBase = new WorkWithDataBase(result, getActivity() );
            String[] datesAndTime = workWithDataBase.getDates();
            String[] tempForPeriod;
            int[] ico = workWithDataBase.getIcons();
            tempForPeriod = workWithDataBase.getTempForDates();
            cityV.setText(cityV.getText() + "\n" + workWithDataBase.getStringValues(CITY_COL_INDEX));
            tempV.setText(tempV.getText() + " " + Math.round(workWithDataBase.getDoubleValues(TEMP_COL_INDEX)));
            weatherV.setText(weatherV.getText() + " " + workWithDataBase.getStringValues(WEATHER_COL_INDEX));

            Glide.with(getActivity())
                    .load("http://openweathermap.org/img/w/" + workWithDataBase.getStringValues(ICO_COL_INDEX)
                            + ".png")
                    .into(imageOfWeather);

            final ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(datesAndTime.length);
            Map<String, Object> m;
            for (int i = 0; i < datesAndTime.length; i++) {
                m = new HashMap<>();
                m.put(DATES_TIME, datesAndTime[i]);
                m.put(ICON, ico[i]);
                m.put(TEMP, tempForPeriod[i]);
                data.add(m);
            }

            String[] from = {ICON, DATES_TIME, TEMP};
            int[] to = {R.id.iconForList, R.id.dateTextView, R.id.tempTextView};

            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), data, R.layout.item, from, to);
            listWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listener.itemPressed(position, result);
                }
            });
            listWeather.setAdapter(simpleAdapter);
        }
    }
}
