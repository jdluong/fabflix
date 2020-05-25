package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MovieListViewAdapter extends ArrayAdapter<MovieWithDetails> {
    private ArrayList<MovieWithDetails> movies;

    public MovieListViewAdapter(ArrayList<MovieWithDetails> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.row, parent, false);

        MovieWithDetails movie = movies.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitleView = view.findViewById(R.id.subtitle);

        titleView.setText(movie.getMovie().getTitle());

        StringBuilder subtitle = new StringBuilder();
        subtitle.append("Year: " + movie.getMovie().getYear() + "\nDirector: " + movie.getMovie().getDirector());

        List<Genre> genres = movie.getGenres();

        if (genres.size() >= 3) {
            subtitle.append("\nGenre: " + genres.get(0).getName() + ", " + genres.get(1).getName() + ", " + genres.get(2).getName());
        }
        else {
            subtitle.append("\nGenre: ");
            for (int i = 0; i < genres.size(); ++i) {
                if (i == genres.size() - 1)
                    subtitle.append(genres.get(i).getName() + " ");
                else
                    subtitle.append(genres.get(i).getName() + ", ");
            }
        }

        List<Star> stars = movie.getStars();

        if (stars.size() >= 3) {
            subtitle.append("\nStars: " + stars.get(0).getName() + ", " +  stars.get(1).getName() + ", " + stars.get(2).getName() + ", ");
        }
        else {
            subtitle.append("\nStars: ");
            for (int i = 0; i < stars.size(); ++i) {
                if (i == stars.size() - 1)
                    subtitle.append(stars.get(i).getName() + " ");
                else
                    subtitle.append(stars.get(i).getName() + ", ");
            }
        }

        subtitle.append("\nRating: " + movie.getRating().getRating());

        subtitleView.setText(subtitle.toString());

        return view;
    }
}