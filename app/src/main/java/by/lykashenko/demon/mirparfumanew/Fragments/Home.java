package by.lykashenko.demon.mirparfumanew.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.NewParfum;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogExitError;
import by.lykashenko.demon.mirparfumanew.Fragments.Dialogs.DialogPodborParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.BrendList;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountArray;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.LoadListData;

/**
 * Created by demon on 20.01.2017.
 */

public class Home extends Fragment implements CountArray.OnCallBackCount, BrendList.OnLoadBrendList, LoadListData.OnLoadNewParfumList {

    public interface StartBrenduHome{
        void onStartBrenduHome(Bundle bundle);
        void onClickParfumCategory(Integer i);
    }

    StartBrenduHome startBrenduHome;

    public void registerHomeBrendu(StartBrenduHome startBrenduHome){
        this.startBrenduHome=startBrenduHome;
    }

    private RecyclerView  recyclerViewBrendu, recyclerViewNewParfum, recyclerViewSales, recyclerViewFavorites;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManagerBrendu;
    private RecyclerView.Adapter adapterBrendu;
    private Integer[] banner = new Integer[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};
    private ArrayList<Brendu> brendu;
    private String[] mGroupsArrayCall = new String[]{"+375 29 157-57-05"};
    private String[] mCallOther = new String[]{"+375 29 864-35-73", "+375 25 938-71-09"};
    private ExpandableListView expandablePhoneNumber;
    private Integer state = 0;
    private LinearLayout casheLayout,nisha,probniki;
    private TextView textViewWomen, textViewMen, textViewUnisex, textViewOtzuvu;
    private static final Integer MENID = 984;
    private static final Integer WOMENID = 1112;
    private String sql_count_women = "SELECT count(1) FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value=\"женский\"";
    private String sql_count_men = "SELECT count(1) FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value=\"мужской\"";
    private String sql_count_unisex = "SELECT count(1) FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value=\"унисекс\"";
    public static String sql_count_otzuvu = "SELECT count(1) FROM `modx_tickets_comments` where thread = 27969 and createdby=0 and editedby=0";
    private RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
    private LoadListData newListData;
    public CountArray countArray;
    private ViewStub viewStubSales,viewStubNisha, viewStubProbniki;
    private View vFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         vFragment = inflater.inflate(R.layout.fragment_home, null);

        viewStubNisha = (ViewStub) vFragment.findViewById(R.id.viewStubNisha);
        viewStubProbniki = (ViewStub) vFragment.findViewById(R.id.viewStubProbniki);
        viewStubSales = (ViewStub) vFragment.findViewById(R.id.viewStubSales);

        TextView textBrenduAll = (TextView) vFragment.findViewById(R.id.textViewAllBrendu);
        textBrenduAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewPager) getActivity().findViewById(R.id.viewpager)).setCurrentItem(1);
            }
        });

        CarouselView carouselView = (CarouselView) vFragment.findViewById(R.id.carouselView);
        carouselView.setPageCount(banner.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int i, ImageView imageView) {
                imageView.setImageResource(banner[i]);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        });


        newListData = new LoadListData(getActivity());
        newListData.registerOnLoadListData(this);

//добавляем класс для подсчёта кол-ва данных
        countArray = new CountArray(getActivity());

// регестрируем interface OnCallBackCount
        countArray.registerCallBackCount(this);

//view в которые добавляем данные
        textViewMen = (TextView) vFragment.findViewById(R.id.textViewMen);
        textViewWomen = (TextView) vFragment.findViewById(R.id.textViewWomen);
        textViewUnisex = (TextView) vFragment.findViewById(R.id.textViewUnisex);
        textViewOtzuvu = (TextView) vFragment.findViewById(R.id.textViewOtzuvuHome);

        LinearLayout podbor = (LinearLayout) vFragment.findViewById(R.id.pickLayout);
        podbor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPodborParfum podborAll = new DialogPodborParfum();

            }
        });

// Отправляем запросы на получение количества данных
        countArray.Count(sql_count_men, 1);
        countArray.Count(sql_count_women, 2);
        countArray.Count(sql_count_unisex, 3);
        countArray.Count(sql_count_otzuvu, 4);

// Доставка и оплата
        casheLayout = (LinearLayout) vFragment.findViewById(R.id.casheLayout);

        casheLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //номера телефонов для связи
        addContactPhoneNumber(vFragment);

