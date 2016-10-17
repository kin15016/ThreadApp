package com.example.threadapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private static final String LOG_TAG = "MainActivity";
  private static final String FILENAME = "numbers.txt";

  /** Create the numbers.txt file in the background. */
  private class CreateFileTask extends AsyncTask<Void, Integer, Void> {
    @Override
    protected Void doInBackground(Void... params) {
      // create a file with the numbers 1..10, each on its own line.
      File file = new File(getApplicationContext().getFilesDir(), FILENAME);

      PrintWriter writer = null;
      try {
        writer = new PrintWriter(file);
        for (int i = 1; i <= 10; i++) {
          writer.println(i);
          Thread.sleep(250);
          publishProgress(i * 10);
        }
      }
      catch (FileNotFoundException | InterruptedException ex) {
        Log.e(LOG_TAG, "createFile:" + ex.toString());
      }
      finally {
        if (writer != null) {
          writer.close();
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
      // clear progress bar
      ProgressBar pbFileIO = (ProgressBar)findViewById(R.id.pbFileIO);
      pbFileIO.setProgress(0);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
      // update progress bar
      ProgressBar pbFileIO = (ProgressBar)findViewById(R.id.pbFileIO);
      pbFileIO.setProgress(progress[0]);
    }
  }

  /** Loads the numbers.txt file in the background. */
  private class LoadFileTask extends AsyncTask<Void, Integer, List<String>> {
    @Override
    protected List<String> doInBackground(Void... params) {
      // load the file of numbers, line-by-line, into an array.
      File file = new File(getApplicationContext().getFilesDir(), FILENAME);
      BufferedReader reader = null;
      List<String> list = new ArrayList<>();
      try {
        reader = new BufferedReader(new FileReader(file));
        String s;
        int progress = 0;
        while ((s = reader.readLine()) != null) {
          list.add(s);
          Thread.sleep(250);
          progress += 10;
          publishProgress(progress);
        }
      }
      catch (IOException | InterruptedException ex) {
        Log.e(LOG_TAG, "loadFile:" + ex.toString());
      }
      finally {
        if (reader != null) {
          try {
            reader.close();
          }
          catch(IOException ex) {
            // do nothing
          }
        }
      }
      return list;
    }

    @Override
    protected void onPostExecute(List<String> list) {
      // update the ListView using the list of numbers
      ArrayAdapter<String> aaNumbers = new ArrayAdapter<>(MainActivity.this,
          android.R.layout.simple_list_item_1, list);
      ListView lvNumbers = (ListView)findViewById(R.id.lvNumbers);
      lvNumbers.setAdapter(aaNumbers);

      // clear progress bar
      ProgressBar pbFileIO = (ProgressBar)findViewById(R.id.pbFileIO);
      pbFileIO.setProgress(0);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
      // update the progress bar
      ProgressBar pbFileIO = (ProgressBar)findViewById(R.id.pbFileIO);
      pbFileIO.setProgress(progress[0]);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void createFile(View view) {
    // start the background file creation task
    CreateFileTask task = new CreateFileTask();
    task.execute();
  }

  public void loadFile(View view) {
    // start the background file loading task
    LoadFileTask task = new LoadFileTask();
    task.execute();
  }

  public void clearFile(View view) {
    // clear the displayed list of numbers
    ListView lvNumbers = (ListView)findViewById(R.id.lvNumbers);
    ArrayAdapter aaNumbers = (ArrayAdapter)lvNumbers.getAdapter();
    if (aaNumbers != null) {
      aaNumbers.clear();
      aaNumbers.notifyDataSetChanged();
    }
  }
}
