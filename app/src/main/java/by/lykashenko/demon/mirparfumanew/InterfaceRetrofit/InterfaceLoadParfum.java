package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumForBrend;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 02.05.2017.
 */

public interface InterfaceLoadParfum {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<ParfumForBrend>> load(@Field("SqlString") String name);
}
