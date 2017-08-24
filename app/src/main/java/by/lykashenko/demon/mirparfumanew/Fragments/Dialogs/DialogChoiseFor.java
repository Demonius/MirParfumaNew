package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import by.lykashenko.demon.mirparfumanew.R;
import by.lykashenko.demon.mirparfumanew.Table.ForTable;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 13.07.17.
 */

public class DialogChoiseFor extends DialogFragment {

	public interface CloseChoiseFor {
		void onCloseChoiseFor(String for_sex);
	}

	private CloseChoiseFor closeChoiseFor;

	public void regesterCloseFor(CloseChoiseFor closeChoiseFor) {
		this.closeChoiseFor = closeChoiseFor;
	}

	private ListView list;

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		getDialog().getWindow()
				.getAttributes().windowAnimations = R.style.DialogAnimation;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		getDialog().setCancelable(false);
		View v = inflater.inflate(R.layout.dialog_choise_brendu, container, false);
		TextView title = (TextView) v.findViewById(R.id.spisok);
		title.setText(getResources().getString(R.string.choiseFor));

		list = (ListView) v.findViewById(R.id.brenduList);
		ActiveAndroid.beginTransaction();

		final List<ForTable> listData = new Select().from(ForTable.class).execute();
		ActiveAndroid.setTransactionSuccessful();
		ActiveAndroid.endTransaction();

		final String[] arrayData = new String[listData.size()];

		for (int i = 0; i < listData.size(); i++) {
			arrayData[i] = listData.get(i).value;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_multiple_choice, arrayData);

		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		for (int i = 0; i < listData.size(); i++) {
			list.setItemChecked(i, listData.get(i).check);
		}

		ImageButton deselect = (ImageButton) v.findViewById(R.id.button_deselect_all);
		deselect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				for (int i = 0; i < listData.size(); i++) {
					list.setItemChecked(i, false);
				}
			}
		});

		ImageButton select = (ImageButton) v.findViewById(R.id.button_select_all);
		select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < listData.size(); i++) {
					list.setItemChecked(i, true);
				}
			}
		});

		final TextView closeButton = (TextView) v.findViewById(R.id.close);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer countChecked = list.getCheckedItemCount();
//
				String text = "";
				for (int i = 0; i < listData.size(); i++) {
					Integer countCh = 0;
//					Log.i(LOG_TAG, "position " + i + " checked =>" + list.getCheckedItemPositions().get(i));
					if (list.getCheckedItemPositions().get(i)) {
						countCh++;
						if (countChecked == countCh) {
							text = text + listData.get(i).value;
						} else
							text = text + listData.get(i).value + ", ";
						ActiveAndroid.beginTransaction();
						ForTable forTableSelected = new Select()
								.from(ForTable.class)
								.where("value like ?", listData.get(i).value)
								.executeSingle();
						forTableSelected.check = true;
						forTableSelected.save();
						ActiveAndroid.setTransactionSuccessful();
						ActiveAndroid.endTransaction();
					}
				}
//				Log.d(LOG_TAG, " checked count => " + countChecked);
				String count = "";
				if (countChecked == 0) {
					Toast.makeText(getContext(), getResources().getString(R.string.no_choise_for), Toast.LENGTH_LONG).show();
				} else {
					if (listData.size() == countChecked) {
						count = getResources().getString(R.string.all);
					} else {
//						count = getResources().getString(R.string.choise) + " " + text;
						count = text;
					}
					closeChoiseFor.onCloseChoiseFor(count);
					dismiss();
				}
			}
		});

		return v;
	}
}
