import { Movie } from './Movie';
import { Rating } from './Rating';
import { Genre } from './Genre';
import { Star } from './Star';

export class MovieWithDetails {
    movie:Movie;
    genres:Genre[];
    stars:Star[];
    rating:Rating;
}