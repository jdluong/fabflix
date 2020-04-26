import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MovieListComponent } from './components/movie-list/movie-list.component';
import { SingleMovieComponent } from './components/single-movie/single-movie.component';
import { SingleStarComponent } from './components/single-star/single-star.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { BrowseByGenreComponent } from './components/browse-by-genre/browse-by-genre.component';
import { BrowseByTitleComponent } from './components/browse-by-title/browse-by-title.component';
import { LoginComponent } from './components/login/login.component';
import { MainSearchBrowseComponent } from './components/main-search-browse/main-search-browse.component';
import { CartComponent } from './components/shopping/cart/cart.component';
import { RedirectComponent } from './components/redirect/redirect.component';
import {CheckoutComponent} from './components/shopping/checkout/checkout.component';
import {PostPaymentComponent} from './components/shopping/post-payment/post-payment.component';

const routes: Routes = [
  { path: 'movie-list', component: MovieListComponent },
  { path: 'movie/:movieId', component: SingleMovieComponent },
  { path: 'star/:starId', component: SingleStarComponent },
  { path: 'genres', component: BrowseByGenreComponent },
  { path: 'titles', component: BrowseByTitleComponent },
  { path: 'redirect', component: RedirectComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'search', component: MainSearchBrowseComponent },
  { path: 'checkout', component: CheckoutComponent},
  { path: 'cart', component: CartComponent},
  { path: 'post-payment', component: PostPaymentComponent },
  { path: '', component: MainSearchBrowseComponent, pathMatch: 'full'},
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
