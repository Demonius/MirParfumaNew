package by.lykashenko.demon.mirparfumanew.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.NewParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.BrendList;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.CountArray;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.NewParfumList;

/**
 * Created by demon on 20.01.2017.
 */

public class Home extends Fragment implements CountArray.OnCallBackCount, BrendList.OnLoadBrendList, NewParfumList.OnLoadNewParfumList {

 private RecyclerView mRecyclerView, recyclerViewBrendu, recyclerViewNewParfum;
 private RecyclerView.LayoutManager mLayoutManager, mLayoutManagerBrendu;
 private RecyclerView.Adapter adapter, adapterBrendu;
 private Integer[] banner = new Integer[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};
 private ArrayList<Brendu> brendu;
 private String[] mGroupsArrayCall = new String[]{"+375 29 157-57-05"};
 private String[] mCallOther = new String[]{"+375 29 864-35-73", "+375 25 938-71-09"};
 private ExpandableListView expandablePhoneNumber;
 private Integer state = 0;
 private LinearLayout casheLayout;
 private TextView textViewWomen, textViewMen, textViewUnisex;
 private static final Integer MENID = 984;
 private static final Integer WOMENID = 1112;
 private String sql_count_women = "SELECT count(1) FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value=\"женский\"";
 private String sql_count_men = "SELECT count(1) FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value=\"мужской\"";
 private String sql_count_unisex = "SELECT count(1) FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value=\"унисекс\"";


 public View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {


  View vFragment = inflater.inflate(R.layout.fragment_home, null);

//добавляем класс для подсчёта кол-ва данных
  CountArray countArray = new CountArray(getActivity());
// регестрируем interface OnCallBackCount
  countArray.registerCallBackCount(this);
//view в которые добавляем данные
  textViewMen = (TextView) vFragment.findViewById(R.id.textViewMen);
  textViewWomen = (TextView) vFragment.findViewById(R.id.textViewWomen);
  textViewUnisex = (TextView) vFragment.findViewById(R.id.textViewUnisex);

// Отправляем запросы на получение количества данных
  countArray.Count(sql_count_men, 1);
  countArray.Count(sql_count_women, 2);
  countArray.Count(sql_count_unisex, 3);


// Доставка и оплата
  casheLayout = (LinearLayout) vFragment.findViewById(R.id.casheLayout);

  casheLayout.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {

   }
  });

  //номера телефонов для связи
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


//RecyclerView для banner верхенго
  mRecyclerView = (RecyclerView) vFragment.findViewById(R.id.bannerView);
  mRecyclerView.setHasFixedSize(true);
  mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
  mRecyclerView.setLayoutManager(mLayoutManager);
  RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
  mRecyclerView.setItemAnimator(itemAnimator);

  adapter = new AdapterBanner(banner);
  mRecyclerView.setAdapter(adapter);

//RecyclerView для пункта Бренды
  String sql_string_limit = "SELECT con.id,cv.value FROM modx_site_content as con, modx_site_tmplvar_contentvalues as cv WHERE con.parent = 854 and cv.contentid=con.id and cv.tmplvarid=1 ORDER BY rand() Limit 10";
  BrendList brendList = new BrendList(getActivity());
  brendList.registerOnLoadBrendList(this);
  brendList.load(sql_string_limit);
  recyclerViewBrendu = (RecyclerView) vFragment.findViewById(R.id.spisokBrend);
  mLayoutManagerBrendu = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
  recyclerViewBrendu.setLayoutManager(mLayoutManagerBrendu);
  recyclerViewBrendu.setItemAnimator(itemAnimator);

  //RecyclerView для пункта новинки
  String sql_string_new = "select cv.contentid,con.pagetitle,pr.image from modx_site_tmplvar_contentvalues as cv,modx_site_content as con, modx_ms2_products as pr  where (cv.tmplvarid = 58 and cv.value = 46869) and con.id=cv.contentid and pr.id=cv.contentid ORDER BY rand() Limit 10";
  NewParfumList newParfumList = new NewParfumList(getActivity());
  newParfumList.registerOnLoadNewparfumList(this);
  newParfumList.load(sql_string_new);
  recyclerViewNewParfum = (RecyclerView) vFragment.findViewById(R.id.spisokNovinki);
  LinearLayoutManager mLayoutManagerNewParfum = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
  recyclerViewNewParfum.setLayoutManager(mLayoutManagerNewParfum);
  recyclerViewNewParfum.setItemAnimator(itemAnimator);


  return vFragment;
 }


 //INTERFACE CALLBACK
