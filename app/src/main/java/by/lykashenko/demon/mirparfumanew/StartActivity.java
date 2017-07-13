package by.lykashenko.demon.mirparfumanew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.BrendList;
import by.lykashenko.demon.mirparfumanew.Table.BrenduAll;
import by.lykashenko.demon.mirparfumanew.Table.BrenduCount;
import by.lykashenko.demon.mirparfumanew.Table.Country;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;
import by.lykashenko.demon.mirparfumanew.Table.ForTable;
import by.lykashenko.demon.mirparfumanew.Table.ListParfum;
import by.lykashenko.demon.mirparfumanew.Table.Nota;
import by.lykashenko.demon.mirparfumanew.Table.Semeistvo;
import by.lykashenko.demon.mirparfumanew.Table.Type;
import by.lykashenko.demon.mirparfumanew.Table.Year;


public class StartActivity extends AppCompatActivity implements BrendList.OnLoadBrendList {
//Select parfums.id, parfums.pagetitle, val.contentid, val.value from (Select brend.id, brend.pagetitle,parfum.contentid FROM (Select id,pagetitle FROM modx_site_content where parent=854)as brend, modx_site_tmplvar_contentvalues as parfum where (parfum.tmplvarid = 65 or parfum.tmplvarid = 11) and brend.pagetitle LIKE parfum.value) as parfums,modx_site_tmplvar_contentvalues as val where val.contentid=parfums.contentid and val.tmplvarid=67 and val.value="женский" ORDER BY parfums.pagetitle
//contentid=65 and id_load =1
//    private String sql_load_brendu = "SELECT brendu.id,brendu.pagetitle, count(val.contentid)" +
//            " FROM modx_site_tmplvar_contentvalues as val," +
//            "(SELECT id,pagetitle FROM modx_site_content WHERE parent = 854)as brendu" +
//            " WHERE (val.tmplvarid = 65 or val.tmplvarid = 11) and brendu.pagetitle LIKE val.value " +
//            "GROUP BY brendu.pagetitle " +
//            "ORDER BY brendu.pagetitle ASC";
    private String sql_load_brendu = "Select brendu.id as brend_id, brendu.pagetitle as brend_name, parfum.id as parfum_id, parfum.value as sex, parfum.pagetitle as value FROM (Select id,pagetitle,parfum.value as value FROM (SELECT contentid, value FROM `modx_site_tmplvar_contentvalues` where tmplvarid=67) as parfum, modx_site_content as brend WHERE brend.id=parfum.contentid) as parfum, (Select id,pagetitle FROM modx_site_content where parent=854)as brendu where parfum.pagetitle like CONCAT('%',REPLACE(REPLACE(brendu.pagetitle,\" and \",\"%\"),\" \",\"%\"),'%') ORDER BY `brendu`.`pagetitle` ASC";

    //contentid=66 and id_load =2
    private String sql_load_type = "SELECT value " +
            "FROM `modx_site_tmplvar_contentvalues` " +
            "where tmplvarid = 66 " +
            "GROUP BY value " +
            "ORDER BY `value` ASC ";

    //contentid=68 and id_load =3
    private String sql_load_year = "SELECT value " +
            "FROM modx_site_tmplvar_contentvalues " +
            "WHERE tmplvarid = 68 and value<>\"\" " +
            "GROUP BY value " +
            "ORDER BY value ASC ";

    //contentid=69 and id_load =4
    private String sql_load_aromat = "";

    //contentid=70 and id_load =5
    private String sql_load_nota = "";

    //contentid=73 and id_load =6
    private String sql_load_country = "SELECT value as country " +
            "FROM `modx_site_tmplvar_contentvalues` " +
            "where tmplvarid = 73 " +
            "GROUP BY value " +
            "ORDER BY `value` ASC ";

    private String sql_load_for = "SELECT value " +
            "FROM modx_site_tmplvar_contentvalues " +
            "where tmplvarid = 67 " +
            "group by value";

    public static final String name_receiver = "load.receiver";
    private BroadcastReceiver receiver;
    private Integer count=0;
private TextView textLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Integer i = intent.getIntExtra("count", 0);
                count=count+i;
                if (count==5){
                    StartMainActivity();
                }
               // Log.d(LOG_TAG, "Message received: " + intent.getIntExtra("count",0) );
               // Log.d(LOG_TAG, "count received: " + count );
            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("Parfum.db").
                setModelClasses(Favorites.class,
                        by.lykashenko.demon.mirparfumanew.Table.Trash.class,
                        BrenduCount.class,
                        Type.class,
                        Year.class,
                        Country.class,
                        ListParfum.class,
                        BrenduAll.class,
                        ForTable.class,
                        Nota.class,
                        Semeistvo.class).create();
        ActiveAndroid.initialize(dbConfiguration);
        textLoad = (TextView) findViewById(R.id.textLoad);
        SharedPreferences sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        Integer load = sharedPreferences.getInt("state",0);
        if (load == 0){
        textLoad.setText(R.string.loasStart);
        //load static data
        BrendList brendList = new BrendList(this);
        brendList.registerOnLoadBrendList(this);
        brendList.load(sql_load_brendu,1);
        brendList.load(sql_load_type,2);
        brendList.load(sql_load_year,3);
//        brendList.load(sql_load_aromat,4);
//        brendList.load(sql_load_nota,5);
        brendList.load(sql_load_country,6);
            brendList.load(sql_load_for,8);
        }
        else{
            textLoad.setText(R.string.loadMain);
            BrendList brendList = new BrendList(this);
            brendList.load(sql_load_country,6);
            StartMainActivity();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver( receiver, new IntentFilter( name_receiver ) );
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void StartMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoadBrendList(ArrayList<Brendu> brendu) {

    }

    @Override
    public void onProgress(Integer i, Integer count) {
        String text = getResources().getString(R.string.load)+" "+Integer.toString(i)+" "+getResources().getString(R.string.iz)+" "+Integer.toString(count)+" "+getResources().getString(R.string.brends);
        textLoad.setText(text);
    }
}
