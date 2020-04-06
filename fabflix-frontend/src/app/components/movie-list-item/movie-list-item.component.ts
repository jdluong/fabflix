import { Component, OnInit, Input } from '@angular/core';
import { Movie } from 'src/app/models/Movie';

@Component({
  selector: 'app-movie-list-item',
  templateUrl: './movie-list-item.component.html',
  styleUrls: ['./movie-list-item.component.css']
})
export class MovieListItemComponent implements OnInit {

  @Input() m: Movie;
  @Input() rank: number;

  constructor() { }

  ngOnInit() {
  }

}
