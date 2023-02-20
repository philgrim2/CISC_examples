package edu.harrisburg.cis349.fragmentlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

public class MovieListFragment extends Fragment {
    private static final String TAG = "MovieListFragment";
    private Gson gson;
    private RequestQueue queue;

    private final MovieLibrary library = MovieLibrary.getInstance();

    private RecyclerView movieRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        movieRecyclerView = (RecyclerView) view
                .findViewById(R.id.movie_recycler_view);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this.getContext());
        queue.start();

        updateList();
    }

    private void updateList()
    {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET,
                        "https://gist.githubusercontent.com/alanponce/d8a5e47b4328b5560fb610c5731de2bd/raw/b9f2a2b20d7d71f0e9c31adf40c7c83308809ac0/movies.json", null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        Movie movie =
                                                gson.fromJson(response.getJSONObject(i)
                                                                .toString(),
                                                        Movie.class);
                                        library.add(movie);



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, error -> Log.d(TAG, "Error:" + error));
        queue.add(jsonArrayRequest);
    }
}
