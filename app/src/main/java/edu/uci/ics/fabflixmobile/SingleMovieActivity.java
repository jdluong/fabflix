package edu.uci.ics.fabflixmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleMovieActivity extends AppCompatActivity {
    ScrollView singleMovieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlemovie);
        singleMovieView = findViewById(R.id.singleMovie);

        String title = getIntent().getStringExtra("title");
        String year = getIntent().getStringExtra("year");
        String director = getIntent().getStringExtra("director");

        ArrayList<String> genres = getIntent().getStringArrayListExtra("genres");
        ArrayList<String> stars = getIntent().getStringArrayListExtra("stars");
        StringBuilder genreStr = new StringBuilder();
        StringBuilder starStr = new StringBuilder();

        for (int i = 0; i < genres.size(); ++i) {
            if (i == genres.size() - 1)
                genreStr.append(genres.get(i));
            else
                genreStr.append(genres.get(i) + "\n");
        }

        for (int i = 0; i < stars.size(); ++i) {
            if (i == stars.size() - 1)
                starStr.append(stars.get(i));
            else
                starStr.append(stars.get(i) + "\n");
        }

        TextView movieTitle = singleMovieView.findViewById(R.id.movieTitle);
        TextView yearValue = singleMovieView.findViewById(R.id.yearValue);
        TextView dirValue = singleMovieView.findViewById(R.id.dirValue);
        TextView genreValue = singleMovieView.findViewById(R.id.genreValue);
        TextView starValue = singleMovieView.findViewById(R.id.starValue);

        movieTitle.setText(title);
        yearValue.setText(year);
        dirValue.setText(director);
        genreValue.setText(genreStr.toString());
        starValue.setText(starStr.toString());
    }
}
