import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Movie } from 'src/app/models/Movie';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';
import { MovieWithDetails } from 'src/app/models/MovieWithDetails';
import { BrowseService } from 'src/app/services/browse.service';
import { QueryParams } from 'src/app/models/QueryParams';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {

  movies: MovieWithDetails[];
  params: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private browseService: BrowseService
   ) { }

  ngOnInit() {

    this.route.queryParams
      .subscribe(data => 
      {
        this.params = data;
        this.initMovies(); 
      });
    
  }

  initMovies() {
    if (this.params.by == 'genre' || this.params.by == 'title') {
      this.browseService.browseBy(this.params).subscribe(
        data => {
          this.movies = data;
        });
    }
  }

  navigateSort(value) {
    if (value) {
      let sortVals = value.split(" ", 4);
      let sortParams = {
                        sortBy1: sortVals[0],
                        order1: sortVals[1],
                        sortBy2: sortVals[2],
                        order2: sortVals[3],
                        page: 1
                      };
      this.params = {...this.params, ...sortParams};
      this.router.navigate(['/movie-list'], { queryParams: this.params});
    }
    return false;
  }

  navigatePerPage(value) {
    if (value) {
      let pageParam = {
                      perPage: value,
                      page: 1
                    };
      this.params = {...this.params, ...pageParam};
      this.router.navigate(['/movie-list'], { queryParams: this.params});
    }
    return false;
  }

}
