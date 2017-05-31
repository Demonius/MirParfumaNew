package by.lykashenko.demon.mirparfumanew.InterfaceRetrofit;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.IdParfum;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by demon on 24.05.2017.
 */

public interface InterfaceOnLoadIdParfum {

	@FormUrlEncoded
	@POST("api.json")
	Call<ArrayList<IdParfum>> load(@Field("SqlString") String name);
}
