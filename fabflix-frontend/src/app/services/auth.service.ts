import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public username: string;
  public password: string;
  public url = 'http://localhost:8080/fabflix_backend_war/api/auth';

  constructor(private http: HttpClient) { }

  authenticate(username: string, password: string) : Observable<Object> {
    const credentials: Map<string, string> = new Map<string, string>();
    credentials.set('username', username);
    credentials.set('password', password);

    const user = {};
    credentials.forEach((val: string, key: string) => {
      user[key] = val;
    });

    return this.http.post(this.url, user, {withCredentials: true});
  }

  isAuth() {
    return this.http.get(this.url + '/isAuth', {withCredentials : true});
  }

}

