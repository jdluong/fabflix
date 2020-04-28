import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ShoppingService} from '../../../services/shopping.service';

@Component({
  selector: 'app-post-payment',
  templateUrl: './post-payment.component.html',
  styleUrls: ['./post-payment.component.css']
})

export class PostPaymentComponent implements OnInit {


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shoppingService: ShoppingService,
  ) { }

  ngOnInit() {
  }

}
