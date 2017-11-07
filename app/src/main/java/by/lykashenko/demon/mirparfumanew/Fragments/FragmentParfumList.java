package by.lykashenko.demon.mirparfumanew.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.IdParfum;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumForBrend;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Podbor;
import by.lykashenko.demon.mirparfumanew.Adapters.AdapterParfumView;
import by.lykashenko.demon.mirparfumanew.Adapters.ParfumCollection;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Ratting;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetIdParfum;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetParfumForBrend;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetPodborParfumId;
import by.lykashenko.demon.mirparfumanew.RetrofitClass.GetRattingPrfum;
import by.lykashenko.demon.mirparfumanew.Table.BrenduAll;
import by.lykashenko.demon.mirparfumanew.Table.Country;
import by.lykashenko.demon.mirparfumanew.Table.ForTable;
import by.lykashenko.demon.mirparfumanew.Table.Nota;
import by.lykashenko.demon.mirparfumanew.Table.Semeistvo;
import by.lykashenko.demon.mirparfumanew.Table.Type;
import by.lykashenko.demon.mirparfumanew.Table.Year;

import static by.lykashenko.demon.mirparfumanew.BrendActivity.titleToolbar;
import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 06.06.17.
 */

public class FragmentParfumList extends Fragment implements GetIdParfum.OnLoadIdParfum, GetRattingPrfum.OnGetRattingParfum, AdapterParfumView.ClickParfum, GetPodborParfumId.LoadPadborParfumId, GetParfumForBrend.OnLoadParfumList, AdapterParfumView.UpdateNewData {


