import { Component, OnInit } from '@angular/core';
import { Movie } from 'src/app/models/Movie';
import { MovieListService } from 'src/app/services/movie-list.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {

  top20Movies: Movie[];

  constructor(private movieListService: MovieListService) { }

  ngOnInit() {
    this.top20Movies = this.movieListService.getTop20Movies();
  }

}
