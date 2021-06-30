package si.uni_lj.fe.tnuv.oral_g;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "StatistikaPodatki.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Podatki(stevilkaScetkanja TEXT primary key, datumScetkanja TEXT, casZacetkaScetkanja TEXT, trajanjeScetkanja TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Podatki");
    }

    public Boolean vstavitevPodatkov(String stevilkaScetkanja, String datumScetkanja, String casZacetkaScetkanja, String trajanjeScetkanja) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues vrednostiPodatkov = new ContentValues();
        vrednostiPodatkov.put("stevilkaScetkanja", stevilkaScetkanja);
        vrednostiPodatkov.put("datumScetkanja", datumScetkanja);
        vrednostiPodatkov.put("casZacetkaScetkanja", casZacetkaScetkanja);
        vrednostiPodatkov.put("trajanjeScetkanja", trajanjeScetkanja);

        long rezultat = DB.insert("Podatki", null, vrednostiPodatkov);
        if(rezultat == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean izbrisiPodatke(String stevilkaScetkanja) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Podatki where stevilkaScetkanja = ?", new String[]{stevilkaScetkanja});
        if(cursor.getCount() > 0) {
            long result = DB.delete("Podatki", "stevilkaScetkanja = ?", new String[]{stevilkaScetkanja});
            if(result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public Cursor pridobiPodatke () {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Podatki", null);
        return cursor;
    }

    public long dobiSteviloVrstic() {
        SQLiteDatabase DB = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(DB, "Podatki");
        DB.close();
        return count;
    }
}
