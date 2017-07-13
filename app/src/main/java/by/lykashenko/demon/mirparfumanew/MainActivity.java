package by.lykashenko.demon.mirparfumanew;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.mancj.slideup.SlideUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.AboutDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseBrendu;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseFor;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseType;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.InfoDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Favorite;
import by.lykashenko.demon.mirparfumanew.Fragments.Home;
import by.lykashenko.demon.mirparfumanew.Fragments.Search;
import by.lykashenko.demon.mirparfumanew.Fragments.Trash;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountArray;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Home.StartBrenduHome,
        Search.LoadBrendInfo,
        Favorite.GoToCatalog,
        DialogChoiseBrendu.CloseChoiseBrendu,
        DialogChoiseType.CloseChoiseType, Trash.GoToCatalog, DialogChoiseFor.CloseChoiseFor {

    public static final String LOG_TAG = "MirParfuma";
    public static final String URL = "http://s6458.h6.modhost.pro/";
    public static final Integer BREND_OK = 100;
    public static final String CLASS_BRENDU = "state_brendu";
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
    private SlideUp slideUp;
    private TextView countBrend;
    private DialogChoiseBrendu choiseBrendu;
    private DialogChoiseType choiseType;
    private DialogChoiseFor choiseFor;
//    private DialogChoiseYear choiseYear;
//    private DialogChoiseSemeistvo choiseSemeistvo;
//    private DialogChoiseNota choiseNota;
//    private DialogChoiseCountry choiseCountry;
    private BroadcastReceiver receiveAddFavorites;
    public final static String message_add_favorites = "add.favorites";
    private Context context;
    private TextView countType;
    private Integer classBrendu = 0; //0-все, 1-мужской, 2- женский, 3- унисекс
    private TextView countFor;


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiveAddFavorites);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        receiveAddFavorites = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

