package by.lykashenko.demon.mirparfumanew;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.mancj.slideup.SlideUp;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.AboutDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseBrendu;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseCountry;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseFor;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseNota;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseSemeistvo;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseType;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogChoiseYear;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.InfoDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.OtzuvuDialogFragment;
import by.lykashenko.demon.mirparfumanew.Fragments.Favorite;
import by.lykashenko.demon.mirparfumanew.Fragments.Home;
import by.lykashenko.demon.mirparfumanew.Fragments.Search;
import by.lykashenko.demon.mirparfumanew.Fragments.Trash;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountArray;
import by.lykashenko.demon.mirparfumanew.Table.Favorites;
import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Home.StartBrenduHome,
        Search.LoadBrendInfo,
        Favorite.GoToCatalog,
        DialogChoiseBrendu.CloseBrendu,
        DialogChoiseType.CloseType,
        Trash.GoToCatalog,
        DialogChoiseFor.CloseChoiseFor,
        DialogChoiseYear.CloseYear,
        DialogChoiseCountry.CloseCountry,
        DialogChoiseSemeistvo.CloseSemeistvo,
        DialogChoiseNota.CloseNota{

    public static final String LOG_TAG = "MirParfuma";
    public static final String URL = "http://beta.mirparfuma.com/";
    public static final Integer BREND_OK = 100;
    public static final String CLASS_BRENDU = "state_brendu";
    private TabLayout tabLayout;
    private CountArray countArray;
    private TextView otzuvu, about, cashe;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private DrawerLayout drawer;
    private SlideUp slideUp;
    private TextView countBrend;
    private DialogChoiseBrendu choiseBrendu;
    private DialogChoiseType choiseType;
    private DialogChoiseFor choiseFor;
    private DialogChoiseYear choiseYear;
    private DialogChoiseSemeistvo choiseSemeistvo;
    private DialogChoiseNota choiseNota;
    private DialogChoiseCountry choiseCountry;
    private BroadcastReceiver receiveAddFavorites;
    private LinearLayout layoutToolbar;
    public final static String message_add_favorites = "add.favorites";
    private TextView countType;
    private Integer classBrendu = 0; //0-все, 1-мужской, 2- женский, 3- унисекс
    private TextView countFor, countYear, countSemeistvo, countNota, countCountry;
    private View view;
    private TextView countTrash;

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiveAddFavorites);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiveAddFavorites = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

//                Integer currentPageId = viewPager.getCurrentItem();
Integer favoriteFragmentVisible = 0;
                Bundle bundle1 = intent.getExtras();
                Integer updateFragment = bundle1.getInt("update");
