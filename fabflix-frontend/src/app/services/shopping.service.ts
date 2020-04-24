import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShoppingService {
  private url:string = "http://localhost:8080/fabflix_backend_war/api/shopping/";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/";


  constructor(private http: HttpClient) { }

  public addToCart(movieId:string, quantity:number) {
    // console.log(this.url+"/addToCart/"+movieId+"/"+quantity)
    return this.http.get(this.url+"addToCart/"+movieId+"/"+quantity, {withCredentials: true});
  }
  
  public getCartContents() {
    return this.http.get(this.url+"getCartContents", {withCredentials:true});
  }

  public changeItemQuantity(movieId:string, quantity:number) {
    return this.http.get(this.url+"changeItemQuantity/"+movieId+"/"+quantity, {withCredentials: true});
  }

  public removeItem(movieId:string) {
    return this.http.delete(this.url+"deleteItem/"+movieId, {withCredentials: true});
  }

}
