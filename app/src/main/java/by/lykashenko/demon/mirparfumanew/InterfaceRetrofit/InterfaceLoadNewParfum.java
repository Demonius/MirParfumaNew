package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.NewParfum;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 05.04.2017.
 */

public interface InterfaceLoadNewParfum {
 @FormUrlEncoded
 @POST("api.json")
 Call<ArrayList<NewParfum>> load(@Field("SqlString") String name);
}
