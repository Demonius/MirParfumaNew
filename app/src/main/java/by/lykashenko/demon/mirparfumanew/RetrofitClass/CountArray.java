package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.content.Context;
import android.util.Log;

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
 * Created by demon on 16.02.2017.
 * Class для подсчёт кол-во строк в полученных данных по sql строке
 */

public class CountArray {
    public interface OnCallBackCount {
        void onCallBackCount(Integer count, Integer state);
    }
    private OnCallBackCount myCallback;
    public void registerCallBackCount(OnCallBackCount callback) {
        this.myCallback = callback;
    }


    private Context context;
    private Integer outputCount = 0;


    public CountArray(Context context) {
        this.context = context;
    }

    public void Count(String SQL_ZAPROS, Integer state) {

        final Integer state1 = state;

//        Log.d(MainActivity.LOG_TAG, "sql zapros => " + SQL_ZAPROS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceCount interfaceCount = retrofit.create(InterfaceCount.class);
        Call<ArrayList<CountAdapter>> call = interfaceCount.getCount(SQL_ZAPROS);
        call.enqueue(new Callback<ArrayList<CountAdapter>>() {
            @Override
            public void onResponse(Call<ArrayList<CountAdapter>> call, Response<ArrayList<CountAdapter>> response) {
                if (response != null) {
//                    Log.d(MainActivity.LOG_TAG, "Количество строк в запроск = " + response.body().get(0).getCount());
                    outputCount = response.body().get(0).getCount();
                    myCallback.onCallBackCount(outputCount, state1);

                }
            }
            @Override
            public void onFailure(Call<ArrayList<CountAdapter>> call, Throwable t) {
                Log.d(MainActivity.LOG_TAG, "ошибка запроса " + t.getMessage());
            }
        });

    }
}
