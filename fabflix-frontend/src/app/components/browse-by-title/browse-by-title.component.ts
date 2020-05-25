import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-browse-by-title',
  templateUrl: './browse-by-title.component.html',
  styleUrls: ['./browse-by-title.component.css']
})
export class BrowseByTitleComponent implements OnInit {
  
  numbers = ['1','2','3','4','5','6','7','8','9','0'];
  letters = ['A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
  
  isAuth: any;
  userType: string;

  constructor(
    private authService: AuthenticationService,
    private router: Router) { }

  ngOnInit() {
    this.authService.isAuth().subscribe(
      data => {
        this.isAuth = data['isAuth'];
        if (this.isAuth == false) {
          this.router.navigate(['/redirect']);
        }
        else {
          if (data['Employee']) {
            this.userType = 'Employee';
          }
          else if (data['Customer']) {
            this.userType = 'Customer';  
          }
        }
      });
  }

}
