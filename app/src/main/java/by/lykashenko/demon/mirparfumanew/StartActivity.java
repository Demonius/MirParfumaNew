package by.lykashenko.demon.mirparfumanew;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {


 private MyTask myTask;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_start);

  myTask = new MyTask();
  myTask.execute();

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
