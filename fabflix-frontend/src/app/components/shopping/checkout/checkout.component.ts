import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { faShoppingCart } from '@fortawesome/free-solid-svg-icons';
import {ShoppingService} from "../../../services/shopping.service";
import { AuthenticationService } from 'src/app/services/auth.service';

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

  isAuth: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService,
    private authService: AuthenticationService
  ) { }

  ngOnInit() {
    this.authService.isAuth().subscribe(
      data => {
        this.isAuth = data;
        if (this.isAuth == false) {
          this.router.navigate(['/redirect']);
        }
        else {
          this.updateTotal();
        }
      }
    )
    // this.updateTotal();
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

  handleOrder() {
    this.shoppingService.authenticateOrder(this.firstName, this.lastName, this.cardNumber, this.expiration).subscribe(result => {
      if (result === true) {
        this.invalidOrder = false;
        this.shoppingService.firstName = this.firstName;
        this.shoppingService.lastName = this.lastName;
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
