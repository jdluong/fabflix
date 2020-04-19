import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { MovieService } from 'src/app/services/movie.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styledUrls: [',/login.component.css']
})
export class LoginComponent implements OnInit {
  constructor (
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService
  ) {}

  ngOnInit() {
    let username: string;
    let password: string;

    
  }
}


