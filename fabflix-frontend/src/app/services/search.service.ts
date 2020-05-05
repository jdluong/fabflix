import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MovieWithDetails } from '../models/MovieWithDetails';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private url:string = "http://localhost:8080/fabflix_backend_war/api/search";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/search";

  constructor(private http: HttpClient) { }

  public search(params:any):Observable<MovieWithDetails[]> {
    return this.http.get<MovieWithDetails[]>(this.url, {params:params});
  }

  public getNumOfMoviesBySearch(params:any):Observable<number> {
    return this.http.get<number>(this.url+"/getNumOfMovies", {params: params});
  }
}
