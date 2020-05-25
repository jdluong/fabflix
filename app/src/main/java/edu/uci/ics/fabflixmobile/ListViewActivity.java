package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    final RequestQueue queue = NetworkManager.sharedManager(this).queue;
    final ArrayList<MovieWithDetails> movies = new ArrayList<>();
    String url = "http://10.0.2.2:8080/fabflix_backend_war/api/";
    //String url = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/";

    MovieListViewAdapter adapter;
    ListView listView;
    TextView emptyView;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = findViewById(R.id.list);
        emptyView = findViewById(R.id.emptyView);
        search = findViewById(R.id.searchFilter);
        adapter = new MovieListViewAdapter(movies, getApplicationContext());
        listView.setAdapter(adapter);

        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get current movie and load new intent with its details
                MovieWithDetails movie = movies.get(position);
                Intent singleMovie = new Intent(ListViewActivity.this, SingleMovieActivity.class);
                singleMovie.putExtra("title", movie.getMovie().getTitle());
                singleMovie.putExtra("year", String.valueOf(movie.getMovie().getYear()));
                singleMovie.putExtra("director", movie.getMovie().getDirector());
                ArrayList<String> genres = new ArrayList<>();
                ArrayList<String> stars = new ArrayList<>();
                for (Genre g : movie.getGenres()) {
                    genres.add(g.getName());
                }
                for (Star s : movie.getStars()) {
                    stars.add(s.getName());
                }
                singleMovie.putStringArrayListExtra("genres", genres);
                singleMovie.putStringArrayListExtra("stars", stars);

                startActivity(singleMovie);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(charSequence.toString());

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url + "getTopTwentyListWithDetails", jsonArray -> {
                    // convert array to movie array list
                    Gson gson = new Gson();
                    MovieWithDetails[] movieArr = gson.fromJson(jsonArray.toString(), MovieWithDetails[].class);

                    for (MovieWithDetails m : movieArr) {
                        movies.add(new MovieWithDetails(m.getMovie(), m.getGenres(), m.getStars(), m.getRating()));
                        adapter.notifyDataSetChanged();
                    }
                }, volleyError -> System.out.println(volleyError.toString()));

                queue.add(jsonArrayRequest);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}