//RecyclerView для пункта Бренды
        addRecyclerViewBrendu(vFragment);

        //RecyclerView для пункта новинки
        addRecyclerViewNovinki(vFragment);

        //RecyclerView для пункта Распродажи
        addRecyclerViewSales(vFragment);

        //RecyclerView для пункта Бестселлеры
        addRecyclerViewFavorites(vFragment);

        LinearLayout women = (LinearLayout) vFragment.findViewById(R.id.womenLayout);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrenduHome.onClickParfumCategory(1);
            }
        });

        return vFragment;
    }

    private void addRecyclerViewFavorites(View vFragment) {
        String sql_string_favorites = "select cv.contentid,con.pagetitle,pr.image from modx_site_tmplvar_contentvalues as cv,modx_site_content as con, modx_ms2_products as pr  where cv.tmplvarid = 58 and cv.value like '%46870%' and con.id=cv.contentid and pr.id=cv.contentid ORDER BY rand() Limit 10";
        newListData.load(sql_string_favorites, 3);
        recyclerViewFavorites = (RecyclerView) vFragment.findViewById(R.id.spisokFavorites);
        LinearLayoutManager mLayoutManagerFavorites = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFavorites.setLayoutManager(mLayoutManagerFavorites);
        recyclerViewFavorites.setItemAnimator(itemAnimator);
    }

    private void addRecyclerViewSales(View vFragment) {
        String sql_string_sales = "select cv.contentid,con.pagetitle,pr.image from modx_site_tmplvar_contentvalues as cv,modx_site_content as con, modx_ms2_products as pr  where (cv.tmplvarid = 58 and cv.value like '%46868%') and con.id=cv.contentid and pr.id=cv.contentid ORDER BY rand() Limit 10";
        newListData.load(sql_string_sales, 2);

    }

    private void addRecyclerViewNovinki(View vFragment) {
        String sql_string_new = "select cv.contentid,con.pagetitle,pr.image from modx_site_tmplvar_contentvalues as cv,modx_site_content as con, modx_ms2_products as pr  where cv.tmplvarid = 58 and cv.value like '%46869%' and con.id=cv.contentid and pr.id=cv.contentid ORDER BY rand() Limit 10";
        newListData.load(sql_string_new, 1);
        recyclerViewNewParfum = (RecyclerView) vFragment.findViewById(R.id.spisokNovinki);
        LinearLayoutManager mLayoutManagerNewParfum = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewParfum.setLayoutManager(mLayoutManagerNewParfum);
        recyclerViewNewParfum.setItemAnimator(itemAnimator);
    }

    private void addRecyclerViewBrendu(View vFragment) {
        String sql_string_limit = "SELECT con.id,cv.value,con.pagetitle FROM modx_site_content as con, modx_site_tmplvar_contentvalues as cv WHERE con.parent = 854 and cv.contentid=con.id and cv.tmplvarid=1 ORDER BY rand() Limit 10";
        BrendList brendList = new BrendList(getActivity());
        brendList.registerOnLoadBrendList(this);
        brendList.load(sql_string_limit);
        recyclerViewBrendu = (RecyclerView) vFragment.findViewById(R.id.spisokBrend);
        mLayoutManagerBrendu = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBrendu.setLayoutManager(mLayoutManagerBrendu);
        recyclerViewBrendu.setItemAnimator(itemAnimator);
    }

    private void addContactPhoneNumber(View vFragment) {
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
        SimpleExpandableListAdapter adapterExpandable = new SimpleExpandableListAdapter(getContext(),
                listDataHeader,
                R.layout.group_name,
                groupFrom,
                groupTo,
                listDataChild,
                R.layout.child_group_name,
                childFrom,
                childTo);
        expandablePhoneNumber = (ExpandableListView) vFragment.findViewById(R.id.expandablePhoneNumberHome);
        expandablePhoneNumber.setAdapter(adapterExpandable);
    }

    //INTERFACE CALLBACK
