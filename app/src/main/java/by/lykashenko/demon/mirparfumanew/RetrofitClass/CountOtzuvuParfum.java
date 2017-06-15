package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.CountAdapter;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceCount;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 12.06.2017.
 */

public class CountOtzuvuParfum {



    public interface CountLoad{
        void onCountLoad(String count);
    }

    private CountLoad countLoad;

    public void registerCountLoad(CountLoad countLoad){
        this.countLoad=countLoad;
    }

    public void load(String sql) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceCount interfaceCount = retrofit.create(InterfaceCount.class);
        Call<ArrayList<CountAdapter>> call = interfaceCount.getCount(sql);
        call.enqueue(new Callback<ArrayList<CountAdapter>>() {
            @Override
            public void onResponse(Call<ArrayList<CountAdapter>> call, Response<ArrayList<CountAdapter>> response) {
                if (response!=null){
                    countLoad.onCountLoad(response.body().get(0).getCount());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CountAdapter>> call, Throwable t) {

            }
        });
    }

}