//                Integer currentPageId = viewPager.getCurrentItem();

                Bundle bundle1 = intent.getExtras();
                Integer updateFragment = bundle1.getInt("update");

                switch (updateFragment) {

                    case 2:
                        Search fragmentSearch = new Search();
                        Bundle bundle = new Bundle();
                        bundle.putInt(CLASS_BRENDU, classBrendu);
                        fragmentSearch.setArguments(bundle);
                        fragmentSearch.registerLoadBrendInfo(MainActivity.this);
                        adapter.updateFragment(fragmentSearch, 1);
                        Favorite favoriteNew = new Favorite();
                        favoriteNew.registerGoToCatalog(MainActivity.this);
                        adapter.updateFragment(favoriteNew, 2);
                        break;
                    case 3:
                        Trash fragmentTrash = new Trash();
                        fragmentTrash.registerGoToCatalog(MainActivity.this);
                        adapter.updateFragment(fragmentTrash, 3);

                }


                viewPager.getAdapter().notifyDataSetChanged();
                setupTabsIcon();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiveAddFavorites, new IntentFilter(message_add_favorites));

        choiseBrendu = new DialogChoiseBrendu();
        choiseBrendu.register(this);

        choiseType = new DialogChoiseType();
        choiseType.register(this);

        choiseFor = new DialogChoiseFor();
        choiseFor.regesterCloseFor(this);

        View slideView = findViewById(R.id.slideView);
        slideUp = new SlideUp.Builder(slideView)
                .withListeners(new SlideUp.Listener.Events() {

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            //Log.i(LOG_TAG, "podbor close");
//                            viewPager.setAlpha(1);
//                            tabLayout.setAlpha(1);
                        }
//						else Log.i(LOG_TAG, "podbor open");

                    }

                    @Override
                    public void onSlide(float percent) {
                        if (percent / 100 > 0.3) {
                            viewPager.setAlpha(percent / 100);
                            tabLayout.setAlpha(percent / 100);
                        }
//                        dim.setAlpha(1 - (percent / 100));
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

        PodborActivationUI();

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
        viewPager.setAlpha(1);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabsIcon();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this, "экран => " + position, Toast.LENGTH_SHORT).show();
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

    private void PodborActivationUI() {

        TextView close, apply;


        close = (TextView) findViewById(R.id.close_podbor);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.hide();
            }
        });

        apply = (TextView) findViewById(R.id.apply_podbor);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.hide();
                Bundle bundle = new Bundle();
                bundle.putInt("state", 0);
                Intent intent = new Intent(getApplicationContext(), BrendActivity.class);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, BREND_OK);
                //перенаправить на BrendActivity
            }
        });

        CrystalRangeSeekbar rangePrice = (CrystalRangeSeekbar) findViewById(R.id.rangePrice);

        final TextView textPrice = (TextView) findViewById(R.id.priceFull);

        rangePrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                String text = minValue + " - " + maxValue;
                textPrice.setText(text);
            }
        });
        rangePrice.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                //Log.d(LOG_TAG, "minValue => " + minValue + "; maxValue => " + maxValue);
            }
        });

        TextView textBrendu = (TextView) findViewById(R.id.textBrendu);
        textBrendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseBrendu.show(getSupportFragmentManager(), "choiseBrendu");
            }
        });

        countBrend = (TextView) findViewById(R.id.text_properties_brendu);

        TextView textType = (TextView) findViewById(R.id.textType);
        textType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseType.show(getSupportFragmentManager(), "choiseType");
            }
        });

        countType = (TextView) findViewById(R.id.text_properties_type);

        TextView choiseForText = (TextView) findViewById(R.id.textFor);
        choiseForText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseFor.show(getSupportFragmentManager(), "choiseFor");
            }
        });


        countFor = (TextView) findViewById(R.id.text_properties_for);

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
        Bundle bundle = new Bundle();
        bundle.putInt(CLASS_BRENDU, classBrendu);
        fragmentSearch.setArguments(bundle);
        fragmentSearch.registerLoadBrendInfo(this);
        adapter.addFragment(fragmentSearch, getResources().getString(R.string.search));

        Favorite fragmentFavorite = new Favorite();
        fragmentFavorite.registerGoToCatalog(this);
        adapter.addFragment(fragmentFavorite, getResources().getString(R.string.favorites));

        Trash trashFragment = new Trash();
        trashFragment.registerGoToCatalog(this);
        adapter.addFragment(trashFragment, getResources().getString(R.string.trash));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (slideUp.isVisible()) {
            slideUp.hide();
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
        bundle.putInt("sex", classBrendu);
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
        if (classBrendu != i) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                    classBrendu = i;
                    break;

            }
            Search fragmentSearch = new Search();
            Bundle bundle = new Bundle();
            bundle.putInt(CLASS_BRENDU, classBrendu);
            fragmentSearch.setArguments(bundle);
            fragmentSearch.registerLoadBrendInfo(this);
            adapter.updateFragment(fragmentSearch, 1);
            viewPager.getAdapter().notifyDataSetChanged();
            setupTabsIcon();

        }
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onClickPodborStart() {
        slideUp.show();
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

    @Override
    public void onCloseChoiseBrendu(String count) {
        countBrend.setText(count);
    }

    @Override
    public void onCloseChoiseType(String count) {
        countType.setText(count);
    }

    @Override
    public void onGoToCatalog(Integer state) {

        switch (state) {
            case 1:
            viewPager.setCurrentItem(1, true);
                 break;
            case 2:
                Trash newTrash = new Trash();
                newTrash.registerGoToCatalog(MainActivity.this);
                adapter.updateFragment(newTrash, 3);
                viewPager.getAdapter().notifyDataSetChanged();
                setupTabsIcon();

                viewPager.setCurrentItem(3);

                break;
        }
    }

    @Override
    public void onCloseChoiseFor(String for_sex) {
        countFor.setText(for_sex);
    }
}
