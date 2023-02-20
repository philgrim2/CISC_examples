package edu.harrisburg.cis349.fragmentlist;

import androidx.fragment.app.Fragment;


public class MovieListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MovieListFragment();
    }
}