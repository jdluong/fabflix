import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from 'src/app/services/movie.service';
import { Star } from 'src/app/models/Star';
import { Movie } from 'src/app/models/Movie';
import { ServerCacheService } from 'src/app/services/server-cache.service';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-single-star',
  templateUrl: './single-star.component.html',
  styleUrls: ['./single-star.component.css']
})
export class SingleStarComponent implements OnInit {

  star:Star;
  movies:Movie[];

  isAuth: any;
  userType: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private cacheService: ServerCacheService,
    private authService: AuthenticationService
  ) { }

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
          let starId:string;
          starId = this.route.snapshot.paramMap.get('starId');

          this.movieService.getStar(starId).subscribe(
            data => {
                this.star = data;
                this.movieService.getMoviesByStarId(starId).subscribe(
                  data => {
                    this.movies = data;
                });
            });
          }
        });
  }

  navigateToList() {
    this.cacheService.getCachedSearchParams().subscribe(
      data => {
        if (Object.keys(data).length == 0) {
          this.router.navigate(['/search']);
        }
        else {
          this.router.navigate(['/movie-list'], { queryParams: data });
        }
      });
  }
  
}
