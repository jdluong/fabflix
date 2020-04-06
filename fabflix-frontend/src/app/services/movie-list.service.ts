import { Injectable } from '@angular/core';
import { Movie } from '../models/Movie';

@Injectable({
  providedIn: 'root'
})
export class MovieListService {

  constructor() { }

  getTop20Movies():Movie[] {
    let movieList:Movie[] = [];
    for (let i = 0; i < 20; i++) {
      movieList[i] = {
        id: 'id${i}',
        title: 'movie'+i,
        year: i,
        director: 'director'+i,
        genres: ['genre0', 'genre1', 'genre2', 'genre3'],
        stars: [
                {id: 'starId0', name: 'starName0', birthYear: 1997},
                {id: 'starId1', name: 'starName1', birthYear: 1998},
                {id: 'starId2', name: 'starName2', birthYear: 1999},
                {id: 'starId3', name: 'starName3', birthYear: 2000}
              ],
        rating: i
      };
    }
    return movieList;
  }

}
