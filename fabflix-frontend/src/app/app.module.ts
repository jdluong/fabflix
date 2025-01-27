import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { NotifierModule, NotifierOptions } from "angular-notifier";
import { PathLocationStrategy, HashLocationStrategy, LocationStrategy } from '@angular/common';

import { AutocompleteHighlightPipe } from './pipes/autocomplete-highlight.pipe';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MovieListComponent } from './components/movie-list/movie-list.component';
import { SingleMovieComponent } from './components/single-movie/single-movie.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { SingleStarComponent } from './components/single-star/single-star.component';
import { BrowseByGenreComponent } from './components/browse-by-genre/browse-by-genre.component';
import { BrowseByTitleComponent } from './components/browse-by-title/browse-by-title.component';
import { LoginComponent } from './components/login/login.component';
import { MainSearchBrowseComponent } from './components/main-search-browse/main-search-browse.component';
import { CartComponent } from './components/shopping/cart/cart.component';
import { CheckoutComponent } from './components/shopping/checkout/checkout.component';
import { PostPaymentComponent } from './components/shopping/post-payment/post-payment.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RedirectComponent } from './components/redirect/redirect.component';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';

/**
 * Custom angular notifier options
 */
const customNotifierOptions: NotifierOptions = {
  position: {
		horizontal: {
			position: 'right',
			distance: 12
		},
		vertical: {
			position: 'bottom',
			distance: 12,
			gap: 10
		}
	},
  theme: 'material',
  behaviour: {
    autoHide: 7500,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease'
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50
    },
    shift: {
      speed: 300,
      easing: 'ease'
    },
    overlap: 150
  }
};

@NgModule({
  declarations: [
    AppComponent,
    MovieListComponent,
    SingleMovieComponent,
    PageNotFoundComponent,
    SingleStarComponent,
    BrowseByGenreComponent,
    BrowseByTitleComponent,
    LoginComponent,
    MainSearchBrowseComponent,
    CartComponent,
    CheckoutComponent,
    PostPaymentComponent,
    RedirectComponent,
    EmployeeDashboardComponent,
    AutocompleteHighlightPipe
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    FontAwesomeModule,
    NotifierModule.withConfig((customNotifierOptions)),
    MatAutocompleteModule
  ],
  exports: [
    AutocompleteHighlightPipe
  ],
  providers: [
    {provide: LocationStrategy,
    useClass: PathLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
