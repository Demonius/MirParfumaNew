package by.lykashenko.demon.mirparfumanew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {


 private Button btn;
 private ProgressBar progressBarSplash;
 private MyTask myTask;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_start);

  myTask = new MyTask();
  myTask.execute();
//  btn = (Button) findViewById(R.id.button2);
//
//
//  btn.setOnClickListener(new View.OnClickListener() {
//   @Override
//   public void onClick(View v) {
//
//   }
//  });


 }

 private class MyTask extends AsyncTask<Void, Void, Void> {
  @Override
  protected Void doInBackground(Void... voids) {

   try {
    TimeUnit.SECONDS.sleep(3);
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
   return null;
  }

  @Override
  protected void onPostExecute(Void result) {
   super.onPostExecute(result);
   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
   startActivity(intent);
   finish();
  }
 }
}
