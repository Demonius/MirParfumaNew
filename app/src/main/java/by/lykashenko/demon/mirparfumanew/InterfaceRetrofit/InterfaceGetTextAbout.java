package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.TextAbout;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 12.04.2017.
 */

public interface InterfaceGetTextAbout {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<TextAbout>> getText(@Field("SqlString") String name);
}
