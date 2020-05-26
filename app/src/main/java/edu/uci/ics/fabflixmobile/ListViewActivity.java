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
import java.util.*;

public class ListViewActivity extends AppCompatActivity {
    final RequestQueue queue = NetworkManager.sharedManager(this).queue;
    final ArrayList<MovieWithDetails> movies = new ArrayList<>();
    final ArrayList<MovieWithDetails> data = new ArrayList<>();
    //String url = "http://10.0.2.2:8080/fabflix_backend_war/api/app/";
    String url = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/";

    MovieListViewAdapter adapter;
    ListView listView;
    TextView emptyView;
    EditText search;
    Button prev;
    Button next;

    private int NUM_ITEMS_PER_PAGE = 20;
    private int pageCount;
    private int increment = 0;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = findViewById(R.id.list);
        emptyView = findViewById(R.id.emptyView);
        search = findViewById(R.id.searchFilter);
        prev = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        prev.setEnabled(false);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --increment;
                loadList(increment);
                checkEnable();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++increment;
                loadList(increment);
                checkEnable();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                movies.clear();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url + "search/" + search.getText().toString(), jsonArray -> {
                            // convert array to movie array list
                            Gson gson = new Gson();
                            MovieWithDetails[] movieArr = gson.fromJson(jsonArray.toString(), MovieWithDetails[].class);

                            for (MovieWithDetails m : movieArr) {
                                movies.add(new MovieWithDetails(m.getMovie(), m.getGenres(), m.getStars(), m.getRating()));
                            }

                        }, volleyError -> System.out.println(volleyError.toString()));

                        int num = movies.size() % NUM_ITEMS_PER_PAGE;
                        num = (num == 0) ? 0 : 1;
                        pageCount = movies.size() / NUM_ITEMS_PER_PAGE + num;

                        queue.add(jsonArrayRequest);
                    }
                }, 200);
            }
        });

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
    }

    private void checkEnable() {
        if (increment + 1 == pageCount)
            next.setEnabled(false);
        else if (increment == 0)
            prev.setEnabled(false);
        else {
            prev.setEnabled(true);
            next.setEnabled(true);
        }
    }

    private void loadList(int pageNum) {
        data.clear();
        int start = pageNum * NUM_ITEMS_PER_PAGE;

        for (int i = start; i < (start + NUM_ITEMS_PER_PAGE); ++i) {
            if (i < movies.size())
                data.add(movies.get(i));
            else
                break;
        }

        adapter = new MovieListViewAdapter(data, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            loadList(0);
            checkEnable();
        }

        return super.onOptionsItemSelected(item);
    }
}