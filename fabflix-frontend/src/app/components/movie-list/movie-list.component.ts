import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Movie } from 'src/app/models/Movie';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';
import { MovieWithDetails } from 'src/app/models/MovieWithDetails';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {

  topMovies: MovieWithDetails[];
  numMovies: number = 20;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService
   ) { }

  ngOnInit() {
    this.movieService.getTopTwentyListWithDetails().subscribe(
      data => {
        this.topMovies = data;
      });
  }

}
