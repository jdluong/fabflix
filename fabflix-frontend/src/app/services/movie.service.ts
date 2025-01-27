import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Movie } from '../models/Movie';
import { Genre } from '../models/Genre';
import { Star } from '../models/Star';
import { MovieWithDetails } from '../models/MovieWithDetails';
import { Rating } from '../models/Rating';
import { ServerIP } from './server';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  //private url:string = "http://localhost:8080/fabflix_backend_war/api/";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/";
  // public url = 'https://localhost:8443/fabflix_backend_war/api/'
  // private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/";
  private readUrl:string = ServerIP.Read+"/fabflix-backend/api/";


  constructor(private http: HttpClient) {}

  public getTopTwentyList():Observable<Movie[]> {
    return this.http.get<Movie[]>(this.readUrl+"getTopTwentyList");
  }

  public getTopTwentyListWithDetails():Observable<MovieWithDetails[]> {
    return this.http.get<MovieWithDetails[]>(this.readUrl+"getTopTwentyListWithDetails");
  }

  public getMovie(movieId:string):Observable<Movie> {
    return this.http.get<Movie>(this.readUrl+"getMovieByMovieId/"+movieId);
  }

  // put everything below this in other service files?
  public getGenresByMovieId(movieId:string):Observable<Genre[]> {
    return this.http.get<Genre[]>(this.readUrl+"getAllGenresByMovieId/"+movieId);
  }

  public getRatingbyMovieId(movieId:string):Observable<Rating> {
    return this.http.get<Rating>(this.readUrl+"getRatingByMovieId/"+movieId);
  }

  public getStarsByMovieId(movieId:string):Observable<Star[]> {
    return this.http.get<Star[]>(this.readUrl+"getAllStarsByMovieId/"+movieId);
  }

  public getStar(starId:string):Observable<Star> {
    return this.http.get<Star>(this.readUrl+"getStarByStarId/"+starId);
  }

  public getMoviesByStarId(starId:string):Observable<Movie[]> {
    return this.http.get<Movie[]>(this.readUrl+"getMoviesByStarId/"+starId);
  }

  public getAllGenres():Observable<Genre[]> {
    return this.http.get<Genre[]>(this.readUrl+"getAllGenres");
  }

}
