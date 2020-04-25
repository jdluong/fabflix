import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from 'src/app/services/movie.service';
import { Star } from 'src/app/models/Star';
import { Movie } from 'src/app/models/Movie';
import { ServerCacheService } from 'src/app/services/server-cache.service';

@Component({
  selector: 'app-single-star',
  templateUrl: './single-star.component.html',
  styleUrls: ['./single-star.component.css']
})
export class SingleStarComponent implements OnInit {

  star:Star;
  movies:Movie[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private cacheService: ServerCacheService
  ) { }

  ngOnInit() {
    let starId:string;
    starId = this.route.snapshot.paramMap.get('starId');

    this.movieService.getStar(starId).subscribe(
      data => {this.star = data;
              // console.log(this.star)
            });
    this.movieService.getMoviesByStarId(starId).subscribe(
      data => this.movies = data
    );

  }

  navigateToList() {
    let params;
    this.cacheService.getCachedSearchParams().subscribe(
      data => {
        params = data;
        this.router.navigate(['/movie-list'], { queryParams: params });
      });
  }
  
}