//Обработка interface OnCallBackCount для отображения в Home
 @Override
 public void onCallBackCount(Integer count, Integer state) {

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
  }

 }

 @Override
 public void onLoadBrendList(ArrayList<Brendu> brendu) {
  adapterBrendu = new AdapterBrendu(brendu);
  recyclerViewBrendu.setAdapter(adapterBrendu);
 }

 @Override
 public void onLoadNewParfumList(ArrayList<NewParfum> newParfums) {
  AdapterNewParfum adapterNewParfum = new AdapterNewParfum(newParfums);
  recyclerViewNewParfum.setAdapter(adapterNewParfum);
 }


 //ADAPTERS RECYCLERVIEW
//adapter для RecyclerView banner
 private class AdapterBanner extends RecyclerView.Adapter<AdapterBanner.BannerViewHolder> {

  private Integer[] m_banner;

  public AdapterBanner(Integer[] banner) {
   m_banner = banner;
  }

  public class BannerViewHolder extends RecyclerView.ViewHolder {

   public CardView cd;
   public ImageView image;

   public BannerViewHolder(View itemView) {
    super(itemView);
    cd = (CardView) itemView.findViewById(R.id.card_banner);
    image = (ImageView) itemView.findViewById(R.id.image_banner);

    cd.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
      Integer position = getAdapterPosition();
      Log.d(MainActivity.LOG_TAG, "выбран banner " + position);
     }
    });
   }
  }

  @Override
  public AdapterBanner.BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

   View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_banner, parent, false);
   BannerViewHolder bannerViewHolder = new BannerViewHolder(v);
   return bannerViewHolder;
  }

  @Override
  public void onBindViewHolder(final BannerViewHolder holder, int position) {

   holder.image.setImageResource(m_banner[position]);
//меняет баннер через определённое время. Проблема с реализацией клика по конкретному банеру
//            final BannerViewHolder holder1=holder;
//
//            Timer myTimer = new Timer(); // Создаем таймер
//
//
//            myTimer.schedule(new TimerTask() { // Определяем задачу
//
//                Integer i =0;
//                @Override
//                public void run() {
//                    if (state == 0) {
//
//                            Log.d(MainActivity.LOG_TAG, "позиция = "+i);
//                            getActivity().runOnUiThread(new Runnable(){
//                                @Override
//                                public void run() {
//                                    if (holder1.image.isShown())
//                                    {
//                                    holder1.image.setImageResource(m_banner[i]);
//
//                                    }else cancel();
//                                }
//                            });
//                            i=i+1;
//                            if (i==(getItemCount()-1)){
//                                state = 1;
//                            }
//                    }else{
//
//                        Log.d(MainActivity.LOG_TAG, "позиция = "+i);
//                        getActivity().runOnUiThread(new Runnable(){
//                            @Override
//                            public void run() {
//                                if (holder1.image.isShown())
//                                {
//                                    holder1.image.setImageResource(m_banner[i]);
//                                }else cancel();
//                            }
//                        });
//                        i=i-1;
//                        if (i==0){
//                            state = 0;
//                        }
//                    }
//
//                }
//            }, 0, 5000);


  }

  @Override
  public int getItemCount() {
   return m_banner.length;
  }
 }

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
  public void onBindViewHolder(BrenduViewHolder holder, int position) {
   String url_image_brend = "http://s6458.h6.modhost.pro/" + m_brendu.get(position).getValue();
   Log.i(MainActivity.LOG_TAG, "brend image => " + url_image_brend);
   Picasso.with(getContext()).load(url_image_brend).into(holder.image);
//            holder.image.setImageResource(m_brendu[position]);


  }

  @Override
  public int getItemCount() {
   return m_brendu.size();
  }


 }

 //adapter для RecyclerView Новинки
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
  public void onBindViewHolder(ParfumViewHolder holder, int position) {
   String url_image_parfum = "http://s6458.h6.modhost.pro/" + newParfums.get(position).getImage();
   Picasso.with(getContext()).load(url_image_parfum).into(holder.imageParfum);
   holder.nameParfum.setText(newParfums.get(position).getPagetitle());

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
