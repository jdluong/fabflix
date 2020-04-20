import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from 'src/app/services/movie.service';
import { Genre } from 'src/app/models/Genre';

@Component({
  selector: 'app-browse-by-genre',
  templateUrl: './browse-by-genre.component.html',
  styleUrls: ['./browse-by-genre.component.css']
})
export class BrowseByGenreComponent implements OnInit {

  genres: Genre[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService
  ) { }

  ngOnInit() {
    this.movieService.getAllGenres().subscribe(
      data => {
        this.genres = data;
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
