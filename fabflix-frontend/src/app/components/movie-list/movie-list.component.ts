import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Movie } from 'src/app/models/Movie';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';
import { Star } from 'src/app/models/Star';
import { MovieWithDetails } from 'src/app/models/MovieWithDetails';
import { BrowseService } from 'src/app/services/browse.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {

  movies: MovieWithDetails[];
  params: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private browseService: BrowseService
   ) { }

  ngOnInit() {

    this.route.queryParams
      .subscribe(params => 
      {
        this.params = params;
        console.log(this.params.id); 
      });
    
    if (this.params.by == 'genre' || this.params.by == 'title') {
      this.browseService.browseBy(this.params).subscribe(
        data => {
          this.movies = data;
        });
    }
    
  }

}
