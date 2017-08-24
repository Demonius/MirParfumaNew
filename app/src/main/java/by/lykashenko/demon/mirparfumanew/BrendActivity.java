package by.lykashenko.demon.mirparfumanew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;

import by.lykashenko.demon.mirparfumanew.Fragments.FragmentOtzuvu;
import by.lykashenko.demon.mirparfumanew.Fragments.FragmentParfum;
import by.lykashenko.demon.mirparfumanew.Fragments.FragmentParfumList;
import by.lykashenko.demon.mirparfumanew.Table.ListParfum;

import static by.lykashenko.demon.mirparfumanew.MainActivity.BREND_OK;
import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

public class BrendActivity extends AppCompatActivity implements View.OnClickListener,
        FragmentParfumList.GetInfoOfParfum,
        FragmentParfum.LoadListOtzuvu{

    private ProgressDialog progressDialog;
    public static TextView titleToolbar;

    @Override

    protected  void onDestroy(){
        super.onDestroy();

        ActiveAndroid.beginTransaction();
        new Delete().from(ListParfum.class).execute();

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBrendActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        titleToolbar = (TextView) findViewById(R.id.textToolbarBrendActivity);
        Intent intent = getIntent();

//        Integer stateFragment = intent.getIntExtra("state", 0);
        Bundle bundle = intent.getBundleExtra("bundle");
        Integer stateFragment = bundle.getInt("state");


        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

            switch (stateFragment) {
                case 0:
                    FragmentParfumList fragment2 = new FragmentParfumList();
                    fragment2.registrationGetInfoOfParfum(this);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("podbor",1);
                    fragment2.setArguments(bundle2);
                    fTrans.add(R.id.frameBrendActivity, fragment2);
                    break;
                case 1:
                    FragmentParfumList fragment1 = new FragmentParfumList();
                    fragment1.registrationGetInfoOfParfum(this);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", bundle.getString("id"));
                    bundle1.putString("name", bundle.getString("name"));
                    bundle1.putInt("sex", bundle.getInt("sex"));
                    fragment1.setArguments(bundle1);
                    fTrans.add(R.id.frameBrendActivity, fragment1);
                    break;
                case 2:
                    FragmentParfum fragmentParfum = new FragmentParfum();
                    fragmentParfum.registerLoadOtzuvu(this);
                    fragmentParfum.setArguments(bundle);
                    fTrans.add(R.id.frameBrendActivity, fragmentParfum);
                    break;

            }
            fTrans.commit();
        ImageView imageBack = (ImageView) findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Integer countFragment = getSupportFragmentManager().getBackStackEntryCount();
                Log.d(LOG_TAG,"count Fragment => "+countFragment);
                if (countFragment==0){
                    finish();
                }else{
                    getSupportFragmentManager().popBackStack();
                }
            }
        });


//toolbar для быстрого перехода между экранами главной activity
        LinearLayout home = (LinearLayout) findViewById(R.id.HomeParfum);
        LinearLayout search = (LinearLayout) findViewById(R.id.SearchParfum);
        LinearLayout favorites = (LinearLayout) findViewById(R.id.FavoritesParfum);
        LinearLayout trash = (LinearLayout) findViewById(R.id.TrashParfum);

        home.setOnClickListener(this);
        search.setOnClickListener(this);
        favorites.setOnClickListener(this);
        trash.setOnClickListener(this);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getResources().getString(R.string.loadMessage));
//        progressDialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HomeParfum:
                ChangePage(0);
                break;
            case R.id.SearchParfum:
                ChangePage(1);
                break;
            case R.id.FavoritesParfum:
                ChangePage(2);
                break;
            case R.id.TrashParfum:
                ChangePage(3);
                break;
        }
    }

    public void ChangePage(Integer i) {
        Intent intent = new Intent();
        intent.putExtra("idSwitch", i);
        setResult(BREND_OK, intent);
        finish();
    }

    @Override
    public void onGetInfoOfParfum(Bundle bundle) {
        FragmentParfum fragmentParfum = new FragmentParfum();
        fragmentParfum.registerLoadOtzuvu(this);
        fragmentParfum.setArguments(bundle);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frameBrendActivity, fragmentParfum);
        fTrans.addToBackStack("parfum");
        fTrans.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_client:
                Toast.makeText(this, "Пока вы не можете подключиться к личному кабинету", Toast.LENGTH_SHORT).show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadListOtzuvu(String id, String count) {
        FragmentOtzuvu fragmentOtzuvu = new FragmentOtzuvu();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("count", count);
        fragmentOtzuvu.setArguments(bundle);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.addToBackStack("otzuvu");
        fTrans.replace(R.id.frameBrendActivity, fragmentOtzuvu);
        fTrans.commit();
    }
//    public void onSaveInstanceState(Bundle outState){
//        getSupportFragmentManager().putFragment(outState,"fragment1",fragment);
//    }
//    public void onRestoreInstanceState(Bundle inState){
//        fragment = getSupportFragmentManager().getFragment(inState,"fragment1");
//    }
}
