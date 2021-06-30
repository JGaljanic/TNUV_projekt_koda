package si.uni_lj.fe.tnuv.oral_g;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.glavniMeniLabel));
    }

    public void odpriNacin(View Scetkanje){    //odpri Ščetkanje, funkcija aktivirana v XML

        Intent n = new Intent(this, Scetkanje.class);
        startActivity(n);

    }

    public void odpriNastavitve(View Nastavitve){   //odpri Nastavitve, funkcija aktivirana v XML

        Intent o = new Intent(this, Nastavitve.class);
        startActivity(o);

    }

    public void odpriKoledar(View Koledar){     //odpri Zobozdravnika, funkcija aktivirana v XML

        Intent k = new Intent(this, Koledar.class);
        startActivity(k);

    }

    public void odpriStatistika(View Statistika){     //odpri Statistiko, funkcija aktivirana v XML

        Intent s = new Intent(this, ZivljenjskaStatistika.class);
        startActivity(s);

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