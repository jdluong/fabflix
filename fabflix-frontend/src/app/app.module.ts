import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MovieListComponent } from './components/movie-list/movie-list.component';
import { SingleMovieComponent } from './components/single-movie/single-movie.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SingleStarComponent } from './components/single-star/single-star.component';

@NgModule({
  declarations: [
    AppComponent,
    MovieListComponent,
    SingleMovieComponent,
    PageNotFoundComponent,
    SingleStarComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
