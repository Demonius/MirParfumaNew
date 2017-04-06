package by.lykashenko.demon.mirparfumanew;


import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.lykashenko.demon.mirparfumanew.Adapters.ViewPagerAdapter;
import by.lykashenko.demon.mirparfumanew.Fragments.Favorite;
import by.lykashenko.demon.mirparfumanew.Fragments.Home;
import by.lykashenko.demon.mirparfumanew.Fragments.Search;
import by.lykashenko.demon.mirparfumanew.Fragments.Trash;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountArray;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG ="MirParfuma";
    public static final String URL = "http://s6458.h6.modhost.pro/";
    private String[] mGroupsArrayCall = new String[] {"+375 29 157-57-05"};
    private String[] mCallOther = new String[]{"+375 29 864-35-73", "+375 25 938-71-09"};
    private ExpandableListView expandablePhoneNumber;
    private TabLayout tabLayout;
    private CountArray countArray;
    private TextView otzuvu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countArray = new CountArray(this);
        countArray.registerCallBackCount(new CountArray.OnCallBackCount() {
            @Override
            public void onCallBackCount(Integer count, Integer state) {
                if (state == 4){
                    String text = getResources().getString(R.string.otzuvu)+" ("+count+")";
                    otzuvu.setText(text);
                }
            }
        });

        countArray.Count(Home.sql_count_otzuvu,4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabsIcon();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(LOG_TAG,"экран № "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ArrayList<Map<String,String>> listDataHeader = new ArrayList<Map<String,String>>();
        ArrayList<ArrayList<Map<String, String>>> listDataChild = new ArrayList<ArrayList<Map<String, String>>>();

        //заголовки
        Map<String,String> m = new HashMap<String,String>();
        m.put("groupName",mGroupsArrayCall[0]);
        listDataHeader.add(m);

        // список атрибутов групп для чтения
        String groupFrom[] = new String[] {"groupName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[] {R.id.textViewGroupList};

        //список
        ArrayList<Map<String, String>> number = new ArrayList<Map<String, String>>();
        Map<String,String> mChild = new HashMap<String,String>();

        mChild.put("phoneNumber",mCallOther[0]);
        number.add(mChild);
        mChild.put("phoneNumber",mCallOther[1]);
        number.add(mChild);
        listDataChild.add(number);

        // список атрибутов элементов для чтения
        String childFrom[] = new String[] {"phoneNumber"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int childTo[] = new int[] {R.id.textViewChildGroup};


        SimpleExpandableListAdapter adapterExpandable = new SimpleExpandableListAdapter(this,
                listDataHeader,
                R.layout.group_name,
                groupFrom,
                groupTo,
                listDataChild,
                R.layout.child_group_name,
                childFrom,
                childTo);
        expandablePhoneNumber = (ExpandableListView)  navigationView.findViewById(R.id.expandablePhoneNumber);
        expandablePhoneNumber.setAdapter(adapterExpandable);

        navigationView.setNavigationItemSelectedListener(this);

        otzuvu = (TextView) navigationView.findViewById(R.id.textViewOtzuvu);


    }

    private void setupTabsIcon() {

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(getResources().getString(R.string.home));
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);

            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabTwo.setText(getResources().getString(R.string.search));
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search, 0, 0);
            tabLayout.getTabAt(1).setCustomView(tabTwo);

            TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabThree.setText(getResources().getString(R.string.favorites));
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite, 0, 0);
            tabLayout.getTabAt(2).setCustomView(tabThree);

            TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabFour.setText(getResources().getString(R.string.trash));
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_trash, 0, 0);
            tabLayout.getTabAt(3).setCustomView(tabFour);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), getResources().getString(R.string.home));
        adapter.addFragment(new Search(), getResources().getString(R.string.search));
        adapter.addFragment(new Favorite(), getResources().getString(R.string.favorites));
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_client) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