    @Override
    public void onUpdateNewData(final Integer fromPosition, final Integer count) {
        viewParfum.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRangeInserted(fromPosition, count);
            }
        });
    }

    public interface GetInfoOfParfum {
        void onGetInfoOfParfum(Bundle bundle);
    }

    private GetInfoOfParfum getInfoOfParfum;

    public void registrationGetInfoOfParfum(GetInfoOfParfum getInfoOfParfum) {
        this.getInfoOfParfum = getInfoOfParfum;
    }

    private Bundle bundle = new Bundle();
    private String nameBrendList;
    private ArrayList<ParfumCollection> parfumCollections;
    private String SQL_PARFUM_FROM_BREND = "";
    private String Sql_Get_Name_And_Price_Parfum = "";

    private AdapterParfumView adapter;
    private List<BrenduAll> idParfumList = new ArrayList<>();
    private RecyclerView viewParfum;
    private Integer state = 1;
    private String sexString = "";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parfum_list, container, false);
        if (bundle.size() > 0) {
            Log.d(LOG_TAG, "parfumlist now save data");
            parfumCollections = bundle.getParcelableArrayList("adapter");
            state = bundle.getInt("state");
            bundle.clear();
        } else {
            Integer podbor = getArguments().getInt("podbor", 0);

            if (podbor == 1) {

                ActiveAndroid.beginTransaction();

                List<ForTable> forSexList = new Select().from(ForTable.class).where("check_for = ?", true).execute();
                List<Type> typeList = new Select().from(Type.class).where("check_type = ?", true).execute();
                List<Year> yearList = new Select().from(Year.class).where("check_year = ?", true).execute();
                List<Semeistvo> semeistvoList = new Select().from(Semeistvo.class).where("check_semeistvo = ?", true).execute();
                List<Nota> notaList = new Select().from(Nota.class).where("check_nota = ?", true).execute();
                List<Country> countryList = new Select().from(Country.class).where("check_country = ?", true).execute();

                String arraySex = "";
                String arrayType = "";
                String arrayYear = "";
                String arraySemeistvo = "";
                String arrayNota = "";
                String arrayCountry = "";

                for (int i = 0; i < forSexList.size(); i++) {
                    if (i == forSexList.size() - 1) {
                        arraySex = arraySex + " '" + forSexList.get(i).value + "'";
                    } else {
                        arraySex = arraySex + " '" + forSexList.get(i).value + "',";

                    }
                }
                for (int i = 0; i < typeList.size(); i++) {
                    if (i == typeList.size() - 1) {
                        arrayType = arrayType + " '" + typeList.get(i).value + "'";
                    } else {
                        arrayType = arrayType + " '" + typeList.get(i).value + "',";
                    }
                }
                for (int i = 0; i < yearList.size(); i++) {
                    if (i == yearList.size() - 1) {
                        arrayYear = arrayYear + yearList.get(i).value;
                    } else {
                        arrayYear = arrayYear + yearList.get(i).value + ", ";

                    }
                }
                for (int i = 0; i < semeistvoList.size(); i++) {
                    if (i == semeistvoList.size() - 1) {
                        arraySemeistvo = arraySemeistvo + " '" + semeistvoList.get(i).value + "'";
                    } else {
                        arraySemeistvo = arraySemeistvo + " '" + semeistvoList.get(i).value + "',";

                    }
                }
                for (int i = 0; i < notaList.size(); i++) {
                    if (i == notaList.size() - 1) {
                        arrayNota = arrayNota + " '" + notaList.get(i).value + "'";
                    } else {
                        arrayNota = arrayNota + " '" + notaList.get(i).value + "',";

                    }
                }
                for (int i = 0; i < countryList.size(); i++) {
                    if (i == countryList.size() - 1) {
                        arrayCountry = arrayCountry + " '" + countryList.get(i).value + "'";
                    } else {
                        arrayCountry = arrayCountry + " '" + countryList.get(i).value + "',";

                    }
                }


                String SQL_PODBOR = "SELECT val.contentid " +
                        "FROM (Select val.contentid FROM (Select val.contentid " +
                        "FROM (Select val.contentid FROM (Select val.contentid " +
                        "From (SELECT contentid FROM modx_site_tmplvar_contentvalues where tmplvarid = 67 and value in(" + arraySex + "))as id, " +
                        "modx_site_tmplvar_contentvalues as val where val.tmplvarid=66 and val.contentid=id.contentid and value in(" + arrayType + ")) as id, " +
                        "modx_site_tmplvar_contentvalues as val WHERE val.contentid=id.contentid and val.tmplvarid=68 and val.value in(" + arrayYear + "))as id, " +
                        "modx_site_tmplvar_contentvalues as val where val.contentid=id.contentid and val.tmplvarid=69 and " +
                        "substring_index(val.value,'||',1) in(" + arraySemeistvo + "))as id, " +
                        "modx_site_tmplvar_contentvalues as val where val.contentid=id.contentid and val.tmplvarid = 70 and " +
                        "substring_index(val.value,'||',1) in(" + arrayNota + "))as id, " +
                        "modx_site_tmplvar_contentvalues as val where val.contentid=id.contentid and val.tmplvarid = 73 and value in(" + arrayCountry + ")";

                Log.d(LOG_TAG, SQL_PODBOR);
                GetPodborParfumId getPodborParfumId = new GetPodborParfumId();
                getPodborParfumId.registerLoad(this);
                getPodborParfumId.load(SQL_PODBOR);


            } else {
                String id = getArguments().getString("id");
                Integer sex = getArguments().getInt("sex");
                Log.i(LOG_TAG, "id brend => " + id);

                nameBrendList = getArguments().getString("name");
                titleToolbar.setText(nameBrendList);
                Log.i(LOG_TAG, "name brend => " + nameBrendList);


                viewParfum = (RecyclerView) v.findViewById(R.id.listParfum);
                viewParfum.setHasFixedSize(true);
                LinearLayoutManager lv = new LinearLayoutManager(getContext());
                viewParfum.setLayoutManager(lv);

                //запрос на id, название, лого и цена парфюма для выбранного бренда

                ActiveAndroid.beginTransaction();
                switch (sex) {
                    case 0:
                        idParfumList = new Select().from(BrenduAll.class).where("brend_id = ?", id).orderBy("parfum_id ASC").execute();
                        sexString = "";
                        break;
                    case 1:
                        idParfumList = new Select().from(BrenduAll.class).where("brend_id = ?", id).where("sex like \'мужчин\'").orderBy("parfum_id ASC").execute();
                        sexString = "\'мужчин\'";
                        break;
                    case 2:
                        idParfumList = new Select().from(BrenduAll.class).where("brend_id = ?", id).where("sex like \'женщин\'").orderBy("parfum_id ASC").execute();
                        sexString = "\'женщин\'";
                        break;
                    case 3:
                        idParfumList = new Select().from(BrenduAll.class).where("brend_id = ?", id).where("sex like \'унисекс\'").orderBy("parfum_id ASC").execute();
                        sexString = "\'унисекс\'";
                        break;

                }
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
                Log.i(LOG_TAG, "count id parfum from bd ===>>" + idParfumList.size());

// СПИСОК ИД ПАРФЮМЕРИИ ИЗ БАЗЫ ПОЛУЧЕН. НЕОБХОИМО ЗАПРОСИТЬ С СЕРВЕРА ИНФУ ПО ПАРФЮМЕРИИ!!!!!!

                adapter = new AdapterParfumView(idParfumList, getContext(), state, sexString);
                adapter.registerClickParfum(this);
                adapter.registerUpdateNewData(this);
                viewParfum.setAdapter(adapter);
            }

        }
        ImageButton changeAdapter = (ImageButton) v.findViewById(R.id.changeAdapter);
        changeAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    state = 2;
                    GridLayoutManager lv = new GridLayoutManager(getContext(), 2);
                    viewParfum.setLayoutManager(lv);
                    adapter.setState(state);
                    viewParfum.setAdapter(adapter);

                } else {
                    state = 1;
                    LinearLayoutManager lv = new LinearLayoutManager(getContext());
                    viewParfum.setLayoutManager(lv);
                    adapter.setState(state);
                    viewParfum.setAdapter(adapter);

                }
            }
        });
        return v;
    }

    @Override
    public void onLoadParfumList(ArrayList<ParfumForBrend> parfumForBrends, ArrayList<Brendu> brend) {

    }

    @Override
    public void onClickParfum(Bundle bundle) {
        getInfoOfParfum.onGetInfoOfParfum(bundle);

    }

    @Override
    public void onLoadPadborParfumId(ArrayList<Podbor> listIdParfum) {

        String listIds = "";
        if (!listIdParfum.isEmpty()) {

            for (int i = 0; i < listIdParfum.size(); i++) {
                if (i == listIdParfum.size() - 1) {
                    listIds = listIds + " '" + listIdParfum.get(i).getId() + "'";
                } else {
                    listIds = listIds + " '" + listIdParfum.get(i).getId() + "',";
                }
            }
            SQL_PARFUM_FROM_BREND = "SELECT val.contentid, val.value, val.tmplvarid, ltrim(substring(con.pagetitle,(char_length(substring_index(con.pagetitle,' ',2))+1)))as pagetitle " +
                    "FROM modx_site_content as con, modx_site_tmplvar_contentvalues as val " +
                    "where val.contentid in(" + listIds + ") and con.id in(" + listIds + ") and val.tmplvarid in (76 , 1) ORDER BY `val`.`contentid` ASC";

            Sql_Get_Name_And_Price_Parfum = "SELECT thread, avg(SUBSTRING(properties,16,1)) as rating FROM modx_tickets_comments where thread in(" + listIds + ") group by thread";
        }
    }

    //
    @Override
    public void onLoadIdParfum(ArrayList<IdParfum> idParfum) throws IOException {

//sql запросы для получения рейтинг parfum


        GetRattingPrfum getRattingParfum = new GetRattingPrfum();
        getRattingParfum.registerGetRattingParfum(this);
        getRattingParfum.load(Sql_Get_Name_And_Price_Parfum);


    }

