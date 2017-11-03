package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.Adapters.OtzuvuForMirParfuma;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 01.11.2017.
 */

public interface InterfaceLoadOtzuvu {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<OtzuvuForMirParfuma>> load(@Field("SqlString") String sql);
}
