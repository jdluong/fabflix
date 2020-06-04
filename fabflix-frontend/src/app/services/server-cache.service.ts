import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServerIP } from './server';

@Injectable({
  providedIn: 'root'
})
export class ServerCacheService {
  //private url:string = "http://localhost:8080/fabflix_backend_war/api/cache/";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/cache/";
  // public url = 'https://localhost:8443/fabflix_backend_war/api/cache/'
  // private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/cache/";
  private readUrl:string = ServerIP.Read+"/api/cache/";

  constructor(private http: HttpClient) { }

  public cacheSearchParams(params) {
    const searchParams = params;
    return this.http.post(this.readUrl+"searchParams", params, {withCredentials:true});
  }

  public getCachedSearchParams() {
    return this.http.get(this.readUrl+"searchParams", {withCredentials: true});
  }
}
