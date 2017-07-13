package by.lykashenko.demon.mirparfumanew.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.IdParfum;
import by.lykashenko.demon.mirparfumanew.Adapters.AdapterParfumView;
import by.lykashenko.demon.mirparfumanew.Adapters.ParfumCollection;
import by.lykashenko.demon.mirparfumanew.Adapters.Ratting;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetIdParfum;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetRattingPrfum;

import static by.lykashenko.demon.mirparfumanew.BrendActivity.titleToolbar;
import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 06.06.17.
 */

public class FragmentParfumList extends Fragment implements GetIdParfum.OnLoadIdParfum, GetRattingPrfum.OnGetRattingParfum, AdapterParfumView.ClickParfum {

    @Override
    public void onClickParfum(Bundle bundle) {
        getInfoOfParfum.onGetInfoOfParfum(bundle);

    }

    public interface GetInfoOfParfum {
        void onGetInfoOfParfum(Bundle bundle);
    }

    private GetInfoOfParfum getInfoOfParfum;

    public void registrationGetInfoOfParfum(GetInfoOfParfum getInfoOfParfum) {
        this.getInfoOfParfum = getInfoOfParfum;
    }

    private String id;
    private Bundle bundle = new Bundle();
    private String nameBrendList;
    private ArrayList<ParfumCollection> parfumCollections;
    private RecyclerView viewParfum;
    private Integer state = 1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parfum_list, container, false);

        id = getArguments().getString("id");

        Log.i(LOG_TAG, "id brend => " + id);

        nameBrendList = getArguments().getString("name");

        titleToolbar.setText(nameBrendList);
        Log.i(LOG_TAG, "name brend => " + nameBrendList);


        viewParfum = (RecyclerView) v.findViewById(R.id.listParfum);
        viewParfum.setHasFixedSize(true);
        LinearLayoutManager lv = new LinearLayoutManager(getContext());
        viewParfum.setLayoutManager(lv);

        if (bundle.size() > 0) {
            Log.d(LOG_TAG, "parfumlist now save data");
            parfumCollections = bundle.getParcelableArrayList("adapter");
            state = bundle.getInt("state");
            bundle.clear();
        } else {
            //запрос на id, название, лого и цена парфюма для выбранного бренда
            final String SQL_PARFUM_FROM_BREND = "SELECT val.contentid, val.value, val.tmplvarid, " +
                    "ltrim(substring(con.pagetitle,(char_length(substring_index(con.pagetitle,' ',2))+1)))as pagetitle" +
                    " FROM modx_site_content as con,modx_site_tmplvar_contentvalues as val," +
                    "(SELECT val.contentid FROM modx_site_content as con, modx_site_tmplvar_contentvalues " +
                    "as val where con.id=" + id + " and con.pagetitle LIKE val.value)as idp where " +
                    "val.contentid=idp.contentid and con.id=idp.contentid and (val.tmplvarid = 76 " +
                    "or val.tmplvarid = 1) order by val.contentid ASC";
            GetIdParfum idParfum = new GetIdParfum();
            idParfum.registerOnLoadIdParfum(this);
            idParfum.load(SQL_PARFUM_FROM_BREND);
        }

        ImageButton changeAdapter = (ImageButton) v.findViewById(R.id.changeAdapter);
        changeAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    GridLayoutManager lv = new GridLayoutManager(getContext(), 2);
                    viewParfum.setLayoutManager(lv);
                    AdapterParfumView adapter = new AdapterParfumView(parfumCollections,getContext(),state);
                    viewParfum.setAdapter(adapter);
                    state = 2;
                } else {
                    LinearLayoutManager lv = new LinearLayoutManager(getContext());
                    viewParfum.setLayoutManager(lv);
                    AdapterParfumView adapter = new AdapterParfumView(parfumCollections,getContext(),state);
                    viewParfum.setAdapter(adapter);
                    state = 1;
                }
            }
        });

        return v;
    }


    @Override
    public void onLoadIdParfum(ArrayList<IdParfum> idParfum) throws IOException {

//sql запросы для получения рейтинг parfum
        String Sql_Get_Name_And_Price_Parfum = "SELECT com.thread, avg(SUBSTRING(com.properties,16,1)) as rating FROM modx_tickets_comments as com,(SELECT val.contentid FROM modx_site_content as con, modx_site_tmplvar_contentvalues as val where con.id=" + id
                + " and con.pagetitle LIKE val.value) as idp WHERE com.thread =idp.contentid group by thread";

        GetRattingPrfum getRattingParfum = new GetRattingPrfum();
        getRattingParfum.registerGetRattingParfum(this);
        getRattingParfum.load(Sql_Get_Name_And_Price_Parfum, idParfum);


    }

    private void loadParfumData(ArrayList<ParfumCollection> parfumCollections) {


        AdapterParfumView adapter = new AdapterParfumView(parfumCollections,getContext(),state);
        adapter.registerClickParfum(this);
        viewParfum.setAdapter(adapter);

    }


    @Override
    public void onGetRattingParfum(ArrayList<IdParfum> idNameParfum, ArrayList<Ratting> ratingsParfum) {

        parfumCollections = new ArrayList<>();
        int i = 0;
        while ((i < idNameParfum.size())&&(idNameParfum.size()-i>1)) {

            ParfumCollection parfumCollection = new ParfumCollection();
//id
            parfumCollection.setIdParfum(idNameParfum.get(i).getContentid());
            //image
            String pathImage = "http://s6458.h6.modhost.pro/" + idNameParfum.get(i).getValue();
            parfumCollection.setImageParfum(pathImage);
            //rating
            Integer y = 0;
            for (Ratting ratting : ratingsParfum) {

                if (ratting.getThread().equals(idNameParfum.get(i).getContentid())) {
                    parfumCollection.setReatingParfum(ratting.getRating());
                    y = 1;
                    break;
                }
            }
            if (y == 0) {
                parfumCollection.setReatingParfum(0);
            }

            //name

            String name = idNameParfum.get(i).getPagetitle();
            Log.i(MainActivity.LOG_TAG, "name parfum => " + name);
            parfumCollection.setNameParfum(name);


            //price

//			String priceOne ="От "+ namePriceOne.split("==")[1]+" руб.";
            try {
//                Log.d(LOG_TAG, "i => " + i);
                String id = idNameParfum.get(i).getContentid();
//                Log.d(LOG_TAG, "id this position => " + id);
                String id_next = idNameParfum.get(i + 1).getContentid();
//                Log.d(LOG_TAG, "id next position => " + id_next);

                if (!id.equals(id_next)) {
                    String priceFull = getResources().getString(R.string.no_parfum);
                    parfumCollection.setCenaParfum(priceFull);
                    i = i + 1;
//                    Log.d(LOG_TAG, "------------------------");
                } else {

//                    Log.d(LOG_TAG, "true data");
                    String priceOne = idNameParfum.get(i + 1).getValue();
//                    Log.d(LOG_TAG, "stroka s cenoi => " + priceOne);
                    JSONArray json = null;
                    try {
                        json = new JSONArray(priceOne);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Integer price = json.getJSONObject(0).getInt("price");
                        String priceFor = json.getJSONObject(0).getString("title");
//                        Log.i(MainActivity.LOG_TAG, "price parfum => " + price + "; ml parfum => " + priceFor);
                        String priceFull = "От " + Integer.toString(price) + " руб.";
                        String priceForFull = "Ваша цена за " + priceFor;
                        parfumCollection.setCenaParfum(priceFull);
                        parfumCollection.setCenaFor(priceForFull);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i = i + 2;
                }
                parfumCollections.add(parfumCollection);
            } catch (IndexOutOfBoundsException e) {
                String priceFull = "ОШИБКА ИНДЕКСА";
                parfumCollection.setCenaParfum(priceFull);
            }


        }

        loadParfumData(parfumCollections);


    }




//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(LOG_TAG, "onDestroy call");
//    }
//
//    public void onStop() {
//        super.onStop();
//        bundle.putParcelableArrayList("adapter", parfumCollections);
//        bundle.putInt("state", state);
//        Log.d(LOG_TAG, "onStop stop");
//    }

//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("adapter",parfumCollections);
//        outState.putInt("state", state);
//    }

}