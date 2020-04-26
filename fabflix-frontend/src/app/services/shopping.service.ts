import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShoppingService {

  constructor(private http: HttpClient) {
  }

  private url = 'http://localhost:8080/fabflix_backend_war/api/shopping/';
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/";

  public firstName: string;
  public lastName: string;
  public address: string;
  public saleIds: number[];
  public customerId: number;
  public cartTotal: number;
  public cartContents: any;
  error;

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

  public authenticateOrder(creditcard: string, expiration: string): Observable<Object> {
    const credentials: Map<string, string> = new Map<string, string>();
    credentials.set('number', creditcard);
    credentials.set('expiration', expiration);

    const info = {};
    credentials.forEach((val: string, key: string) => {
      info[key] = val;
    });

    return this.http.post(this.url + 'auth', info, {withCredentials: true});
  }

  // tslint:disable-next-line:ban-types
  public getCustomerId(creditcard: string): Observable<Number> {
    const credentials: Map<string, string> = new Map<string, string>();
    credentials.set('number', creditcard);

    const info = {};
    credentials.forEach((val: string, key: string) => {
      info[key] = val;
    });

    // @ts-ignore
    return this.http.post(this.url + 'getCustomerId', info, {withCredentials: true});
  }

  public addSales() {
    const movieIds = [];
    for (const key of Object.keys(this.cartContents)) {
      movieIds.push(key);
    }

    this.http.get(this.url + 'addSale/' + this.customerId + movieIds, {withCredentials: true}).subscribe(result => {
      for (const id of Object.keys(result)) {
        this.saleIds.push(Number(id));
      }
    }, error => {
      console.log(error);
    });
  }
}
