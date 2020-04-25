import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { faShoppingCart } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  public firstName: string;
  public lastName: string;
  public cardNumber: string;
  public expiration: string;
  public cartIcon = faShoppingCart;
  public cartTotal: number;
  public incomplete = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit() {
    this.getCartTotal();
  }

  navigateToCart() {
    this.router.navigate(['/cart']);
  }

  checkFields() {
    if (this.firstName === undefined || this.lastName === undefined || this.cardNumber === undefined || this.expiration === undefined) {
      this.incomplete = true;
    } else {
      this.incomplete = false;
      console.log('onwards');
    }
  }

  getCartTotal() {
    this.cartTotal = 0.00;
    this.cartTotal.toPrecision(2);
  }


}
