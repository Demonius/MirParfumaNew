package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.CountAdapter;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 16.02.2017.
 */

public interface InterfaceCount {

 @FormUrlEncoded
 @POST("api.json")
 Call<ArrayList<CountAdapter>> getCount(@Field("SqlString") String name);
}
