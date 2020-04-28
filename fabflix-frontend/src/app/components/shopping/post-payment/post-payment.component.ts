import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ShoppingService} from '../../../services/shopping.service';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-post-payment',
  templateUrl: './post-payment.component.html',
  styleUrls: ['./post-payment.component.css']
})

export class PostPaymentComponent implements OnInit {
  public totalPrice: number;
  public totalMovies: number;
  public sales = new Map();
  public quantities = new Map();

  isAuth: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService,
    private authService: AuthenticationService
  ) { }

  ngOnInit() {
    // this.authService.isAuth().subscribe(
    //   data => {
    //     this.isAuth = data;
    //     if (this.isAuth == false) {
    //       this.router.navigate(['/redirect']);
    //     }
    //     else {
    //       this.constructOrders();
    //     }
    //   }
    // )
    this.constructOrders();
  }
  constructOrders() {
    let saleId: any;
    let movieId: any;

    console.log(this.shoppingService.saleIds);
    console.log(this.shoppingService.saleIds[0]);
    console.log(this.shoppingService.saleIds[1]);

    // for (const sale in this.shoppingService.saleIds) {
    //   console.log(sale);
    //
    //  //  this.shoppingService.getMovieId(saleId).subscribe(result => {
    //  //    for (const id of Object.keys(result)) {
    //  //      movieId = id;
    //  //    }
    //  //  });
    //  //
    //  //  if (this.sales.has(movieId)) {
    //  //    this.quantities[movieId] = this.quantities[movieId] + 1;
    //  //  } else {
    //  //    this.sales.set(movieId, new Array());
    //  //    this.quantities.set(movieId, 1);
    //  //  }
    //  //
    //  //  this.sales[movieId].push(saleId);
    //   }
  }
}
