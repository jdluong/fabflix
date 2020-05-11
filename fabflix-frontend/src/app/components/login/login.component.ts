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

  recaptchaId: any;

  isAuth: any;

  userType = "Customer";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService) {
      this.loadScript();
  }

  ngOnInit() {
    this.authenticationService.isAuth().subscribe(
      data => {
        this.isAuth = data['isAuth'];
        if (this.isAuth === true) {
          if (data['Employee']) {
            this.router.navigate(['/_dashboard']);
          }
          else if (data['Customer']) {
            this.router.navigate(['/search']);  
          }
        }
      });
  }
 
  ngAfterViewChecked() {
    if (this.recaptchaId == null) {
      this.recaptchaId = grecaptcha.render('recaptcha', {
        'sitekey': "6LdCRfEUAAAAAHfGp1JVafyPoAsYADMioRmb54oO" 
      });
    }
  }

  changeTo(userType:string) {
    this.userType = userType;
  }

  checkFields() {
    if (this.username === undefined || this.password === undefined) {
      this.incomplete = true;
      console.log("resetting recaptcha..");
      grecaptcha.reset(this.recaptchaId);
      console.log("recaptcha reset");
    } else {
      this.recaptchaResp = grecaptcha.getResponse();
      console.log(this.recaptchaResp);
      if (this.recaptchaResp.length === 0) {
        this.invalidRecaptcha = true;
        console.log("resetting recaptcha..");
        grecaptcha.reset(this.recaptchaId);
        console.log("recaptcha reset");
      } else {
        this.incomplete = false;
        this.invalidRecaptcha = false;
        this.handleLogin();
      }
    }
  }

  handleLogin() {
    this.authenticationService.authenticate(this.username, this.password, this.recaptchaResp, this.userType).subscribe(result => {
      if (result === true) {
        this.invalidLogin = false;
        this.loginSuccess = true;
        if (this.userType == "Customer") {
          this.router.navigate(['/search']);
        }
        else if (this.userType == "Employee") {
          this.router.navigate(['/_dashboard']);
        }
      } else {
        this.invalidLogin = true;
        this.loginSuccess = false;
        console.log("resetting recaptcha..");
        grecaptcha.reset(this.recaptchaId);
        console.log("recaptcha reset");
      }
    }, error => {
      console.log(error);
    });
  }

  public loadScript() {
    let body = <HTMLDivElement> document.body;
    let script = document.createElement('script');
    script.innerHTML = '';
    script.src = 'https://www.google.com/recaptcha/api.js';
    script.async = true;
    script.defer = true;
    body.appendChild(script);
}

  public loadRecaptcha() {
    var script = document.createElement('script');
    script.src = "https://www.google.com/recaptcha/api.js";
    script.async =false;
    document.head.appendChild(script);
    return new Promise((resolve, reject) => {
      script.onload = resolve;
    }); 
  }
}
