package com.example.myweatherapplication.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.myweatherapplication.R;
import com.example.myweatherapplication.utils.ParseJSON;

import org.json.JSONObject;

import static com.example.myweatherapplication.dataBase.DataBase.TABLE_NAME;

/**
 * Created by Влад on 22.11.2015.
 */
public class WorkWithDataBase {
    final static String LOG_TAG = "DataBase";

    static SQLiteDatabase dBase;
    static Context cont;
    ContentValues cv = new ContentValues();

    private int cityColIndex;
    private int dateAndTimeColIndex;
    private int tempColIndex;
    private int countryColIndex;
    private int weatherColIndex;
    private int pressureColIndex;
    private int windSpeedColIndex;
    private int icoColIndex;

    static int SIZE_OF_LIST;

    public WorkWithDataBase(JSONObject jObject, Context context) {
        cont = context;
        ParseJSON pJson = new ParseJSON(jObject);
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();
        Cursor c = dBase.query(TABLE_NAME, null, null, null, null, null, null);
        showDB(c);
        SIZE_OF_LIST = c.getCount();
        if (!pJson.isNull()) {
            if (c.getCount() == 0) {
                for (int i = 0; i < SIZE_OF_LIST; i++) {
                    pJson.setCountInArray(i);
                    cv.put(DataBase.DATE_AND_TIME, pJson.getDateAndTime());
                    cv.put(DataBase.CITY, pJson.getCity());
                    cv.put(DataBase.COUNTRY, pJson.getCountry());
                    cv.put(DataBase.WEATHER, pJson.getDescription());
                    cv.put(DataBase.TEMP, pJson.getTemp());
                    cv.put(DataBase.PRESSURE, pJson.getPressure());
                    cv.put(DataBase.WIND_SPEED, pJson.getSpeedWind());
                    cv.put(DataBase.ICON, pJson.getIconURL());
                    dBase.insert(TABLE_NAME, null, cv);
                }
            } else {
                for (int i = 0; i < SIZE_OF_LIST; i++) {
                    pJson.setCountInArray(i);
                    cv.put(DataBase.DATE_AND_TIME, pJson.getDateAndTime());
                    cv.put(DataBase.CITY, pJson.getCity());
                    cv.put(DataBase.COUNTRY, pJson.getCountry());
                    cv.put(DataBase.WEATHER, pJson.getDescription());
                    cv.put(DataBase.TEMP, pJson.getTemp());
                    cv.put(DataBase.PRESSURE, pJson.getPressure());
                    cv.put(DataBase.WIND_SPEED, pJson.getSpeedWind());
                    cv.put(DataBase.ICON, pJson.getIconURL());
                    dBase.update(TABLE_NAME, cv, "id = ?",
                            new String[]{String.valueOf(i + 1)});
                }

            }
        }
        cityColIndex = c.getColumnIndex(DataBase.CITY);
        dateAndTimeColIndex = c.getColumnIndex(DataBase.DATE_AND_TIME);
        countryColIndex = c.getColumnIndex(DataBase.COUNTRY);
        weatherColIndex = c.getColumnIndex(DataBase.WEATHER);
        tempColIndex = c.getColumnIndex(DataBase.TEMP);
        pressureColIndex = c.getColumnIndex(DataBase.PRESSURE);
        windSpeedColIndex = c.getColumnIndex(DataBase.WIND_SPEED);
        icoColIndex = c.getColumnIndex(DataBase.ICON);
        showDB(c);
        c.close();
        dBase.close();
    }

    public String getStringValues(int val) {
        String returnValues = "";
        String[] selectArg = new String[]{String.valueOf(1)};
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();
        Cursor c = dBase.query(TABLE_NAME, null, "id = ?", selectArg, null, null, null);
        c.moveToFirst();
        switch (val) {
            case 1:
                returnValues = c.getString(cityColIndex);
                break;
            case 2:
                returnValues = c.getString(countryColIndex);
                break;
            case 3:
                returnValues = c.getString(weatherColIndex);
                break;
            case 4:
                returnValues = c.getString(icoColIndex);
                break;
        }
        return returnValues;
    }

    public double getDoubleValues(int val) {
        double returnValues = 0;
        String[] selectArg = new String[]{String.valueOf(1)};
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();
        Cursor c = dBase.query(TABLE_NAME, null, "id = ?", selectArg, null, null, null);
        c.moveToFirst();
        switch (val) {
            case 1:
                returnValues = c.getDouble(tempColIndex);
                break;
            case 2:
                returnValues = c.getDouble(pressureColIndex);
                break;
            case 3:
                returnValues = c.getDouble(windSpeedColIndex);
                break;
        }
        return Math.round(returnValues);
    }

