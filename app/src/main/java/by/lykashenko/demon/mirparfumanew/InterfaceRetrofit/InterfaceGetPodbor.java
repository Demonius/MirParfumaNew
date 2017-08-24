package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Podbor;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 14.07.2017.
 */

public interface InterfaceGetPodbor {
	@FormUrlEncoded
	@POST("api.json")
	Call<ArrayList<Podbor>> getParfumId(@Field("SqlString") String name);
}
