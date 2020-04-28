import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ShoppingService} from '../../../services/shopping.service';
import {MovieService} from '../../../services/movie.service';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-post-payment',
  templateUrl: './post-payment.component.html',
  styleUrls: ['./post-payment.component.css']
})

export class PostPaymentComponent implements OnInit {

  public movieTitles: any;
  public ids: any;
  public mergedLists: any;

  isAuth: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService,
    private movieService: MovieService,
    private authService: AuthenticationService
  ) { }

  ngOnInit() {
    this.authService.isAuth().subscribe(
      data => {
        this.isAuth = data;
        if (this.isAuth === false) {
          this.router.navigate(['/redirect']);
        } else {
          this.constructOrders();
          this.merge();
        }
      });
  }

  constructOrders() {
    const movies = new Map();
    this.ids = [];

    for (const key of Object.keys(this.shoppingService.cartContents)) {
      this.ids.push(key);
      this.movieService.getMovie(key).subscribe( result => {
        movies.set(key, result.title);
      });
    }

    this.movieTitles = movies;
  }

  merge() {
    for (let i = 0; i < this.movieTitles.length; i++) {
      this.mergedLists.push({saleId: this.shoppingService.saleIds[i], movieId: this.ids[i]});
    }
  }

  calculateTotal() {
    let total = 0;

    for (const key of Object.keys(this.shoppingService.cartContents)) {
      total += (this.shoppingService.cartContents[key]*5);
    }

    return total;
  }

  navigateToBrowse() {
    this.router.navigate(['/titles']);
  }

  navigateToSearch() {
    this.router.navigate(['/search']);
  }
}
