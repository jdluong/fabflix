import { Component, OnInit } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { AuthenticationService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { EmployeeService } from 'src/app/services/employee.service';

@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.css']
})
export class EmployeeDashboardComponent implements OnInit {

  isAuth: boolean;
  userType: string;

  starName:string;
  birthYear:number;

  title:string;
  year:number;
  director:string;
  movieStar:string;
  genre:string;

  invalidAddMovie = false;
  invalidAddMovieMessage = "One or more fields missing";

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private notifier: NotifierService,
    private employeeService: EmployeeService
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

  addStar() {
    let params:any;
    if (!this.birthYear) {
      params = {"name": this.starName};
    }
    else {
      params = {"name": this.starName,
                "birthYear": this.birthYear};
    }
    this.employeeService.addStar(params).subscribe(
      data => {
        this.addStarNotification(data['id']);
      });
  }

  addStarNotification(id) {
    let message = "(NEW) " + this.starName + ": " + id;
    this.notifier.notify('success', message);
  }

  addMovie() {
    if (this.title == undefined || this.year == undefined || this.director == undefined || this.movieStar == undefined || this.genre == undefined) {
      this.invalidAddMovie = true;
    }
    else {
      let params = {"title": this.title,
                  "year": this.year,
                  "director": this.director,
                  "star": this.movieStar,
                  "genre": this.genre};
    this.employeeService.addMovie(params).subscribe(
      data => {
        this.addMovieNotifications(data);
      });
    }
  }

  addMovieNotifications(messages) {
    if (messages['movie_message']) {
      if (!messages['star_message'] && !messages['genre_message']) {
        this.notifier.notify('error', messages['movie_message']);
      }
      else {
        let message = messages['star_message'] + ' | ' + 
                      messages['genre_message'] + ' | ' +
                      messages['movie_message'];
        this.notifier.notify('success', message);
      }
    }
  }
}