    public String[] getDates() {

        String[] dates = new String[SIZE_OF_LIST - 1];
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();
        String query = "SELECT " + DataBase.DATE_AND_TIME + " FROM " + TABLE_NAME;
        Cursor c = dBase.rawQuery(query, null);
        showDB(c);
        c.moveToFirst();
        c.moveToNext();
        int i = 0;
        while (i < dates.length) {
            dates[i] = c.getString(c.getColumnIndex(DataBase.DATE_AND_TIME));
            i++;
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return dates;
    }

    public String[] getTempForDates() {
        String[] tempForDates = new String[SIZE_OF_LIST - 1];
        double temp = 0;
        String[] columns = new String[]{DataBase.TEMP};
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();

        Cursor c = dBase.query(TABLE_NAME, columns, null, null, null, null, null);
        showDB(c);
        c.moveToFirst();
        c.moveToNext();
        for (int i = 0; i < SIZE_OF_LIST - 1; i++) {
            temp = c.getDouble(c.getColumnIndex(DataBase.TEMP));
            tempForDates[i] = Math.round(temp) + "";
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return tempForDates;
    }

    private void showDB(Cursor c) {
        if (c.moveToFirst()) {
            String str;
            do {
                str = "";
                for (String cn : c.getColumnNames()) {
                    str = str.concat(cn + " = "
                            + c.getString(c.getColumnIndex(cn)) + "; ");
                }
                Log.d(LOG_TAG, str);

            } while (c.moveToNext());
        }
    }

    public int[] getIcons() {
        int[] icoForDates = new int[SIZE_OF_LIST - 1];
        String icon = "";
        String[] columns = new String[]{DataBase.ICON};
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();

        Cursor c = dBase.query(TABLE_NAME, columns, null, null, null, null, null);
        showDB(c);
        c.moveToFirst();
        c.moveToNext();
        for (int i = 0; i < SIZE_OF_LIST - 1; i++) {
            icon = c.getString(c.getColumnIndex(DataBase.ICON));
            icoForDates[i] = getIcon(icon);
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return icoForDates;
    }

    private int getIcon(String icon) {
        if (icon.equals("01d"))
            return R.drawable._01d;
        else if (icon.equals("01n"))
            return R.drawable._01n;
        else if (icon.equals("02d"))
            return R.drawable._02d;
        else if (icon.equals("02n"))
            return R.drawable._02n;
        else if (icon.equals("03d"))
            return R.drawable._03d;
        else if (icon.equals("03n"))
            return R.drawable._03n;
        else if (icon.equals("04d"))
            return R.drawable._04d;
        else if (icon.equals("04n"))
            return R.drawable._04n;
        else if (icon.equals("09d"))
            return R.drawable._09d;
        else if (icon.equals("09n"))
            return R.drawable._09n;
        else if (icon.equals("10d"))
            return R.drawable._10d;
        else if (icon.equals("10n"))
            return R.drawable._10n;
        else if (icon.equals("11d"))
            return R.drawable._11d;
        else if (icon.equals("11n"))
            return R.drawable._11n;
        else if (icon.equals("13d"))
            return R.drawable._13d;
        else if (icon.equals("13n"))
            return R.drawable._13n;
        else if (icon.equals("50d"))
            return R.drawable._50d;
        else return R.drawable._50n;
    }

    static public double getPressureForPeriod(int pos) {
        String[] columns = new String[]{DataBase.PRESSURE};
        double[] pressureForDate = new double[SIZE_OF_LIST - 1];
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();

        Cursor c = dBase.query(TABLE_NAME, columns, null, null, null, null, null);
        c.moveToFirst();
        c.moveToNext();
        for (int i = 0; i < SIZE_OF_LIST - 1; i++) {
            pressureForDate[i] = c.getDouble(c.getColumnIndex(DataBase.PRESSURE));
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return pressureForDate[pos];
    }

    static public double getWindSpeed(int pos) {
        String[] columns = new String[]{DataBase.WIND_SPEED};
        double[] windSpeedForDate = new double[SIZE_OF_LIST - 1];
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();

        Cursor c = dBase.query(TABLE_NAME, columns, null, null, null, null, null);
        c.moveToFirst();
        c.moveToNext();
        for (int i = 0; i < SIZE_OF_LIST - 1; i++) {
            windSpeedForDate[i] = Math.round(c.getDouble(c.getColumnIndex(DataBase.WIND_SPEED)));
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return  windSpeedForDate[pos];
    }

    static public double getTemp(int pos) {
        String[] columns = new String[]{DataBase.TEMP};
        double[] temp = new double[SIZE_OF_LIST - 1];
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();

        Cursor c = dBase.query(TABLE_NAME, columns, null, null, null, null, null);
        c.moveToFirst();
        c.moveToNext();
        for (int i = 0; i < SIZE_OF_LIST - 1; i++) {
            temp[i] = Math.round(c.getDouble(c.getColumnIndex(DataBase.TEMP)));
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return  temp[pos];
    }

    static public String getIconForItem(int pos) {
        String[] columns = new String[]{DataBase.ICON};
        String[] iconForItem= new String[SIZE_OF_LIST - 1];
        DataBase dataBase = new DataBase(cont);
        dBase = dataBase.getWritableDatabase();

        Cursor c = dBase.query(TABLE_NAME, columns, null, null, null, null, null);
        c.moveToFirst();
        c.moveToNext();
        for (int i = 0; i < SIZE_OF_LIST - 1; i++) {
            iconForItem[i] = (c.getString(c.getColumnIndex(DataBase.ICON)));
            c.moveToNext();
        }

        c.close();
        dBase.close();
        return  iconForItem[pos];
    }
}
