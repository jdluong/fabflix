import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '../../services/auth.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  errorMessage = 'Invalid username/password.';
  successMessage: string;
  invalidLogin = false;
  loginSuccess = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
  }

  handleLogin() {
    this.authenticationService.authenticate(this.username, this.password).subscribe(result => {
      if (result === true) {
        this.authenticationService.registerSuccessfulLogin(this.username, this.password);
        this.invalidLogin = false;
        this.loginSuccess = true;
        this.successMessage = 'Login Successful.';
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
