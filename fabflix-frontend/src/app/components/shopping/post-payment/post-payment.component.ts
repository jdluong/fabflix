import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ShoppingService} from '../../../services/shopping.service';
import {MovieService} from '../../../services/movie.service';

@Component({
  selector: 'app-post-payment',
  templateUrl: './post-payment.component.html',
  styleUrls: ['./post-payment.component.css']
})

export class PostPaymentComponent implements OnInit {

  public movieTitles: any;
  public ids: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService,
    private movieService: MovieService
  ) { }

  ngOnInit() {
    this.constructOrders();
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

  calculateTotal() {
    let total = 0;

    for (const key of Object.keys(this.shoppingService.cartContents)) {
      total += (this.shoppingService.cartContents[key]*5);
    }

    return total;
  }
}
