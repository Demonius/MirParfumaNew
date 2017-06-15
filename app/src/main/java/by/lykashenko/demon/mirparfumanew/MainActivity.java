package by.lykashenko.demon.mirparfumanew;


import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.AboutDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.InfoDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Favorite;
import by.lykashenko.demon.mirparfumanew.Fragments.Home;
import by.lykashenko.demon.mirparfumanew.Fragments.Search;
import by.lykashenko.demon.mirparfumanew.Fragments.Trash;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountArray;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.StartBrenduHome, Search.LoadBrendInfo, Favorite.GoToCatalog {

    public static final String LOG_TAG = "MirParfuma";
    public static final String URL = "http://s6458.h6.modhost.pro/";
    public static final Integer BREND_OK = 100;
    private String[] mGroupsArrayCall = new String[]{"+375 29 157-57-05"};
    private String[] mCallOther = new String[]{"+375 29 864-35-73", "+375 25 938-71-09"};
    private ExpandableListView expandablePhoneNumber;
    private TabLayout tabLayout;
    private CountArray countArray;
    private TextView otzuvu, about, cashe;
    private ViewPager viewPager;
    private ViewStub viewStubImageToolbar, viewStubTextToolbar;
    private ViewPagerAdapter adapter;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("Parfum.db").setModelClasses(Favorites.class, by.lykashenko.demon.mirparfumanew.Table.Trash.class).create();
        ActiveAndroid.initialize(dbConfiguration);
        countArray = new CountArray(this);
        countArray.registerCallBackCount(new CountArray.OnCallBackCount() {
            @Override
            public void onCallBackCount(String count, Integer state) {
                if (state == 4) {
                    String text = getResources().getString(R.string.otzuvu) + " (" + count + ")";
                    otzuvu.setText(text);
                }
            }
        });

        countArray.Count(Home.sql_count_otzuvu, 4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewStubImageToolbar = (ViewStub) toolbar.findViewById(R.id.viewStubImageToolbar);
        viewStubImageToolbar.inflate();

        viewStubTextToolbar = (ViewStub) toolbar.findViewById(R.id.viewStubTextToolbar);
        viewStubTextToolbar.inflate();


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabsIcon();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this, "экран => " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        ArrayList<Map<String, String>> listDataHeader = new ArrayList<Map<String, String>>();
        ArrayList<ArrayList<Map<String, String>>> listDataChild = new ArrayList<ArrayList<Map<String, String>>>();

        //заголовки
        Map<String, String> m = new HashMap<String, String>();
        m.put("groupName", mGroupsArrayCall[0]);
        listDataHeader.add(m);

        // список атрибутов групп для чтения
        String groupFrom[] = new String[]{"groupName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[]{R.id.textViewGroupList};

        //список
        ArrayList<Map<String, String>> number = new ArrayList<Map<String, String>>();
        Map<String, String> mChild = new HashMap<String, String>();

        mChild.put("phoneNumber", mCallOther[0]);
        number.add(mChild);
        mChild.put("phoneNumber", mCallOther[1]);
        number.add(mChild);
        listDataChild.add(number);

        // список атрибутов элементов для чтения
        String childFrom[] = new String[]{"phoneNumber"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int childTo[] = new int[]{R.id.textViewChildGroup};


        SimpleExpandableListAdapter adapterExpandable = new SimpleExpandableListAdapter(this,
                listDataHeader,
                R.layout.group_name,
                groupFrom,
                groupTo,
                listDataChild,
                R.layout.child_group_name,
                childFrom,
                childTo);
        expandablePhoneNumber = (ExpandableListView) navigationView.findViewById(R.id.expandablePhoneNumber);
        expandablePhoneNumber.setAdapter(adapterExpandable);

        navigationView.setNavigationItemSelectedListener(this);

        otzuvu = (TextView) navigationView.findViewById(R.id.textViewOtzuvu);
        about = (TextView) navigationView.findViewById(R.id.textViewAbout);
        cashe = (TextView) navigationView.findViewById(R.id.textViewCashe);
        TextView info = (TextView) navigationView.findViewById(R.id.textViewInfo);

        //onClick
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDialog();
            }
        });

        cashe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDialog();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialogFragment info = new InfoDialogFragment();
                info.show(getSupportFragmentManager(), "dlg2");
            }
        });


    }

    private void AboutDialog() {

        AboutDialogFragment aboutDialog = new AboutDialogFragment();
        aboutDialog.show(getSupportFragmentManager(), "dlg1");
    }

    private void setupTabsIcon() {

        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_home));
        ((TextView) tabOne.findViewById(R.id.text_tab)).setText(R.string.home);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_search));
        ((TextView) tabTwo.findViewById(R.id.text_tab)).setText(R.string.search);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        ((TextView) tabThree.findViewById(R.id.text_tab)).setText(R.string.favorites);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        LinearLayout tabFour = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_trash));
        ((TextView) tabFour.findViewById(R.id.text_tab)).setText(R.string.trash);
        tabLayout.getTabAt(3).setCustomView(tabFour);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Home fragmentHome = new Home();
        fragmentHome.registerHomeBrendu(this);
        adapter.addFragment(fragmentHome, getResources().getString(R.string.home));
        Search fragmentSearch = new Search();
        fragmentSearch.registerLoadBrendInfo(this);
        adapter.addFragment(fragmentSearch, getResources().getString(R.string.search));
        Favorite fragmentFavorite = new Favorite();
        fragmentFavorite.registerGoToCatalog(this);
        adapter.addFragment(fragmentFavorite, getResources().getString(R.string.favorites));
        adapter.addFragment(new Trash(), getResources().getString(R.string.trash));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

            case android.R.id.home:

                drawer.openDrawer(GravityCompat.START);

                return true;
            case R.id.action_client:
                Toast.makeText(this, "Пока вы не можете подключиться к личному кабинету", Toast.LENGTH_SHORT).show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CallBrendActivity(Bundle bundle) {


        Intent intent = new Intent(this, BrendActivity.class);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, BREND_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Integer id = data.getIntExtra("idSwitch", 0);
            viewPager.setCurrentItem(id);
        } else return;
    }

    @Override
    public void onStartBrenduHome(Bundle bundle) {
        CallBrendActivity(bundle);
    }

    @Override
    public void onClickParfumCategory(Integer i) {
        switch (i) {
            case 1:
//viewPager.setCurrentItem(1);

                break;
        }
    }

    @Override
    public void onLoadBrendInfo(Bundle bundle) {
        CallBrendActivity(bundle);
    }

    @Override
    public void onGoToCatalog(Integer state, Bundle bundle) {
        switch (state) {
            case 1:
                viewPager.setCurrentItem(1, true);
                break;
            case 2:
                onStartBrenduHome(bundle);
                break;


        }
    }
}
