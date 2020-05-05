import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '../../services/auth.service';

declare var grecaptcha: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  errorMessage = 'Invalid username/password.';
  incompleteMessage = 'One or more fields incomplete.';
  recaptchaMessage = 'Invalid recaptcha.';
  incomplete = false;
  invalidLogin = false;
  invalidRecaptcha = false;
  loginSuccess = false;
  recaptchaResp: any;

  isAuth: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.authenticationService.isAuth().subscribe(
      data => {
        this.isAuth = data;
        if (this.isAuth === true) {
          this.router.navigate(['/search']);
        }
      });
  }

  checkFields() {
    if (this.username === undefined || this.password === undefined) {
      this.incomplete = true;
    } else {
      this.recaptchaResp = grecaptcha.getResponse();
      console.log(this.recaptchaResp);
      if (this.recaptchaResp.length === 0) {
        this.invalidRecaptcha = true;
      } else {
        this.incomplete = false;
        this.invalidRecaptcha = false;
        this.handleLogin();
      }
    }
  }

  handleLogin() {
    this.authenticationService.authenticate(this.username, this.password, this.recaptchaResp).subscribe(result => {
      if (result === true) {
        this.invalidLogin = false;
        this.loginSuccess = true;
        this.router.navigate(['/search']);
      } else {
        this.invalidLogin = true;
        this.loginSuccess = false;
      }
    }, error => {
      console.log(error);
    });
  }
}
