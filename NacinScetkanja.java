package si.uni_lj.fe.tnuv.oral_g;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NacinScetkanja extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nacin_scetkanja);
    }
    public void Alert1(View Alert1){
        Toast.makeText(this, "Izbrali ste način Vsakodnevno ščetkanje. Proces se bo začel čez 5 sekund!", Toast.LENGTH_LONG).show();
    }

    public void Alert2(View Alert2){
        Toast.makeText(this, "Izbrali ste način Oskrba dlesni. Proces se bo začel čez 5 sekund!", Toast.LENGTH_LONG).show();
    }

    public void Alert3(View Alert3){
        Toast.makeText(this, "Izbrali ste način Beljenje. Proces se bo začel čez 5 sekund!", Toast.LENGTH_LONG).show();
    }

    public void Alert4(View Alert4){
        Toast.makeText(this, "Izbrali ste način Globoko čiščenje. Proces se bo začel čez 5 sekund!", Toast.LENGTH_LONG).show();
    }
}