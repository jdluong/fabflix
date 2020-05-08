import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.css']
})
export class EmployeeDashboardComponent implements OnInit {

  isAuth: boolean;
  userType: string;

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.isAuth().subscribe(
      data => {
        this.isAuth = data['isAuth'];
        if (this.isAuth == true) {
          if (data['Customer']) {
            this.userType = 'Customer';
            this.router.navigate(['/not-found']);
          }
          else if (data['Employee']) {
            this.userType = 'Employee';
          }
        }
        else {
          this.router.navigate(['/redirect']);
        }
      });
  }

}
