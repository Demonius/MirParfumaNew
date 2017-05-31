package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.IdParfum;
import by.lykashenko.demon.mirparfumanew.Adapters.ParfumCollection;
import by.lykashenko.demon.mirparfumanew.Adapters.Ratting;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetIdParfum;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetRattingPrfum;

/**
 * Created by demon on 17.05.2017.
 */

public class DialogFragmentParfum extends DialogFragment implements GetIdParfum.OnLoadIdParfum, GetRattingPrfum.OnGetRattingParfum, View.OnClickListener {


    private String id;

    private ParfumCollection parfumCollection;
    private ArrayList<ParfumCollection> parfumCollections;
    private RecyclerView viewParfum;
    private Integer state = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_NoActionBar);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);


        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_parfum_for_brend, container, false);

        Toolbar toolbarAbout = (Toolbar) v.findViewById(R.id.toolbarAbout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarAbout);
        id = getArguments().getString("id");

        Log.i(MainActivity.LOG_TAG, "id brend => " + id);

        String nameBrendList = getArguments().getString("name");


        ImageView image_back = (ImageView) v.findViewById(R.id.image_back_parfumlist);
        TextView nameBrend = (TextView) v.findViewById(R.id.nameBrend);
        LinearLayout home = (LinearLayout) v.findViewById(R.id.linearHome);
        LinearLayout search = (LinearLayout) v.findViewById(R.id.linearSearch);
        LinearLayout favorites = (LinearLayout) v.findViewById(R.id.linearFavorites);
        LinearLayout trash = (LinearLayout) v.findViewById(R.id.linearTrash);

        home.setOnClickListener(this);
        search.setOnClickListener(this);
        favorites.setOnClickListener(this);
        trash.setOnClickListener(this);

        nameBrend.setText(nameBrendList);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        viewParfum = (RecyclerView) v.findViewById(R.id.listParfum);
        viewParfum.setHasFixedSize(true);

        ImageButton changeAdapter = (ImageButton) v.findViewById(R.id.changeAdapter);
        changeAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state==1){
                    GridLayoutManager lv = new GridLayoutManager(getContext(),2);
                    viewParfum.setLayoutManager(lv);
                    AdapterParfumView adapter = new AdapterParfumView(parfumCollections);
                    viewParfum.setAdapter(adapter);
                    state=2;
                }else{
                    LinearLayoutManager lv = new LinearLayoutManager(getContext());
                    viewParfum.setLayoutManager(lv);
                    AdapterParfumView adapter = new AdapterParfumView(parfumCollections);
                    viewParfum.setAdapter(adapter);
                    state=1;
                }
            }
        });

