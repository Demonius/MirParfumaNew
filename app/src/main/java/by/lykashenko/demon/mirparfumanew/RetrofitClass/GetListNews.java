package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.News;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceLoadNews;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by User on 31.10.2017.
 */

public class GetListNews {


    public interface OnLoadListNews {
        void onLoadListNews(ArrayList<News> listNews);
    }

    private OnLoadListNews loadListNews;


    private String sql_query;

    public GetListNews(String sql_query) {
        this.sql_query = sql_query;
    }

    public void registerCallback(OnLoadListNews loadListNews) {
        this.loadListNews = loadListNews;
    }


    public void load() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceLoadNews loadNews = retrofit.create(InterfaceLoadNews.class);
        Call<ArrayList<News>> call = loadNews.load(sql_query);

        call.enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<News>> call, @NonNull Response<ArrayList<News>> response) {
                Log.d(LOG_TAG, "load list news - >> " + response.code());
                loadListNews.onLoadListNews(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                Log.e(LOG_TAG, "error response load news ->> " + t.getMessage());
            }
        });

    }
}