if (viewPager.getCurrentItem()==2){
    favoriteFragmentVisible=2;
}
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
                        favoriteFragmentVisible =2;
                        break;
                    case 3:
                        Trash fragmentTrash = new Trash();
                        fragmentTrash.registerGoToCatalog(MainActivity.this);
                        adapter.updateFragment(fragmentTrash, 3);

                }


                viewPager.getAdapter().notifyDataSetChanged();

                setupTabsIcon(favoriteFragmentVisible);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiveAddFavorites, new IntentFilter(message_add_favorites));

        choiseBrendu = new DialogChoiseBrendu();
        choiseBrendu.registerBrendu(this);

        choiseType = new DialogChoiseType();
        choiseType.registerType(this);

        choiseFor = new DialogChoiseFor();
        choiseFor.regesterCloseFor(this);

        choiseYear = new DialogChoiseYear();
        choiseYear.registerYear(this);

        choiseCountry = new DialogChoiseCountry();
        choiseCountry.registerCountry(this);

        choiseSemeistvo = new DialogChoiseSemeistvo();
        choiseSemeistvo.registerSemeistvo(this);

        choiseNota = new DialogChoiseNota();
        choiseNota.registerNota(this);

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
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case android.R.id.home:

                        drawer.openDrawer(GravityCompat.START);

                        return true;
                    case R.id.action_client:
                        Toast.makeText(getBaseContext(), "Пока вы не можете подключиться к личному кабинету", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });
        layoutToolbar = toolbar.findViewById(R.id.toolbar_layout);
        view = LinearLayout.inflate(getBaseContext(), R.layout.viewstub_layout_image_toolbar, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        layoutToolbar.addView(view);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setAlpha(1);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabsIcon(0);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i=0; i<tabLayout.getTabCount();i++){

                    if (i==position){
                        tabLayout.getTabAt(i).getCustomView().setBackgroundColor(getResources().getColor(R.color.colorTabSelected));
                    }else{
                        tabLayout.getTabAt(i).getCustomView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                }


                layoutToolbar.removeView(view);

                if (position==1&&classBrendu!=0){
                    view = LayoutInflater.from(getBaseContext()).inflate(R.layout.viewstub_layout_textview_toolbar, null);
                    TextView text = view.findViewById(R.id.textToolbarCenter);
                    switch (classBrendu) {
                        case 1:
                            text.setText("Мужская парфюмерия");
                            toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                            break;
                        case 2:
                            text.setText("Женская парфюмерия");
                            toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                            break;
                        case 3:
                            text.setText("Унисекс парфюмерия");
                            toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                            break;

                    }
                    layoutToolbar.addView(view);
                }else{
                    view = LinearLayout.inflate(getBaseContext(), R.layout.viewstub_layout_image_toolbar, null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.setLayoutParams(params);
                    layoutToolbar.addView(view);
                    toggle.setHomeAsUpIndicator(R.drawable.ic_nav_menu);
                }
                //Toast.makeText(MainActivity.this, "экран => " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classBrendu!=0&&viewPager.getCurrentItem()==1) {
                    classBrendu=0;
                    Search fragmentSearch = new Search();
                    Bundle bundle = new Bundle();
                    bundle.putInt(CLASS_BRENDU, classBrendu);
                    fragmentSearch.setArguments(bundle);
                    fragmentSearch.registerLoadBrendInfo(MainActivity.this);
                    adapter.updateFragment(fragmentSearch, 1);
                    viewPager.getAdapter().notifyDataSetChanged();
                    setupTabsIcon(0);
                    viewPager.setCurrentItem(0);


                }else{
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final NavigationView navigationView = findViewById(R.id.nav_view);

        TextView linkParfumSite = navigationView.getHeaderView(0).findViewById(R.id.link_mir_parfuma);

        SpannableString text_spannable = new SpannableString(getResources().getString(R.string.link_mir_Parfuma));
        text_spannable.setSpan(new UnderlineSpan(), 0, text_spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkParfumSite.setText(text_spannable);

        linkParfumSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri addressCrm = Uri.parse(MainActivity.URL);
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, addressCrm);
                startActivity(openLinkIntent);
            }
        });

        final LinearLayout phoneLayout = navigationView.getHeaderView(0).findViewById(R.id.block_phone);

        final View layout = LayoutInflater.from(getBaseContext()).inflate(R.layout.add_two_number, null);

        final ImageView imageArrowUpDown = navigationView.getHeaderView(0).findViewById(R.id.image_arrow);

        final TextView textNumberVelcom = navigationView.getHeaderView(0).findViewById(R.id.number_velcom);

        textNumberVelcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.velcom)));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.velcom)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        final Animation animImageDown = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        final Animation animImageUp = new RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animImageDown.setInterpolator(new LinearInterpolator());
        animImageUp.setInterpolator(new LinearInterpolator());
        animImageDown.setDuration(300);
        animImageDown.setFillEnabled(true);
        animImageUp.setFillEnabled(true);
        animImageUp.setDuration(300);

        imageArrowUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneLayout.getChildCount() == 1) {
                    Animation animatedUp = new ScaleAnimation(1f, 1f,
                            0f, 1f,
                            Animation.RELATIVE_TO_PARENT, 0f,
                            Animation.RELATIVE_TO_PARENT, 0f);
                    animatedUp.setDuration(300);
                    layout.setAnimation(animatedUp);
                    phoneLayout.addView(layout);
                    //animated image
                    animImageDown.setFillAfter(true);
                    imageArrowUpDown.startAnimation(animImageDown);

                    TextView textNumberMTS = navigationView.getHeaderView(0).findViewById(R.id.number_mts);
                    TextView textNumberLife = navigationView.getHeaderView(0).findViewById(R.id.number_life);

                    textNumberMTS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.mts)));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } else {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 200);
                                }
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.mts)));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                    textNumberLife.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.life)));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } else {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 200);
                                }
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.life)));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });


                } else {
                    Animation animatedDown = new ScaleAnimation(
                            1f, 1f,
                            1f, 0f,
                            Animation.RELATIVE_TO_PARENT, 0f,
                            Animation.RELATIVE_TO_PARENT, 0f);
                    animatedDown.setDuration(300);
                    layout.setAnimation(animatedDown);
                    phoneLayout.removeView(layout);
                    //animated image
                    animImageUp.setFillAfter(true);
                    imageArrowUpDown.startAnimation(animImageUp);

                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        otzuvu = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewOtzuvu);

        otzuvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtzuvuDialogFragment otzuvuDialogFragment = new OtzuvuDialogFragment();
                otzuvuDialogFragment.show(getSupportFragmentManager(),"dlgOtzuvu");
            }
        });

        about = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewAbout);
        cashe = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewCashe);
        TextView info = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewInfo);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 100) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.velcom)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if (requestCode == 200) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.mts)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if (requestCode == 300) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getResources().getString(R.string.life)));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        TextView choiseYearText = (TextView) findViewById(R.id.textYear);
        choiseYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseYear.show(getSupportFragmentManager(), "choiseYear");
            }
        });

        countYear = (TextView) findViewById(R.id.text_properties_year);

        TextView choiseCountryText = (TextView) findViewById(R.id.textCountry);
        choiseCountryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseCountry.show(getSupportFragmentManager(), "choiseCountry");
            }
        });

        countCountry = (TextView) findViewById(R.id.text_properties_country);

        TextView choiseSemeistvoText = (TextView) findViewById(R.id.textAromat);
        choiseSemeistvoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseSemeistvo.show(getSupportFragmentManager(), "choiseSemeistvo");
            }
        });

        countSemeistvo = (TextView) findViewById(R.id.text_properties_aromat);

        TextView choiseNotaText = (TextView) findViewById(R.id.textNota);
        choiseNotaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseNota.show(getSupportFragmentManager(), "choiseNots");
            }
        });

        countNota = (TextView) findViewById(R.id.text_properties_nota);

    }

    private void AboutDialog() {

        AboutDialogFragment aboutDialog = new AboutDialogFragment();
        aboutDialog.show(getSupportFragmentManager(), "dlg1");
    }

    private void setupTabsIcon(Integer tabsIndex) {

        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabOne.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_home));
        ((TextView) tabOne.findViewById(R.id.text_tab)).setText(R.string.home);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabTwo.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_search));
        ((TextView) tabTwo.findViewById(R.id.text_tab)).setText(R.string.search);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabThree.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        ((TextView) tabThree.findViewById(R.id.text_tab)).setText(R.string.favorites);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        ConstraintLayout tabFour = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.custum_tab_trash, null);
        tabFour.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabFour.findViewById(R.id.image_tab).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_trash));

        countTrash = tabFour.findViewById(R.id.widget_counter);
        ActiveAndroid.beginTransaction();
        countTrash.setText(Integer.toString(new Select().from(by.lykashenko.demon.mirparfumanew.Table.Trash.class).execute().size()));
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        ((TextView) tabFour.findViewById(R.id.text_tab)).setText(R.string.trash);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        tabLayout.getTabAt(tabsIndex).getCustomView().setBackgroundColor(getResources().getColor(R.color.colorTabSelected));


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
            setupTabsIcon(1);

            layoutToolbar.removeView(view);

            if (i == 0) {
                view = LinearLayout.inflate(getBaseContext(), R.layout.viewstub_layout_image_toolbar, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                layoutToolbar.addView(view);
            } else {
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.viewstub_layout_textview_toolbar, null);
                TextView text = view.findViewById(R.id.textToolbarCenter);
                switch (classBrendu) {
                    case 1:
                        text.setText("Мужская парфюмерия");
                        break;
                    case 2:
                        text.setText("Женская парфюмерия");
                        break;
                    case 3:
                        text.setText("Унисекс парфюмерия");
                        break;

                }
                layoutToolbar.addView(view);
            }

