package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Ratting;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 24.05.2017.
 */

public interface InterfeceGetRatingParfum {

	@FormUrlEncoded
	@POST("api.json")
	Call<ArrayList<Ratting>> load(@Field("SqlString") String name);

}
