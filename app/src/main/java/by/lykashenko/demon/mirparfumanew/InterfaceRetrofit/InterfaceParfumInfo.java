package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Parfum;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 09.06.2017.
 */

public interface InterfaceParfumInfo {

    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<Parfum>> load(@Field("SqlString") String name);
}
