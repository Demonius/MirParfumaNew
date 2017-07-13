package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.Adapters.Otzuvu;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Admin on 03.07.17.
 */

public interface InterfaceGetOtzuvu {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<Otzuvu>> getOtzuvu(@Field("SqlString") String name);
}
