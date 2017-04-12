package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.TextAbout;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceGetTextAbout;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 12.04.2017.
 */

public class GetTextAbout {

    public interface OnLoad{
        void onLoad(String content);
    }

    private OnLoad onLoadContent;

    public void registerOnLoad(OnLoad onLoadContent){
        this.onLoadContent = onLoadContent;
    }



    public void load(String sqlQuery){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceGetTextAbout interfaceGetTextAbout = retrofit.create(InterfaceGetTextAbout.class);
        Call<ArrayList<TextAbout>> call = interfaceGetTextAbout.getText(sqlQuery);
        call.enqueue(new Callback<ArrayList<TextAbout>>() {
            @Override
            public void onResponse(Call<ArrayList<TextAbout>> call, Response<ArrayList<TextAbout>> response) {
                if (response!=null){
                    Log.e(MainActivity.LOG_TAG,"response => "+response.body().get(0).getContent());
                    onLoadContent.onLoad(response.body().get(0).getContent());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TextAbout>> call, Throwable throwable) {

            }
        });

    }
}
