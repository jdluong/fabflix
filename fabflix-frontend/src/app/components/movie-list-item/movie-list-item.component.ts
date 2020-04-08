import { Component, OnInit, Input } from '@angular/core';
import { Movie } from 'src/app/models/Movie';
import { Genre } from 'src/app/models/Genre';
import { MovieService } from 'src/app/services/movie.service';
import { Star } from 'src/app/models/Star';
import { Rating } from 'src/app/models/Rating';

@Component({
  selector: 'app-movie-list-item',
  templateUrl: './movie-list-item.component.html',
  styleUrls: ['./movie-list-item.component.css']
})
export class MovieListItemComponent implements OnInit {

  @Input() m: Movie;
  @Input() rank: number;
  genres:Genre[];
  stars:Star[];
  rating:Rating;

  constructor(private movieService: MovieService ) { }

  ngOnInit() {
    this.get3Genres(this.m.id);
  }
  
  get3Genres(movieId:string) {
    this.movieService.getGenresByMovieId(movieId).subscribe(
        data => {
          this.genres = data;
        });
  }

}
