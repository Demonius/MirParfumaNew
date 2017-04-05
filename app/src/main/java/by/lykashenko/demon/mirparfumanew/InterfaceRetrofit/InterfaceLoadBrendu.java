package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;


import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Brendu;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 03.04.2017.
 */

public interface InterfaceLoadBrendu {

 @FormUrlEncoded
 @POST("api.json")
 Call<ArrayList<Brendu>> load(@Field("SqlString") String name);
}
