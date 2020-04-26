import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { faShoppingCart } from '@fortawesome/free-solid-svg-icons';
import {ShoppingService} from "../../../services/shopping.service";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  public cartIcon = faShoppingCart;

  public firstName: string;
  public lastName: string;
  public cardNumber: string;
  public expiration: string;
  public cartTotal = 0.0;

  public incompleteForm = false;
  public invalidOrder = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService
  ) { }

  ngOnInit() {
    this.updateTotal();
  }

  navigateToCart() {
    this.router.navigate(['/cart']);
  }

  checkFields() {
    if (this.firstName === undefined || this.lastName === undefined || this.cardNumber === undefined || this.expiration === undefined) {
      console.log(this.expiration);
      this.incompleteForm = true;
    } else {
      this.incompleteForm = false;
      this.handleOrder();
    }
  }

  setCustomerId(creditcard: string) {
    this.shoppingService.getCustomerId(creditcard).subscribe(result => {
      // @ts-ignore
      this.shoppingService.customerId = result;
    });
  }

  handleOrder() {
    this.shoppingService.authenticateOrder(this.cardNumber, this.expiration).subscribe(result => {
      if (result === true) {
        this.invalidOrder = false;
        this.shoppingService.firstName = this.firstName;
        this.shoppingService.lastName = this.lastName;
        this.setCustomerId(this.cardNumber);
        this.shoppingService.addSales();
        this.router.navigate(['/post-payment']);
      } else {
        this.invalidOrder = true;
      }
    });
  }

  updateTotal() {
    this.cartTotal = this.shoppingService.cartTotal;
  }

}
