import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import {Observable} from 'rxjs';
import { ServerIP } from './server';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public username: string;
  public password: string;
  //public url = 'http://localhost:8080/fabflix_backend_war/api/auth';
  // private url:string = "http://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8080/fabflix-backend/api/auth";
  // public url = 'https://localhost:8443/fabflix_backend_war/api/auth'
  // private url:string = "https://ec2-54-68-162-171.us-west-2.compute.amazonaws.com:8443/fabflix-backend/api/auth";
  private readUrl:string = ServerIP.Read+"/fabflix-backend/api/auth";

  constructor(private http: HttpClient) { }

  authenticate(username: string, password: string, recaptcha: any, userType:string): Observable<Object> {
    const credentials: Map<string, string> = new Map<string, string>();
    credentials.set('username', username);
    credentials.set('password', password);
    credentials.set('g-recaptcha', recaptcha);
    credentials.set('userType', userType);

    const user = {};
    credentials.forEach((val: string, key: string) => {
      user[key] = val;
    });

    return this.http.post(this.readUrl, user, {withCredentials: true});
  }

  isAuth() {
    return this.http.get(this.readUrl + '/isAuth', {withCredentials : true});
  }

}

