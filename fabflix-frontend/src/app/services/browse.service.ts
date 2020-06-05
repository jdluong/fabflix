import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MovieWithDetails } from '../models/MovieWithDetails';
import { Observable } from 'rxjs';
import { ServerIP } from './server';

@Injectable({
  providedIn: 'root'
})
export class BrowseService {
  //private url:string = "http://localhost:8080/fabflix_backend_war/api/browse";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/browse";
  // public url = 'https://localhost:8443/fabflix_backend_war/api/browse'
  // private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/browse";
  private readUrl:string = "http://"+ServerIP.Read+":8080/fabflix-backend/api/browse";

  constructor(private http: HttpClient) { }

  public browseBy(params:any):Observable<MovieWithDetails[]> {
      return this.http.get<MovieWithDetails[]>(this.readUrl, {params: params});
  }

  public getNumOfMoviesByGenre(genreId:string):Observable<number> {
      return this.http.get<number>(this.readUrl+"/getNumOfMovies/genre/"+genreId);
  }

  public getNumOfMoviesByTitle(startsWith:string):Observable<number> {
    return this.http.get<number>(this.readUrl+"/getNumOfMovies/title/"+startsWith);
  }
}
