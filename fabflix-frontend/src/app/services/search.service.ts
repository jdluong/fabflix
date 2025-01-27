import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MovieWithDetails } from '../models/MovieWithDetails';
import { Observable } from 'rxjs';
import { ServerIP } from './server';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  //private url:string = "http://localhost:8080/fabflix_backend_war/api/search";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/search";
  // public url = 'https://localhost:8443/fabflix_backend_war/api/search'
  // private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/search";
  private readUrl:string = ServerIP.Read+"/fabflix-backend/api/search";

  constructor(private http: HttpClient) { }

  public getSuggestions(params:any):Observable<any[]> {
    return this.http.get<any[]>(this.readUrl+"/suggestions", {params: params});
  }
  
  public search(params:any):Observable<MovieWithDetails[]> {
    return this.http.get<MovieWithDetails[]>(this.readUrl, {params:params});
  }

  public getNumOfMoviesBySearch(params:any):Observable<number> {
    return this.http.get<number>(this.readUrl+"/getNumOfMovies", {params: params});
  }
}
