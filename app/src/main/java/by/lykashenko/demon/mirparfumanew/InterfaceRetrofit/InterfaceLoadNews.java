package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.News;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 31.10.2017.
 */

public interface InterfaceLoadNews {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<News>> load(@Field("SqlString") String sql);
}
