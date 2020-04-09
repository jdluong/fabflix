import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from 'src/app/services/movie.service';
import { Star } from 'src/app/models/Star';
import { Movie } from 'src/app/models/Movie';

@Component({
  selector: 'app-single-star',
  templateUrl: './single-star.component.html',
  styleUrls: ['./single-star.component.css']
})
export class SingleStarComponent implements OnInit {

  star:Star;
  movies:Movie[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService
  ) { }

  ngOnInit() {
    let starId:string;
    starId = this.route.snapshot.paramMap.get('starId');

    this.movieService.getStar(starId).subscribe(
      data => {this.star = data;
              console.log(this.star)}
    );
    this.movieService.getMoviesByStarId(starId).subscribe(
      data => this.movies = data
    );

  }
  
}
