package edu.harrisburg.cis349.simplelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ListView listView;
    TextView textView;
    String[] listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LayoutInflater factory = getLayoutInflater();
        final View myView = factory.inflate(R.layout.my_list, null);
        textView = (TextView) myView.findViewById(R.id.textView);

        listView = findViewById(R.id.listView);
        listItem = getResources().getStringArray(R.array.array_technology);

        for (String s : listItem) {
            Log.d(TAG, s);
        }
    }
}