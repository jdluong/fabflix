import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MovieListComponent } from './components/movie-list/movie-list.component';
import { SingleMovieComponent } from './components/single-movie/single-movie.component';
import { SingleStarComponent } from './components/single-star/single-star.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { BrowseByGenreComponent } from './components/browse-by-genre/browse-by-genre.component';
import { BrowseByTitleComponent } from './components/browse-by-title/browse-by-title.component';
import { LoginComponent } from './components/login/login.component';

const routes: Routes = [
  { path: 'movie-list', component: MovieListComponent },
  { path: 'movie/:movieId', component: SingleMovieComponent },
  { path: 'star/:starId', component: SingleStarComponent },
  { path: 'genres', component: BrowseByGenreComponent },
  { path: 'titles', component: BrowseByTitleComponent },
  { path: '', redirectTo: '/movie-list', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent },
  { path: 'login', component: LoginComponent },
  { path: '', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
