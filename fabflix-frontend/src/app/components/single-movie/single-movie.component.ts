import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { HttpParams } from '@angular/common/http'

import { MovieService } from '../../services/movie.service';
import { Movie } from 'src/app/models/Movie';
import { GenerateBaseOptions } from 'rxjs/internal/observable/generate';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';

@Component({
  selector: 'app-single-movie',
  templateUrl: './single-movie.component.html',
  styleUrls: ['./single-movie.component.css']
})
export class SingleMovieComponent implements OnInit {

  movie: Movie;
  genres: Genre[];
  stars: Star[]

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService
  ) { }

  ngOnInit() {

    let movieId:string;

    // this.route.paramMap.pipe(
    //   switchMap((params: ParamMap) =>
    //     params.get('movieId'))).
    //     subscribe( data => { console.log(data) });
    movieId = this.route.snapshot.paramMap.get('movieId');

    this.movieService.getMovie(movieId).subscribe(
      data => {this.movie = data;
              console.log(this.movie)}
    );
    this.movieService.getGenresByMovieId(movieId).subscribe(
      data => this.genres = data
    );
    // this.movieService.getStarsByMovieId(movieId, {'limit': false}).subscribe(
    //   data => this.stars = data
    // );

  }

}
