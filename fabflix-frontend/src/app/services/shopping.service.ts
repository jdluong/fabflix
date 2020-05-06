import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {map, tap} from 'rxjs/operators';
import {Movie} from '../models/Movie';
import {ObserveOnSubscriber} from 'rxjs/internal/operators/observeOn';

@Injectable({
  providedIn: 'root'
})
export class ShoppingService {

  constructor(private http: HttpClient) {
  }

  // private url = 'http://localhost:8080/fabflix_backend_war/api/shopping/';
  // public url = 'https://localhost:8443/fabflix_backend_war/api/shopping/'
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/shopping/";
  private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/shopping/";

  public firstName: string;
  public lastName: string;
  public saleIds: number[];
  public cartTotal: number;
  public cartContents: any;

  public addToCart(movieId: string, quantity: number) {
    // console.log(this.url+"/addToCart/"+movieId+"/"+quantity)
    return this.http.get(this.url + 'addToCart/' + movieId + '/' + quantity, {withCredentials: true});
  }

  public getCartContents() {
    return this.http.get(this.url + 'getCartContents', {withCredentials: true});
  }

  public changeItemQuantity(movieId: string, quantity: number) {
    return this.http.get(this.url + 'changeItemQuantity/' + movieId + '/' + quantity, {withCredentials: true});
  }

  public removeItem(movieId: string) {
    return this.http.delete(this.url + 'deleteItem/' + movieId, {withCredentials: true});
  }

  public authenticateOrder(firstName: string, lastName:string, creditcard: string, expiration: string): Observable<Object> {
    const credentials: Map<string, string> = new Map<string, string>();
    credentials.set('firstName', firstName);
    credentials.set('lastName', lastName);
    credentials.set('number', creditcard);
    credentials.set('expiration', expiration);

    const info = {};
    credentials.forEach((val: string, key: string) => {
      info[key] = val;
    });

    return this.http.post(this.url + 'auth', info, {withCredentials: true});
  }

  public addSales() {
    const movies = new Map();
    for (const key of Object.keys(this.cartContents)) {
      movies.set(key, this.cartContents[key]);
    }

    this.saleIds = [];

    for (const movie of movies.keys()) {
        this.http.get<number>(this.url + 'addSale/' + movie, {withCredentials: true}).subscribe(result => {
          this.saleIds.push(result);
        });
    }
  }

  public emptyCart() {
    return this.http.delete(this.url+'emptyCart',{withCredentials:true});
  }

  // public getMovieTitle(saleId: any) {
  //   const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
  //
  //   this.http.get<string>(this.url + 'getMovieTitle/' + saleId, {withCredentials: true, headers}).subscribe(result => {
  //     console.log(result);
  //     return result;
  //   });
  // }
  //
  // public getQuantity(saleId: any) {
  //   return this.http.get<number>(this.url + 'getQuantiity/' + saleId, {withCredentials: true}).subscribe(result => {
  //     console.log(result);
  //     return result;
  //   });
  // }

}
