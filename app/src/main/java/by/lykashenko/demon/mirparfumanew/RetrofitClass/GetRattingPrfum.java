package by.lykashenko.demon.mirparfumanew.RetrofitClass;

import java.io.IOException;
import java.util.ArrayList;

import by.lykashenko.demon.mirparfumanew.AdapterRetrofit.IdParfum;
import by.lykashenko.demon.mirparfumanew.Adapters.Ratting;
import by.lykashenko.demon.mirparfumanew.InterfaceRetrofit.InterfeceGetRatingParfum;
import by.lykashenko.demon.mirparfumanew.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by demon on 24.05.2017.
 */

public class GetRattingPrfum {



	public interface OnGetRattingParfum {
		void onGetRattingParfum(ArrayList<IdParfum> idNameParfum, ArrayList<Ratting> ratingsParfum);
	}

	private OnGetRattingParfum getRattingParfum;

	public void registerGetRattingParfum(OnGetRattingParfum getRattingParfum) {
		this.getRattingParfum = getRattingParfum;
	}

	public void load(String sql_get_rating_parfum, final ArrayList<IdParfum> idParfum) throws IOException {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MainActivity.URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		InterfeceGetRatingParfum loadRatingParfum = retrofit.create(InterfeceGetRatingParfum.class);
		Call<ArrayList<Ratting>> call = loadRatingParfum.load(sql_get_rating_parfum);

		call.enqueue(new Callback<ArrayList<Ratting>>() {
			@Override
			public void onResponse(Call<ArrayList<Ratting>> call, Response<ArrayList<Ratting>> response) {
				if (response != null){
					getRattingParfum.onGetRattingParfum(idParfum,response.body());
				}
			}

			@Override
			public void onFailure(Call<ArrayList<Ratting>> call, Throwable t) {

			}
		});
	}
}

