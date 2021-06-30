package si.uni_lj.fe.tnuv.oral_g;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Scetkanje extends AppCompatActivity {

    TextView BLEpovezavaStatus;
    TextView casScetkanja;
    TextView scetkanjeNavodila;
    Chronometer casScetkanjaCas;
    Handler simuliranBLEDelay;

    int stevecScetkanj;
    String kljucStevcaScetkanj;
    int privzetaVrednostStevcaScetkanj;
    SharedPreferences dobiStevecScetkanj;

    long minute;
    String minuteString;
    long sekunde;
    String sekundeString;

    StringBuilder SB;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scetkanje);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.scetkanjeLabel));

    }

    @Override
    protected void onStart() {
        super.onStart();

        //SIMULACIJA BLE POVEZAVE IN PREJEMA PODATKOV
        simuliranBLEDelay = new Handler();
        BLEpovezavaStatus = findViewById(R.id.BLEpovezavaStatus);
        scetkanjeNavodila = findViewById(R.id.scetkanjeNavodila);
        casScetkanja = findViewById(R.id.casScetkanja);
        casScetkanjaCas = findViewById(R.id.casScetkanjaCas);
        DB = new DBHelper(this);

        //BLE "povezan" po delayu 4 sekund
        simuliranBLEDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                //final int casScetkanjaUporabnika = new Random().nextInt(281000) + 20000; //naključen čas ščetkanja uporabnika med 20s in 5min v milisekundah
                final int casScetkanjaUporabnika = new Random().nextInt(4001) + 1000; //naključen čas ščetkanja uporabnika med 1s in 5s v milisekundah
                //final int casScetkanjaUporabnika = 1000;  //za testiranje, nehaj šteti že po eni sekundi
                BLEpovezavaStatus.setText(R.string.BLEpovezan);
                scetkanjeNavodila.setVisibility(View.INVISIBLE);
                casScetkanja.setVisibility(View.VISIBLE);
                casScetkanjaCas.setVisibility(View.VISIBLE);

                //začetek štetja časa
                casScetkanjaCas.setBase(SystemClock.elapsedRealtime());
                casScetkanjaCas.start();
                //Simulacija detektiranja ščetkanja (prejemanje podatkov)
                casScetkanjaCas.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long pretekelCas = SystemClock.elapsedRealtime() - chronometer.getBase();
                        //ko je "detektiran konec ščetkanja" ustavi timer
                        if (pretekelCas >= casScetkanjaUporabnika) {
                            casScetkanjaCas.stop();
                            //povečaj in shrani števec
                            kljucStevcaScetkanj = getResources().getString(R.string.kljuc_stevca_scetkanj);
                            privzetaVrednostStevcaScetkanj = getResources().getInteger(R.integer.ni_scetkanj);

                            dobiStevecScetkanj = getSharedPreferences("hrambaStevcaScetkanj",Context.MODE_PRIVATE);
                            stevecScetkanj = dobiStevecScetkanj.getInt(kljucStevcaScetkanj, privzetaVrednostStevcaScetkanj);

                            stevecScetkanj++;

                            SharedPreferences.Editor editor = dobiStevecScetkanj.edit();
                            editor.putInt(kljucStevcaScetkanj, stevecScetkanj);
                            editor.apply();

                            //dobi čas ščetkanja
                            Date currentTime = Calendar.getInstance().getTime();
                            DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

                            //dobi trajanje ščetkanja iz preteklega časa
                            pretekelCas = pretekelCas/1000;
                            minute = Math.floorDiv(pretekelCas, 60);
                            sekunde = pretekelCas - minute * 60;

                            minuteString = String.valueOf(minute);
                            sekundeString = String.valueOf(sekunde);

                            SB = new StringBuilder();
                            if(minute < 10) {
                                SB.append("0");
                            }
                            SB.append(minuteString);
                            SB.append(":");
                            if(sekunde < 10) {
                                SB.append("0");
                            }
                            SB.append(sekundeString);

                            String stevecScetkanjString = String.valueOf(stevecScetkanj);
                            String datumScetkanjaString = new SimpleDateFormat("dd. MM. yyyy", Locale.getDefault()).format(new Date());
                            String casZacetkaScetkanjaString = dateFormat. format(currentTime);
                            String trajanjeScetkanjaString = SB.toString();
                            DB.vstavitevPodatkov(stevecScetkanjString, datumScetkanjaString, casZacetkaScetkanjaString, trajanjeScetkanjaString);

                            Toast.makeText(Scetkanje.this, getResources().getString(R.string.podatki_shranjeni_toast_text), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, 4000);
    }
}