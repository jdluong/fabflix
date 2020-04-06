import { Component, OnInit } from '@angular/core';
import { Movie } from 'src/app/models/Movie';
import { MovieListService } from 'src/app/services/movie-list.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {

  topMovies: Movie[];
  numMovies: number = 20;

  constructor(private movieListService: MovieListService) { }

  ngOnInit() {
    this.topMovies = this.movieListService.getTop20Movies();
    console.log(this.topMovies);
  }

  get3GenresNames(m:Movie):string[] {
    return m.genres.slice(0,3);
  }
  
  get3StarsNames(m:Movie):string[] {
    let starsNames:string[] = [];
    for (let i = 0; i < 3; i++) {
      starsNames.push(m.stars[i].name);
    }

    return starsNames;
  }

}
