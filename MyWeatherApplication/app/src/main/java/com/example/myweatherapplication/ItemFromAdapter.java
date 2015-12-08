package com.example.myweatherapplication;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFromAdapter extends Fragment {
    private TextView pressure;
    private TextView windSpeed;
    private TextView temp;
    private ImageView ico;

    private double pressureForPeriod = 0;
    private double windSpeedForPeriod = 0;
    private double tempForPeriod = 0;
    private String icoForPeriod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle dataFromDataBase = getArguments();
        pressureForPeriod = dataFromDataBase.getDouble("pressure");
        windSpeedForPeriod = dataFromDataBase.getDouble("windSpeed");
        tempForPeriod = dataFromDataBase.getDouble("temp");
        icoForPeriod = dataFromDataBase.getString("ico");

        View rootView = inflater.inflate(R.layout.fragment_item_from_adapter, container, false);
        pressure = (TextView)rootView.findViewById(R.id.pressure);
        windSpeed = (TextView)rootView.findViewById(R.id.windSpeedOnItem);
        temp = (TextView)rootView.findViewById(R.id.tempInItem);
        ico = (ImageView)rootView.findViewById(R.id.iconOnItemList);

        pressure.setText(pressure.getText() + " " + Double.toString(pressureForPeriod) + " мм");
        windSpeed.setText(windSpeed.getText() + " " + Double.toString(windSpeedForPeriod) + " м / с");
        temp.setText(temp.getText() + " " + Double.toString(tempForPeriod) + " градусов");
        Glide.with(getActivity())
                .load("http://openweathermap.org/img/w/" + icoForPeriod + ".png")
                .into(ico);
        return rootView;
    }


}
