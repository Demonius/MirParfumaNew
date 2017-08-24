package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.Podbor;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceGetPodbor;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 14.07.2017.
 */

public class GetPodborParfumId {

	public interface LoadPadborParfumId{
		void onLoadPadborParfumId(ArrayList<Podbor> listIdParfum);
	}

	private LoadPadborParfumId loadPadborParfumId;

	public void registerLoad(LoadPadborParfumId loadPadborParfumId){
		this.loadPadborParfumId=loadPadborParfumId;
	}


	public void load(String sql_podbor) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MainActivity.URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		InterfaceGetPodbor interfaceGetPodbor = retrofit.create(InterfaceGetPodbor.class);
		Call<ArrayList<Podbor>> call = interfaceGetPodbor.getParfumId(sql_podbor);
		call.enqueue(new Callback<ArrayList<Podbor>>() {
			@Override
			public void onResponse(Call<ArrayList<Podbor>> call, Response<ArrayList<Podbor>> response) {
				if (response!=null){
					ArrayList<Podbor> list = response.body();
					loadPadborParfumId.onLoadPadborParfumId(list);
				}
			}

			@Override
			public void onFailure(Call<ArrayList<Podbor>> call, Throwable t) {

			}
		});
	}
}
