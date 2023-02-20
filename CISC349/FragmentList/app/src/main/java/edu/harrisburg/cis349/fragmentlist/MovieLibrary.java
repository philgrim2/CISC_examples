package edu.harrisburg.cis349.fragmentlist;

import java.util.ArrayList;
import java.util.List;

public class MovieLibrary {
    protected ArrayList<Movie> movies;

    private static MovieLibrary instance;

    public static MovieLibrary getInstance() {
        if (null == instance)  {
            instance = new MovieLibrary();
        }
        return instance;
    }

    protected MovieLibrary()
    {
        movies = new ArrayList<Movie>();
    }

    public void add(Movie movie)
    {
        movies.add(movie);
    }

    public List<Movie> getMovies()
    {
        return movies;
    }
}
