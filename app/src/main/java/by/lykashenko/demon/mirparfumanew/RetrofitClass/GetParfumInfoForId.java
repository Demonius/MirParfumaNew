package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumsInfo;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceGetParfumImfoForId;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import by.lykashenko.demon.mirparfumanew.Table.Parfums;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by User on 04.11.2017.
 */

public class GetParfumInfoForId {

    public interface LoadData{
        void  loadData(Boolean loadData);
    }

    private LoadData loadData;

    public void registerLoadData(LoadData loadData){
        this.loadData = loadData;
    }

    public void load(String sql) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceGetParfumImfoForId interfaceGetParfumImfoForId = retrofit.create(InterfaceGetParfumImfoForId.class);
        Call<ArrayList<ParfumsInfo>> call = interfaceGetParfumImfoForId.load(sql);
        call.enqueue(new Callback<ArrayList<ParfumsInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ParfumsInfo>> call, Response<ArrayList<ParfumsInfo>> response) {
                if (response.code()==200){
                    Log.d(LOG_TAG,"response count parfum info ->>> "+response.body().size());
                    for (ParfumsInfo parfumInfo: response.body()) {
                        Parfums parfum = new Parfums();
                        parfum.tmplvarid = parfumInfo.getTmplvarid();
                        parfum.contentid = parfumInfo.getContentid();
                        parfum.infoparfum= parfumInfo.getValueInfo();
                        parfum.save();
                    }
                    loadData.loadData(true);
                }else{
                    Log.e(LOG_TAG,"error load data for parfum info --- code aswer ==>> "+response.code()+", nessage answer =>> "+response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ParfumsInfo>> call, Throwable t) {
                Log.e(LOG_TAG,"error load data for parfum info --- answer ==>> "+t.getMessage());
            }
        });
    }
}
