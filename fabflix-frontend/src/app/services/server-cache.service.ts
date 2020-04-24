import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ServerCacheService {
  private url:string = "http://localhost:8080/fabflix_backend_war/api/cache/";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/";

  constructor(private http: HttpClient) { }

  public cacheSearchParams(params) {
    const headers = new HttpHeaders()
    headers.set('Content-Type', 'application/json');
    console.log(params);
    return this.http.post(this.url+"searchParams", params, {withCredentials:true});
  }

  public getCachedSearchParams() {
    return this.http.get(this.url+"searchParams", {withCredentials: true});
  }
}