//Обработка interface OnCallBackCount для отображения в Home
    @Override
    public void onCallBackCount(String count, Integer state) {

        switch (state) {
            case 1:
                String text = getResources().getString(R.string.men_parfum) + " (" + count + ")";
                textViewMen.setText(text);
                break;

            case 2:
                String text1 = getResources().getString(R.string.women_parfum) + " (" + count + ")";
                textViewWomen.setText(text1);
                break;
            case 3:
                String text2 = getResources().getString(R.string.unisex_parfum) + " (" + count + ")";
                textViewUnisex.setText(text2);
                break;
            case 4:
                String otzuvu_count = getResources().getString(R.string.otzuvu) + " (" + count + ")";
                textViewOtzuvu.setText(otzuvu_count);
                break;
            case 5:
                DialogExitError dialog = new DialogExitError();
                dialog.show(getActivity().getFragmentManager(), "dlg1");

                break;
        }

    }

    @Override
    public void onLoadBrendList(ArrayList<Brendu> brendu) {

        Log.i(MainActivity.LOG_TAG, "brendu =>" + brendu);
        if (brendu != null) {
            adapterBrendu = new AdapterBrendu(brendu);
            recyclerViewBrendu.setAdapter(adapterBrendu);
        } else {
            Log.d(MainActivity.LOG_TAG, "brendu => null");
        }
    }

    @Override
    public void onLoadNewParfumList(ArrayList<NewParfum> newParfums, int i) {
        if (newParfums != null) {
            switch (i) {
                case 1:
                    AdapterNewParfum adapterNewParfum = new AdapterNewParfum(newParfums);
                    recyclerViewNewParfum.setAdapter(adapterNewParfum);
                    break;
                case 2:
                    if (newParfums.size()>0) {
                        viewStubSales.inflate();
                        recyclerViewSales = (RecyclerView) vFragment.findViewById(R.id.spisokSales);
                        LinearLayoutManager mLayoutManagerSales = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewSales.setLayoutManager(mLayoutManagerSales);
                        recyclerViewSales.setItemAnimator(itemAnimator);
                        AdapterNewParfum adapterSales = new AdapterNewParfum(newParfums);
                        recyclerViewSales.setAdapter(adapterSales);

                    }
                    break;
                case 3:
                    AdapterNewParfum adapterFavorites = new AdapterNewParfum(newParfums);
                    recyclerViewFavorites.setAdapter(adapterFavorites);
                    break;
            }
        } else {
            Log.d(MainActivity.LOG_TAG, "parfums null");
        }
    }


    //ADAPTERS RECYCLERVIEW
    //adapter для RecyclerView Бренды
    private class AdapterBrendu extends RecyclerView.Adapter<AdapterBrendu.BrenduViewHolder> {
        private ArrayList<Brendu> m_brendu;

        public AdapterBrendu(ArrayList<Brendu> brendu) {
            m_brendu = brendu;
        }

        public class BrenduViewHolder extends RecyclerView.ViewHolder {

            public CardView cd;
            public ImageView image;

            public BrenduViewHolder(View itemView) {
                super(itemView);

                cd = (CardView) itemView.findViewById(R.id.card_brendu);
                image = (ImageView) itemView.findViewById(R.id.imageViewBrendu);


            }
        }

        @Override
        public BrenduViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_brendu, parent, false);
            BrenduViewHolder brenduViewHolder = new BrenduViewHolder(v);
            return brenduViewHolder;
        }

        @Override
        public void onBindViewHolder(BrenduViewHolder holder, final int position) {
            String url_image_brend = "http://s6458.h6.modhost.pro/" + m_brendu.get(position).getValue();
//            Log.i(MainActivity.LOG_TAG, "brend image => " + url_image_brend);
            Picasso.with(getContext()).load(url_image_brend).into(holder.image);
            holder.cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//вызов второго активити с brend
                    Bundle bundle = new Bundle();
                    bundle.putString("id", m_brendu.get(position).getId());
                    bundle.putString("name", m_brendu.get(position).getPagetitle());
                    bundle.putInt("state",1);
                    startBrenduHome.onStartBrenduHome(bundle);

                }
            });

        }

        @Override
        public int getItemCount() {
            return m_brendu.size();
        }


    }

    //adapter для RecyclerView Новинки, Распродажи и Бестселлеры
    private class AdapterNewParfum extends RecyclerView.Adapter<AdapterNewParfum.ParfumViewHolder> {

        private ArrayList<NewParfum> newParfums;

        public AdapterNewParfum(ArrayList<NewParfum> newParfums) {
            this.newParfums = newParfums;
        }

        @Override
        public ParfumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_newparfum, parent, false);
            ParfumViewHolder pvh = new ParfumViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(ParfumViewHolder holder, final int position) {
            String url_image_parfum = "http://s6458.h6.modhost.pro/" + newParfums.get(position).getImage();
            Picasso.with(getContext()).load(url_image_parfum).into(holder.imageParfum);
            holder.nameParfum.setText(newParfums.get(position).getPagetitle());
holder.cd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("id", newParfums.get(position).getContentid());
        bundle.putString("name", newParfums.get(position).getPagetitle());
        bundle.putInt("state",2);
        startBrenduHome.onStartBrenduHome(bundle);

    }
});

        }

        @Override
        public int getItemCount() {
            return newParfums.size();
        }

        public class ParfumViewHolder extends RecyclerView.ViewHolder {

            private CardView cd;
            private ImageView imageParfum;
            private TextView nameParfum;

            public ParfumViewHolder(View itemView) {
                super(itemView);
                cd = (CardView) itemView.findViewById(R.id.cardViewNewParfum);
                imageParfum = (ImageView) itemView.findViewById(R.id.imageParfum);
                nameParfum = (TextView) itemView.findViewById(R.id.nameParfum);
            }
        }
    }
}
