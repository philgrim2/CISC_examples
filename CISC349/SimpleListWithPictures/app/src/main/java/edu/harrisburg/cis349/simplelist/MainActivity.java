package edu.harrisburg.cis349.simplelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

        ArrayList<User> arrayOfUsers = new ArrayList<>();
        arrayOfUsers.add(new User("Eve", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("John", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Mark", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Michael", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Adam", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Mary", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Olivia", "777-777-7777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));


        // Create the adapter to convert the array to views
        UserAdapter adapter = new UserAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(adapter);
    }
}