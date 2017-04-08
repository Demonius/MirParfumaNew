package by.lykashenko.demon.mirparfumanew.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import by.lykashenko.demon.mirparfumanew.R;

/**
 *
 * Created by demon on 08.04.2017.
 */

public class DialogExitError extends DialogFragment implements OnClickListener {

 public Dialog onCreateDialog(Bundle savedInstanceState) {

  AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
    .setTitle(R.string.title_no_connection)
    .setMessage(R.string.messge_no_connection)
    .setPositiveButton(R.string.positive_button, this);
  return adb.create();
 }

 @Override
 public void onClick(DialogInterface dialog, int which) {
  switch (which){
   case Dialog.BUTTON_POSITIVE:
    getActivity().finish();
    break;

  }
 }
// public void onDismiss(DialogInterface dialog) {
//  super.onDismiss(dialog);
////  getActivity().finish();
// }
//
// public void onCancel(DialogInterface dialog) {
////  super.onCancel(dialog);
//  getActivity().finish();
// }
}
