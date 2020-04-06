import { Star } from './Star';

export class Movie {
    id: string;
    title: string;
    year: number;
    director: string;
    genres: string[];
    stars: Star[];
    rating: number;
}