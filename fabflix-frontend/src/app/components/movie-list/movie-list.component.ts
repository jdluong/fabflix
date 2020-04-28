import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { switchMap } from 'rxjs/operators';
import { Movie } from 'src/app/models/Movie';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';
import { MovieWithDetails } from 'src/app/models/MovieWithDetails';
import { BrowseService } from 'src/app/services/browse.service';
import { SearchService } from 'src/app/services/search.service';
import { ShoppingService } from 'src/app/services/shopping.service';
import { ServerCacheService } from 'src/app/services/server-cache.service';
import { AuthenticationService } from 'src/app/services/auth.service';

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

  isAuth: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private browseService: BrowseService,
    private searchService: SearchService,
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
        this.isAuth = data;
        if (this.isAuth == false) {
          this.router.navigate(['/redirect']);
        }
        else {
          this.route.queryParams.subscribe(
            data => {
              this.params = data;
              if (this.params == {}){
                this.router.navigate(['/search']);
              }
              else {
                this.initMovies();
                this.initMaxPage();
              }
            });
        }
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
          this.initPageNums();
        });
    }
    else if (this.params.by == 'title') {
      this.browseService.getNumOfMoviesByTitle(this.params.startsWith).subscribe(
        data => {
          this.maxPage = Math.ceil(data/this.params.perPage);
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
  }


  // for search params update by select boxes

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


  // for add to cart button

  addToCart(movieId:string, movieTitle:string) {
    this.shoppingService.addToCart(movieId, 1).subscribe(
      data => {
        // console.log(data[movieId]);
        if (data[movieId] == 1) {
          this.notifier.notify('success', 'Added \"'+movieTitle+"\" to cart");
        }
        else {
          this.notifier.notify('error', 'Could not add \"'+movieTitle+"\" to cart");
        }
      });

  }


  // for caching search params after clicking on movie/star/cart

  navigateToSearch() {
    let params = {}; // empty searchParams in cache
    this.cacheService.cacheSearchParams(params).subscribe();
    this.router.navigate(['/search']);
  }

  navigateToCart() {
    this.cacheService.cacheSearchParams(this.params).subscribe();
    this.router.navigate(['/cart']);
  }

  navigateToMovie(movieId:string) {
    this.cacheService.cacheSearchParams(this.params).subscribe();
    this.router.navigate(['/movie/', movieId]);
  }

  navigateToStar(starId:string) {
    this.cacheService.cacheSearchParams(this.params).subscribe();
    this.router.navigate(['/star/', starId]);
  }


  // for page nav at the bottom

  navigateToPage(pageNum:number) {
    let pageParam = {page: pageNum};
    this.params = {...this.params, ...pageParam};
    this.router.navigate(['/movie-list'], { queryParams: this.params});
  }

  navigatePrevPage() {
    let currPage = this.params.page;
    let pageParam = {page: --currPage};
    this.params = {...this.params, ...pageParam};
    this.router.navigate(['/movie-list'], { queryParams: this.params});
  }

  navigateNextPage() {
    let currPage = this.params.page;
    let pageParam = {page: ++currPage};
    this.params = {...this.params, ...pageParam};
    this.router.navigate(['/movie-list'], { queryParams: this.params});
  }


  // for html/css

  prevButtonDis() {
    return (this.params.page == 1);
  }

  nextButtonDis() {
    return (this.params.page == this.maxPage);
  }

  currPageDis(pageNum) {
    return (this.params.page == pageNum);
  }

  currPageClass(pageNum) {
    let classes = {
      'btn': true,
      'btn-secondary': (this.params.page == pageNum),
      'disabled': (this.params.page == pageNum)
    }
    return classes;
  }

}