//            ImageView image = findViewById(R.id.imageToolbar);
//            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_bottom));
//            TextView text = findViewById(R.id.textToolbar);
//            switch(classBrendu){
//                case 1:
//                    text.setText("Мужская парфюмерия");
//                    break;
//                case 2:
//                    text.setText("Женская парфюмерия");
//                    break;
//                case 3:
//                    text.setText("Унисекс парфюмерия");
//                    break;
//            }

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
    public void onCloseBrendu(String count) {
        countBrend.setText(count);
    }

    @Override
    public void onCloseType(String count) {
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
                setupTabsIcon(3);

                viewPager.setCurrentItem(3);

                break;
        }
    }

    @Override
    public void onCloseChoiseFor(String for_sex) {
        countFor.setText(for_sex);
    }

    @Override
    public void onCloseYear(String count) {
        countYear.setText(count);
    }

    @Override
    public void onCloseCountry(String count) {
        countCountry.setText(count);
    }

    @Override
    public void onCloseSemeistvo(String count) {
        countSemeistvo.setText(count);
    }

    @Override
    public void onCloseNota(String count) {
        countNota.setText(count);
    }

//    @Override
//    public void updateCountTrash(Integer count) {
//        ((TextView)tabLayout.getTabAt(3).getCustomView().findViewById(R.id.widget_counter)).setText(Integer.toString(count));
//    }
}
