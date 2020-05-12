import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';
import { AuthenticationService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-browse-by-genre',
  templateUrl: './browse-by-genre.component.html',
  styleUrls: ['./browse-by-genre.component.css']
})
export class BrowseByGenreComponent implements OnInit {

  genres: Genre[];

  isAuth: any;
  userType:string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private authService: AuthenticationService
  ) { }

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
          this.movieService.getAllGenres().subscribe(
            data => {
              this.genres = data;
            });
          }
      });
  }

  browseByGenre(genre: Genre) {
    let params = {
      by: 'genre',
      id: genre.id
    };
    this.router.navigate(['/movie-list', params]);
  }
  
}
