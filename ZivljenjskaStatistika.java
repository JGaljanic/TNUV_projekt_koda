package si.uni_lj.fe.tnuv.oral_g;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class ZivljenjskaStatistika extends AppCompatActivity {

    ListView zgodovinaScetkanja;
    StringBuilder SB;
    Cursor dobiPodatke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_zivljenjska_statistika);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.statistikaLabel));

        zgodovinaScetkanja = findViewById(R.id.zgodovinaScetkanja);

        ArrayList<String> arrayList = new ArrayList<>();

        DBHelper DB = new DBHelper(this);
        dobiPodatke = DB.pridobiPodatke();

        SB = new StringBuilder();

        while(dobiPodatke.moveToNext()) {
            SB.append(getResources().getString(R.string.datum_scetkanja) + dobiPodatke.getString(1) +"\n");
            SB.append(getResources().getString(R.string.ura_scetkanja) + dobiPodatke.getString(2) +"\n");
            SB.append(getResources().getString(R.string.trajanje_scetkanja) + dobiPodatke.getString(3) +"\n");

            String zgodovinaScetkanjaVrstica = SB.toString();

            arrayList.add(0, zgodovinaScetkanjaVrstica);
            SB.delete(0,SB.length());
        }

        if(arrayList.isEmpty()){
            arrayList.add(0, "Niste si še ščetkali zob!");
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        zgodovinaScetkanja.setAdapter(arrayAdapter);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Nastavitve", MODE_PRIVATE).edit();
        editor.putString("jezik", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Nastavitve", Activity.MODE_PRIVATE);
        String language = prefs.getString("jezik","");
        setLocale(language);
    }
}