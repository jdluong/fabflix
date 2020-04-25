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
  incompleteMessage = 'One or more fields incomplete.';
  incomplete = false;
  invalidLogin = false;
  loginSuccess = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
  }

  checkFields() {
    if (this.username === undefined || this.password === undefined) {
      this.incomplete = true;
    } else {
      this.incomplete = false;
      this.handleLogin();
    }
  }

  handleLogin() {
    this.authenticationService.authenticate(this.username, this.password).subscribe(result => {
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
