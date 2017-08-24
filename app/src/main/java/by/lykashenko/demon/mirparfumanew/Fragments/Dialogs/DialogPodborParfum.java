package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import by.lykashenko.demon.mirparfumanew.R;

import static by.lykashenko.demon.mirparfumanew.MainActivity.LOG_TAG;

/**
 * Created by Admin on 15.06.17.
 */

public class DialogPodborParfum extends DialogFragment implements DialogChoiseBrendu.CloseChoiseBrendu {

    private DialogPodborParfum context;
    private TextView countBrend;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_podbor, container, false);

        getDialog().setTitle(R.string.podbor);

        context = this;

        CrystalRangeSeekbar rangePrice = (CrystalRangeSeekbar) v.findViewById(R.id.rangePrice);

        final TextView textPrice = (TextView) v.findViewById(R.id.priceFull);

        rangePrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                String text = minValue + " - " + maxValue;
                textPrice.setText(text);
            }
        });
        rangePrice.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d(LOG_TAG, "minValue => " + minValue + "; maxValue => " + maxValue);
            }
        });

        TextView textBrendu = (TextView) v.findViewById(R.id.textBrendu);
        textBrendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChoiseBrendu choiseBrendu = new DialogChoiseBrendu();
                choiseBrendu.register(context);
                choiseBrendu.show(getFragmentManager(), "choiseBrendu");
            }
        });

        countBrend = (TextView) v.findViewById(R.id.text_properties_brendu);
        return v;
    }

    @Override
    public void onCloseChoiseBrendu(String count) {
        countBrend.setText(count);
    }
}
