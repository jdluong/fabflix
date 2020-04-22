import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MovieListComponent } from './components/movie-list/movie-list.component';
import { SingleMovieComponent } from './components/single-movie/single-movie.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SingleStarComponent } from './components/single-star/single-star.component';
import { BrowseByGenreComponent } from './components/browse-by-genre/browse-by-genre.component';
import { BrowseByTitleComponent } from './components/browse-by-title/browse-by-title.component';
import { LoginComponent } from './components/login/login.component';

import { FormsModule } from '@angular/forms';
import { HttpInterceptorService } from './services/httpInterceptor.service';

@NgModule({
  declarations: [
    AppComponent,
    MovieListComponent,
    SingleMovieComponent,
    PageNotFoundComponent,
    SingleStarComponent,
    BrowseByGenreComponent,
    BrowseByTitleComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
