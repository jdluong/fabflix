import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { switchMap } from 'rxjs/operators';
import { HttpParams } from '@angular/common/http'

import { MovieService } from '../../services/movie.service';
import { Movie } from 'src/app/models/Movie';
import { GenerateBaseOptions } from 'rxjs/internal/observable/generate';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';
import { Rating } from 'src/app/models/Rating';
import { ShoppingService } from 'src/app/services/shopping.service';
import { ServerCacheService } from 'src/app/services/server-cache.service';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-single-movie',
  templateUrl: './single-movie.component.html',
  styleUrls: ['./single-movie.component.css']
})
export class SingleMovieComponent implements OnInit {

  movie: Movie;
  genres: Genre[];
  stars: Star[]
  rating: Rating;

  isAuth: any;
  userType: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private shoppingService: ShoppingService,
    private cacheService: ServerCacheService,
    private notifier: NotifierService,
    private authService: AuthenticationService) 
    {
      this.notifier = notifier;
    }

  ngOnInit() {
    this.authService.isAuth().subscribe( 
      data => {
        this.isAuth = data['isAuth'];
        if (this.isAuth == false) {
          this.router.navigate(['/redirect']);
        }
        else {
          if (data['Employee']) {
            this.userType = 'Employee';
          }
          else if (data['Customer']) {
            this.userType = 'Customer';  
          }
          let movieId:string;
          movieId = this.route.snapshot.paramMap.get('movieId');
          this.movieService.getMovie(movieId).subscribe(
            data => {
              this.movie = data;
              this.movieService.getGenresByMovieId(movieId).subscribe(
                data => this.genres = data
              );
              this.movieService.getStarsByMovieId(movieId).subscribe(
                data => this.stars = data
              );
              this.movieService.getRatingbyMovieId(movieId).subscribe(
                data => this.rating = data
              );
            });
        }
      });
  }

  navigateToList() {
    let params;
    this.cacheService.getCachedSearchParams().subscribe(
      data => {
        params = data;
        this.router.navigate(['/movie-list'], { queryParams: params });
      });
  }
  
  addToCart(movieId:string, movieTitle:string) {
    this.shoppingService.addToCart(movieId, 1).subscribe(
      data => {
        if (data[movieId] == 1) {
          this.notifier.notify('success', 'Added \"'+movieTitle+"\" to cart");
        }
        else {
          this.notifier.notify('error', 'Could not add \"'+movieTitle+"\" to cart");
        }
      });
  }

}
