package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    final RequestQueue queue = NetworkManager.sharedManager(this).queue;
    final ArrayList<Movie> movies = new ArrayList<>();
    final HashMap<String, ArrayList<String>> genres = new HashMap<>();
    final HashMap<String, ArrayList<String>> stars = new HashMap<>();
    String url = "http://10.0.2.2:8080/fabflix_backend_war/api/";

    MovieListViewAdapter adapter;
    ListView listView;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = findViewById(R.id.list);
        emptyView = findViewById(R.id.emptyView);

        // retrieve movie list from backend and store in movies array
        // TODO change request URL once full text search done
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url + "getTopTwentyList", jsonArray -> {
            // convert array to movie array list
            Gson gson = new Gson();
            Movie[] movieArr = gson.fromJson(jsonArray.toString(), Movie[].class);

            for (Movie m : movieArr) {
                movies.add(new Movie(m.getId(), m.getTitle(), m.getYear(), m.getDirector()));
            }
        }, volleyError -> System.out.println(volleyError.toString()));

        queue.add(jsonArrayRequest);

        // retrieve 3 genre list from backend and store in genre map with movie id as key
        for (Movie m : movies) {
            String id  = m.getId();
            genres.put(id, new ArrayList<>());

           JsonArrayRequest genreJsonRequest = new JsonArrayRequest(url + "get3GenresByMovieId/" + id, jsonArray -> {
                Gson gson = new Gson();
                Type genreListType = new TypeToken<ArrayList<Genre>>(){}.getType();
                ArrayList<Genre> genreArray = gson.fromJson(jsonArray.toString(), genreListType);

                for (Genre g : genreArray)
                    genres.get(id).add(g.getName());

            }, volleyError -> System.out.println(volleyError.toString()));

           Volley.newRequestQueue(this).add(genreJsonRequest);
        }
        
        for (Map.Entry<String, ArrayList<String>> e : genres.entrySet()) {
            System.out.println("Movie Id: " + e.getKey());
            for (String g : e.getValue())
                System.out.println(g);
        }

        // retrieve 3 star list from backend and store in star map with movie id as key

        // view settings
        adapter = new MovieListViewAdapter(movies, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getTitle(), movie.getYear());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        //listView.setEmptyView(emptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.appSearchBar);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}