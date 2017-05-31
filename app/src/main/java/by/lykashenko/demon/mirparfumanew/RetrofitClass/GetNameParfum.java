package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import java.io.IOException;
import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.Adapters.ParfumNamePrice;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceOnGetNameParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 24.05.2017.
 */

public class GetNameParfum {



	public interface OnGetNameParfum{
		void onGetNameParfum(ArrayList<ParfumNamePrice> parfumNamePrice);
	}

	private OnGetNameParfum onGetNameParfumLoad;

	public void registeringOnGetNameParfum(OnGetNameParfum onGetNameParfumLoad){
		this.onGetNameParfumLoad=onGetNameParfumLoad;
	}

	public void load(String sql_get_name_and_price_parfum) {

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MainActivity.URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		InterfaceOnGetNameParfum loadNameParfum = retrofit.create(InterfaceOnGetNameParfum.class);
		Call<ArrayList<ParfumNamePrice>> call = loadNameParfum.load(sql_get_name_and_price_parfum);
		call.enqueue(new Callback<ArrayList<ParfumNamePrice>>() {
					@Override
					public void onResponse(Call<ArrayList<ParfumNamePrice>> call, Response<ArrayList<ParfumNamePrice>> response) {
						if (response!=null){
							onGetNameParfumLoad.onGetNameParfum(response.body());
						}
					}

					@Override
					public void onFailure(Call<ArrayList<ParfumNamePrice>> call, Throwable t) {

					}
				});

	}
}
