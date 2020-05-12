import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShoppingService } from 'src/app/services/shopping.service';
import { Movie } from 'src/app/models/Movie';
import { MovieService } from 'src/app/services/movie.service';
import { ServerCacheService } from 'src/app/services/server-cache.service';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cart: any;
  ids: any;
  movieTitles: any;
  isAuth: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService,
    private movieService: MovieService,
    private cacheService: ServerCacheService,
    private authService: AuthenticationService) { }

  ngOnInit() {
    this.authService.isAuth().subscribe(
      data => {
        this.isAuth = data['isAuth'];
        if (this.isAuth == false) {
          this.router.navigate(['/redirect']);
        }
        else {
          this.shoppingService.getCartContents().subscribe(
            data => {
              this.cart = data;
              console.log(this.cart);
              this.initMovies();
            });   
        }
      });
    // this.shoppingService.getCartContents().subscribe(
    //   data => {
    //     this.cart = data;
    //     console.log(this.cart);
    //     this.initMovies();
    //   });
  }

  initMovies() {
    let map = new Map();
    this.ids = [];
    for (let key of Object.keys(this.cart)) {
      this.ids.push(key);
      this.movieService.getMovie(key).subscribe(
        data => {
          map.set(key, data.title);
        });
    }
    this.movieTitles = map;
    console.log(this.movieTitles);
  }

  calculateTotal() {
    let total = 0;
    for (let key of Object.keys(this.cart)) {
      total += (this.cart[key]*5);
    }
    return total;
  }

  navigateToList() {
    let params;
    this.cacheService.getCachedSearchParams().subscribe(
      data => {
        params = data;
        this.router.navigate(['/movie-list'], { queryParams: params });
      });
  }

  navigateToCheckout() {
    this.shoppingService.cartTotal = this.calculateTotal();
    this.shoppingService.cartContents = this.cart;
    this.router.navigate(['/checkout']);
  }

  updateQuantity(movieId, quantity) {
    this.shoppingService.changeItemQuantity(movieId, quantity).subscribe(
      data => {
        this.ngOnInit();
      });
  }

  removeItem(movieId) {
    this.shoppingService.removeItem(movieId).subscribe(
      data => {
        this.ngOnInit();
      }
    );
  }

}
