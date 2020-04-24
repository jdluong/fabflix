import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShoppingService } from 'src/app/services/shopping.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cart:any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService) { }

  ngOnInit() {
    this.shoppingService.getCartContents().subscribe(
      data => {
        this.cart = data;
        console.log(this.cart);
      });
  }

}
