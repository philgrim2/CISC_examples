package edu.harrisburg.cis349.simplelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class UserActivity extends AppCompatActivity {

    static UserAdapter adapter;

    public static Intent newIntent(Context packageContext, UserAdapter adapterRef) {
        Intent i = new Intent(packageContext, UserActivity.class);
        adapter = adapterRef;
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        int index = getIntent().getIntExtra(UserAdapter.EXTRA_SELECTED_ITEM, -1);
        if (index >= 0)
        {
            adapter.populateView(findViewById(R.id.userLayout), index);
        }
    }
}