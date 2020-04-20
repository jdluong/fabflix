import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MovieWithDetails } from '../models/MovieWithDetails';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BrowseService {
  private url:string = "http://localhost:8080/fabflix_backend_war/api/browse";
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/browse";

  constructor(private http: HttpClient) { }

  public browseBy(params:any):Observable<MovieWithDetails[]> {
      this.http.get(this.url, {params: })
  }
}
