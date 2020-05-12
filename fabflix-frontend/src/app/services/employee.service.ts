import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  //private url:string = "http://localhost:8080/fabflix_backend_war/api/employee/";
  public url = 'https://localhost:8443/fabflix_backend_war/api/employee/'
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/employee/";
  // private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/employee/";

  constructor(private http: HttpClient) { }

  public addStar(params:any):Observable<Object> {
    return this.http.post(this.url+"addStar", params, {withCredentials:true});
  }

  public addMovie(params:any):Observable<Object> {
    return this.http.post(this.url+"addMovie", params, {withCredentials: true});
  }

  public getTables():Observable<Object> {
    return this.http.get(this.url+"getTables", {withCredentials:true});
  }

}
