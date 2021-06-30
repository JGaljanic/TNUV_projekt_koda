package si.uni_lj.fe.tnuv.oral_g;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class Nastavitve extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // spremenljivke za izbris podatkov
    Boolean izbrisPodatkov;
    long count;
    String vrstica;

    // spremenljivke za reset števca ščetkanj
    int stevecScetkanj;
    String kljucStevcaScetkanj;
    int privzetaVrednostStevcaScetkanj;
    SharedPreferences dobiStevecScetkanj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();   // naloži locale (jezik)
        setContentView(R.layout.activity_nastavitve);

        // jezik naslova zavihka
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.nastavitveLabel));

        // naredi seznam nastavitev
        ListView listView = (ListView) findViewById(R.id.seznamNastavitev);
        ArrayList<String> arrayList = new ArrayList<>();

        // dodaj nastavitve
        arrayList.add(getResources().getString(R.string.jezikNastavitev));
        arrayList.add(getResources().getString(R.string.izbrisPodatkovNastavitev));

        // array adapter in click listener
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String izbranaNastavitev = parent.getItemAtPosition(position).toString();

        // če klikneš sprememba jezika se odpre dialogno okno
        if (izbranaNastavitev.equals(getResources().getString(R.string.jezikNastavitev))) {
            spremembaJezika();
        }
        // če klikneš izbris podatkov se izbrišejo podatki podatkovne baze in števec ščetkanj se postavi na 0
        else if (izbranaNastavitev.equals(getResources().getString(R.string.izbrisPodatkovNastavitev))) {

            DBHelper DB = new DBHelper(this);
            //dialogno okno
            AlertDialog.Builder izbrisBuilder = new AlertDialog.Builder(Nastavitve.this);
            izbrisBuilder.setMessage(getResources().getString(R.string.izbris_podatkov_dialog_text)).setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.izbris_da), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //izbris podatkov baze
                        count = DB.dobiSteviloVrstic();

                        for (int i = 1; i <= count; i++) {
                            vrstica = String.valueOf(i);
                            izbrisPodatkov = DB.izbrisiPodatke(vrstica);
                        }
                        //reset števca ščetkanj
                        kljucStevcaScetkanj = getResources().getString(R.string.kljuc_stevca_scetkanj);
                        privzetaVrednostStevcaScetkanj = getResources().getInteger(R.integer.ni_scetkanj);

                        dobiStevecScetkanj = getSharedPreferences("hrambaStevcaScetkanj",Context.MODE_PRIVATE);
                        stevecScetkanj = dobiStevecScetkanj.getInt(kljucStevcaScetkanj, privzetaVrednostStevcaScetkanj);

                        stevecScetkanj = 0;

                        SharedPreferences.Editor editor = dobiStevecScetkanj.edit();
                        editor.putInt(kljucStevcaScetkanj, stevecScetkanj);
                        editor.apply();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.izbris_ne), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            AlertDialog alert = izbrisBuilder.create();
            alert.setTitle("Dialog Header");
            alert.show();
        }
    }

    private void spremembaJezika() {
        final String[] listItems = {"Slovenščina", "English"};  // seznam jezikov
        // dialogno okno
        AlertDialog.Builder jezikBuilder = new AlertDialog.Builder(Nastavitve.this);
        jezikBuilder.setTitle(getResources().getString(R.string.izberiteJezik));
        jezikBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            // če klikneš prvo opcijo se jezik postavi v slovenščino, če klikneš drugo opcijo pa v angleščino, resetiraj aktivnost
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("sl");
                    recreate();
                }
                else if (i == 1) {
                    setLocale("en");
                    recreate();
                }
                //ko je opcija izbrana zapri okno
                dialogInterface.dismiss();
            }
        });

        AlertDialog jezikDialog = jezikBuilder.create();
        jezikDialog.show();
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