//    private void loadParfumData(ArrayList<ParfumCollection> parfumCollections) {
//
//
//        AdapterParfumView adapter = new AdapterParfumView(parfumCollections, getContext(), state);
//        adapter.registerClickParfum(this);
//        viewParfum.setAdapter(adapter);
//
//    }


    @Override
    public void onGetRattingParfum(HashMap<String, Integer> answer) {

//        parfumCollections = new ArrayList<>();
//        int i = 0;
//        while ((i < idNameParfum.size()) && (idNameParfum.size() - i > 1)) {
//
//            ParfumCollection parfumCollection = new ParfumCollection();
////id
//            parfumCollection.setIdParfum(idNameParfum.get(i).getContentid());
//            //image
//            String pathImage = MainActivity.URL + idNameParfum.get(i).getValue();
//            parfumCollection.setImageParfum(pathImage);
//            //rating
//            Integer y = 0;
//            for (Ratting ratting : ratingsParfum) {
//
//                if (ratting.getThread().equals(idNameParfum.get(i).getContentid())) {
//                    parfumCollection.setReatingParfum(ratting.getRating());
//                    y = 1;
//                    break;
//                }
//            }
//            if (y == 0) {
//                parfumCollection.setReatingParfum(0);
//            }
//
//            //name
//
//            String name = idNameParfum.get(i).getPagetitle();
//            Log.i(MainActivity.LOG_TAG, "id parfum => " + idNameParfum.get(i).getContentid());
//            parfumCollection.setNameParfum(name);
//
//
//            //price
//
////			String priceOne ="От "+ namePriceOne.split("==")[1]+" руб.";
//            try {
////                Log.d(LOG_TAG, "i => " + i);
//                String id = idNameParfum.get(i).getContentid();
////                Log.d(LOG_TAG, "id this position => " + id);
//                String id_next = idNameParfum.get(i + 1).getContentid();
////                Log.d(LOG_TAG, "id next position => " + id_next);
//
//                if (!id.equals(id_next)) {
//                    String priceFull = getResources().getString(R.string.no_parfum);
//                    parfumCollection.setCenaParfum(priceFull);
//                    i = i + 1;
////                    Log.d(LOG_TAG, "------------------------");
//                } else {
//
////                    Log.d(LOG_TAG, "true data");
//                    String priceOne = idNameParfum.get(i + 1).getValue();
////                    Log.d(LOG_TAG, "stroka s cenoi => " + priceOne);
//                    JSONArray json = null;
//                    try {
//                        json = new JSONArray(priceOne);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Integer price = json.getJSONObject(0).getInt("price");
//                        String priceFor = json.getJSONObject(0).getString("title");
////                        Log.i(MainActivity.LOG_TAG, "price parfum => " + price + "; ml parfum => " + priceFor);
//                        String priceFull = "От " + Integer.toString(price) + " руб.";
//                        String priceForFull = "Ваша цена за " + priceFor;
//                        parfumCollection.setCenaParfum(priceFull);
//                        parfumCollection.setCenaFor(priceForFull);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    i = i + 2;
//                }
//                parfumCollections.add(parfumCollection);
//            } catch (IndexOutOfBoundsException e) {
//                String priceFull = "ОШИБКА ИНДЕКСА";
//                parfumCollection.setCenaParfum(priceFull);
//            }


//        }
//        loadParfumData(parfumCollections);
    }
}