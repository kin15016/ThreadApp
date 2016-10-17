package com.example.threadapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private static final String LOG_TAG = "MainActivity";
  private static final String FILENAME = "numbers.txt";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void createFile(View view) {
    File file = new File(getApplicationContext().getFilesDir(), FILENAME);

    PrintWriter writer = null;
    try {
      writer = new PrintWriter(file);
      for (int i = 1; i <= 10; i++) {
        writer.println(i);
        Thread.sleep(250);
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
  }

  public void loadFile(View view) {
    // load the file of numbers, line-by-line, into an array.
    File file = new File(getApplicationContext().getFilesDir(), FILENAME);
    BufferedReader reader = null;
    ArrayList<String> list = new ArrayList<>();
    try {
      reader = new BufferedReader(new FileReader(file));
      String s;
      while ((s = reader.readLine()) != null) {
        list.add(s);
        Thread.sleep(250);
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

    // update the ListView using the list of numbers
    ArrayAdapter<String> aaNumbers = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
        list);
    ListView lvNumbers = (ListView)findViewById(R.id.lvNumbers);
    lvNumbers.setAdapter(aaNumbers);
  }

  public void clearFile(View view) {
    ListView lvNumbers = (ListView)findViewById(R.id.lvNumbers);
    ArrayAdapter aaNumbers = (ArrayAdapter)lvNumbers.getAdapter();
    if (aaNumbers != null) {
      aaNumbers.clear();
      aaNumbers.notifyDataSetChanged();
    }
  }
}
