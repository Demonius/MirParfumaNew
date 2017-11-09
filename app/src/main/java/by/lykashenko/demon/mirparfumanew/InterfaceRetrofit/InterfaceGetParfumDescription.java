package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.DescriptionParfum;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 09.11.2017.
 */

public interface InterfaceGetParfumDescription {
    @FormUrlEncoded
    @POST("api.json")
    Call<ArrayList<DescriptionParfum>> getDescripton(@Field("SqlString") String name);
}
