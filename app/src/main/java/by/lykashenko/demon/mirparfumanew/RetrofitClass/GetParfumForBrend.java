package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumForBrend;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceLoadParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 02.05.2017.
 */

public class GetParfumForBrend {
    private Context context;

    public void load(String sqlQueryGetParfum, final ArrayList<Brendu> brendu) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceLoadParfum interfaceLoadParfum = retrofit.create(InterfaceLoadParfum.class);
        Call<ArrayList<ParfumForBrend>> call = interfaceLoadParfum.load(sqlQueryGetParfum);
        call.enqueue(new Callback<ArrayList<ParfumForBrend>>() {
            @Override
            public void onResponse(Call<ArrayList<ParfumForBrend>> call, Response<ArrayList<ParfumForBrend>> response) {
                if (response!=null){
                    ArrayList<ParfumForBrend> parfumForBrends = response.body();
                    onLoad.onLoadParfumList(parfumForBrends,brendu);
                }else{
                    Log.e(MainActivity.LOG_TAG, "ошибка ответа: код => " + response.code() + " ,тело ошибки => " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ParfumForBrend>> call, Throwable throwable) {
                Log.e(MainActivity.LOG_TAG, "ошибка азпроса => " + throwable.getMessage());
            }
        });

    }

    public interface OnLoadParfumList {
        void onLoadParfumList(ArrayList<ParfumForBrend> parfumForBrends, ArrayList<Brendu> brend);
    }

    private OnLoadParfumList onLoad;

    public void registerOnLoadParfumList(OnLoadParfumList onLoad){
        this.onLoad=onLoad;

    }

    public GetParfumForBrend(Context context) {
        this.context = context;
    }
}
