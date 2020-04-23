import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Movie } from 'src/app/models/Movie';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';
import { MovieWithDetails } from 'src/app/models/MovieWithDetails';
import { BrowseService } from 'src/app/services/browse.service';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {

  movies: MovieWithDetails[];
  params: any;
  maxPage: number;
  pageNums = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private browseService: BrowseService,
    private searchService: SearchService
   ) { }

  ngOnInit() {

    this.route.queryParams
      .subscribe(data => 
      {
        this.params = data;
        this.initMovies();
        this.initMaxPage();
      });
    
  }

  initMovies() {
    if (this.params.by == 'genre' || this.params.by == 'title') {
      this.browseService.browseBy(this.params).subscribe(
        data => {
          this.movies = data;
        });
    }
    else {
      this.searchService.search(this.params).subscribe(
        data => {
          this.movies = data;
        });
    }
  }


  initMaxPage() {
    if (this.params.by == 'genre') {
      this.browseService.getNumOfMoviesByGenre(this.params.id).subscribe(
        data => {
          this.maxPage = Math.ceil(data/this.params.perPage);
          console.log("maxPage: "+this.maxPage);
          this.initPageNums();
        });
    }
    else if (this.params.by == 'title') {
      this.browseService.getNumOfMoviesByTitle(this.params.startsWith).subscribe(
        data => {
          this.maxPage = Math.ceil(data/this.params.perPage);
          console.log("maxPage: "+this.maxPage);
          this.initPageNums();
        });
    }
    else {
      this.searchService.getNumOfMoviesBySearch(this.params).subscribe(
        data => {
          this.maxPage = Math.ceil(data/this.params.perPage);
          this.initPageNums();
        }
      );
    }
  }

  initPageNums() {
    this.pageNums = [];
    for (let i = 0; i < this.maxPage; i++) {
      this.pageNums.push(i+1);
    }
    console.log(this.pageNums);
  }

  selectSort(value) {
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

  selectPerPage(value) {
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

  navigateToPage(pageNum:number) {
    let pageParam = {page: pageNum};
    this.params = {...this.params, ...pageParam};
    this.router.navigate(['/movie-list'], { queryParams: this.params});
  }

}
