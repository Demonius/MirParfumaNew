package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.ParfumsInfo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 04.11.2017.
 */

public interface InterfaceGetParfumImfoForId {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<ParfumsInfo>> load(@Field("SqlString") String name);
}