//запрос на id подкатегорий, название, лого и цена  для бренда
        final String SQL_PARFUM_FROM_BREND = "SELECT val.contentid, val.value, val.tmplvarid, " +
                "con.pagetitle FROM modx_site_content as con,modx_site_tmplvar_contentvalues as val," +
                "(SELECT val.contentid FROM modx_site_content as con, modx_site_tmplvar_contentvalues " +
                "as val where con.id=" + id + " and con.pagetitle LIKE val.value)as idp where " +
                "val.contentid=idp.contentid and con.id=idp.contentid and (val.tmplvarid = 76 " +
                "or val.tmplvarid = 1) order by val.contentid ASC";
        GetIdParfum idParfum = new GetIdParfum();
        idParfum.registerOnLoadIdParfum(this);
        idParfum.load(SQL_PARFUM_FROM_BREND);
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

        LinearLayoutManager lv = new LinearLayoutManager(getContext());
        viewParfum.setLayoutManager(lv);
        AdapterParfumView adapter = new AdapterParfumView(parfumCollections);
        viewParfum.setAdapter(adapter);

    }


    @Override
    public void onGetRattingParfum(ArrayList<IdParfum> idNameParfum, ArrayList<Ratting> ratingsParfum) {

        parfumCollections = new ArrayList<>();

        for (int i = 0; i < idNameParfum.size(); i = i + 2) {

            parfumCollection = new ParfumCollection();
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

            String nameFull = idNameParfum.get(i).getPagetitle();
            String name = nameFull.substring(15, nameFull.length());
            parfumCollection.setNameParfum(name);
            Log.i(MainActivity.LOG_TAG, "name parfum => " + name);


            //price

//			String priceOne ="От "+ namePriceOne.split("==")[1]+" руб.";
            try {
                if (idNameParfum.get(i).getContentid().equals(idNameParfum.get(i + 1).getContentid())) {


                    String priceOne = idNameParfum.get(i + 1).getValue();
                    JSONArray json = null;
                    try {
                        json = new JSONArray(priceOne);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Integer price = json.getJSONObject(0).getInt("price");
                        String priceFor = json.getJSONObject(0).getString("title");
                        String priceFull = "От " + Integer.toString(price) + " руб.";
                        String priceForFull = "Ваша цена за " + priceFor.split(" ")[0] + " (" + priceFor.split(" ")[1] + ")";
                        parfumCollection.setCenaParfum(priceFull);
                        parfumCollection.setCenaFor(priceForFull);
                        Log.i(MainActivity.LOG_TAG, "price parfum => " + priceFull);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    String priceFull = "Нет данных";
                    parfumCollection.setCenaParfum(priceFull);
                }
            } catch (IndexOutOfBoundsException e) {
                String priceFull = "Нет данных";
                parfumCollection.setCenaParfum(priceFull);
            }

            parfumCollections.add(parfumCollection);
        }

        loadParfumData(parfumCollections);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.linearHome:
                Close(0);
                dismiss();
                break;
            case R.id.linearSearch:
                Close(1);
                break;
            case R.id.linearFavorites:
                Close(2);
                break;
            case R.id.linearTrash:
                Close(3);
                break;

        }

    }

    private void Close(int i) {
//        Intent intent = new Intent();
//        intent.putExtra("button",i);
        ((ViewPager) getActivity().findViewById(R.id.viewpager)).setCurrentItem(i);
        Log.d(MainActivity.LOG_TAG, "button close => " + ((ViewPager) getActivity().findViewById(R.id.viewpager)).getCurrentItem());
//        getFragmentManager().getFragments().
//                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
        dismiss();
    }

    private class AdapterParfumView extends RecyclerView.Adapter<AdapterParfumView.ParfumViewHolder> {
        private ArrayList<ParfumCollection> parfumCollections;

        public AdapterParfumView(ArrayList<ParfumCollection> parfumCollections) {
            this.parfumCollections = parfumCollections;

        }

        public class ParfumViewHolder extends RecyclerView.ViewHolder {

            ImageView imageParfum;
            TextView nameParfum, priceParfum, priceFor;
            RatingBar ratingParfum;

            public ParfumViewHolder(View itemView) {

                super(itemView);

                imageParfum = (ImageView) itemView.findViewById(R.id.imageParfumCatalog);
                nameParfum = (TextView) itemView.findViewById(R.id.nameParfumCatalog);
                priceFor = (TextView) itemView.findViewById(R.id.cenaFor);
                priceParfum = (TextView) itemView.findViewById(R.id.priceParfumMin);
                ratingParfum = (RatingBar) itemView.findViewById(R.id.ratingBarParfum);
                ratingParfum.setNumStars(5);
                ratingParfum.isIndicator();

            }
        }


        @Override
        public ParfumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (state==1) {
               v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum, parent, false);
            }else{
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_parfum_grid, parent, false);
            }
            ParfumViewHolder parfumViewHolder = new ParfumViewHolder(v);
            return parfumViewHolder;
        }

        @Override
        public void onBindViewHolder(ParfumViewHolder holder, int position) {
            String url_image_brend = parfumCollections.get(position).getImageParfum();
            Picasso.with(getContext()).load(url_image_brend).into(holder.imageParfum);
            holder.nameParfum.setText(parfumCollections.get(position).getNameParfum());
            holder.priceParfum.setText(parfumCollections.get(position).getCenaParfum());
            holder.priceFor.setText(parfumCollections.get(position).getCenaFor());
            holder.ratingParfum.setRating((float) parfumCollections.get(position).getReatingParfum());
        }

        @Override
        public int getItemCount() {
            return parfumCollections.size();
        }
    }


}
