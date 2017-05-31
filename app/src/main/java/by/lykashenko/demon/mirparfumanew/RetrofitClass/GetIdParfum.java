package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.IdParfum;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfaceOnLoadIdParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 24.05.2017.
 */

public class GetIdParfum {

	public interface OnLoadIdParfum{
		void onLoadIdParfum(ArrayList<IdParfum> idParfum) throws IOException;
	}

	private OnLoadIdParfum onLoadIdParfum;

	public void registerOnLoadIdParfum(OnLoadIdParfum onLoadIdParfum){
		this.onLoadIdParfum=onLoadIdParfum;
	}


	public void load(String sql_parfum_from_brend) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MainActivity.URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		InterfaceOnLoadIdParfum loadIdParfum = retrofit.create(InterfaceOnLoadIdParfum.class);
		Call<ArrayList<IdParfum>> call = loadIdParfum.load(sql_parfum_from_brend);
		call.enqueue(new Callback<ArrayList<IdParfum>>() {
			@Override
			public void onResponse(Call<ArrayList<IdParfum>> call, Response<ArrayList<IdParfum>> response) {
				if (response!=null){
					Log.i(MainActivity.LOG_TAG,"idparfum answer code => "+response.code()+"| message => "+response.message());
					if (response.code()==200) {
						try {
							onLoadIdParfum.onLoadIdParfum(response.body());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
						Log.e(MainActivity.LOG_TAG,"error answer server via load id parfum");
					}
				}
			}

			@Override
			public void onFailure(Call<ArrayList<IdParfum>> call, Throwable t) {
				Log.e(MainActivity.LOG_TAG,"error answer with error_text => "+t.getMessage());
			}
		});

	}
}
