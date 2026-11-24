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
        arrayOfUsers.add(new User("Eve", "717-777-7771", "https://miro.medium.com/v2/resize:fit:1080/0*A7MUqyCLvZDcHkfM.jpg"));
        arrayOfUsers.add(new User("John", "717-777-6772", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQJpzpPcx17rcSzBs86omCUcyaCGd7qWMZclRYpkcQPRDA1a1mU"));
        arrayOfUsers.add(new User("Mark", "717-777-5773", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Michael", "717-777-4774", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Adam", "717-777-3775", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Mary", "717-777-2776", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));
        arrayOfUsers.add(new User("Olivia", "717-777-1777", "https://static.toiimg.com/photo/msid-67586673/67586673.jpg"));


        // Create the adapter to convert the array to views
        UserAdapter adapter = new UserAdapter(this, arrayOfUsers);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(adapter);
    }
}