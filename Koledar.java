package si.uni_lj.fe.tnuv.oral_g;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class Koledar extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {


    Button buttonZobar;
    Button buttonShraniStevilko;
    CalendarView Koledar;
    String stevilka;
    EditText stevilkaZobarja;
    SharedPreferences sharedPrefs;
    String kljucStevilke;
    String privzetaVrednostStevilke;
    TextView datumPregleda;
    String datumPregledaDatum;
    String kljucDatumaPregleda;
    String privzetaVrednostDatumaPregleda;
    String datumPregledaText;
    TextView stevilkaZobarjaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_koledar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.zobozdravnikLabel));

        //start listenerjev gumbov in koledarja
        buttonShraniStevilko = findViewById(R.id.gumbShraniStevilko);
        buttonShraniStevilko.setOnClickListener(this);

        buttonZobar = findViewById(R.id.gumbZobar);
        buttonZobar.setOnClickListener(this);

        Koledar = (CalendarView) findViewById(R.id.calendarView);
        Koledar.setOnDateChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.gumbShraniStevilko:
                //shrani vstavljeno številko v string
                stevilkaZobarja = findViewById(R.id.stevilkaZobarja);
                stevilka = stevilkaZobarja.getText().toString();

                kljucStevilke = getResources().getString(R.string.kljuc_stevilke);

                sharedPrefs = getPreferences(Context.MODE_PRIVATE);
                //shrani številko zobozdravnika
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(kljucStevilke, stevilka);
                editor.apply();

                stevilkaZobarjaText = findViewById(R.id.stevilkaZobarjaText);
                stevilkaZobarjaText.setText(getResources().getString(R.string.shranjena_stevilka) + stevilka);
                break;

            case R.id.gumbZobar:

                //dobi shranjeno številko, če je še ni vrni presledek
                kljucStevilke = getResources().getString(R.string.kljuc_stevilke);
                privzetaVrednostStevilke = getResources().getString(R.string.no_number);

                sharedPrefs = getPreferences(Context.MODE_PRIVATE);
                stevilka = sharedPrefs.getString(kljucStevilke, privzetaVrednostStevilke);
                //uporabi številko za odpiranje klicalnika
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + stevilka));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        //spremeni datum pregleda
        month++;
        datumPregleda = findViewById(R.id.datumPregleda);
        datumPregledaDatum = dayOfMonth + ". " + month + ". " + year;
        datumPregleda.setText(getResources().getString(R.string.pregledText) + datumPregledaDatum);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Če datum pregleda ob zagonu aktivnosti že obstaja ga prikaži
        kljucDatumaPregleda = getResources().getString(R.string.kljuc_datuma_pregleda);
        privzetaVrednostDatumaPregleda = getResources().getString(R.string.no_date);

        sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        datumPregledaDatum = sharedPrefs.getString(kljucDatumaPregleda, privzetaVrednostDatumaPregleda);

        datumPregleda = findViewById(R.id.datumPregleda);
        datumPregleda.setText(getResources().getString(R.string.pregledText) + datumPregledaDatum);

        //Če številka zobozdravnika ob zagonu aktivnosti že obstaja jo prikaži
        kljucStevilke = getResources().getString(R.string.kljuc_stevilke);
        privzetaVrednostStevilke = getResources().getString(R.string.no_number);

        sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        stevilka = sharedPrefs.getString(kljucStevilke, privzetaVrednostStevilke);

        if (!stevilka.equals(privzetaVrednostStevilke)) {
            stevilkaZobarjaText = findViewById(R.id.stevilkaZobarjaText);
            stevilkaZobarjaText.setText(getResources().getString(R.string.shranjena_stevilka) + stevilka);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //ob izklopu aktivnosti shrani datum

        kljucDatumaPregleda = getResources().getString(R.string.kljuc_datuma_pregleda);

        sharedPrefs = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(kljucDatumaPregleda, datumPregledaDatum);
        editor.apply();
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