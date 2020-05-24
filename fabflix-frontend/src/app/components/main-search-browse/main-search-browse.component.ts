import { Component, OnInit } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { ActivatedRoute, Router } from '@angular/router';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { AuthenticationService } from 'src/app/services/auth.service';
import { Title } from '@angular/platform-browser';
// import { FormControl } from '@angular/forms';
import {Subject} from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-main-search-browse',
  templateUrl: './main-search-browse.component.html',
  styleUrls: ['./main-search-browse.component.css']
})
export class MainSearchBrowseComponent implements OnInit {

  advancedSearch:boolean;
  fullTextSearch:boolean;
  suggestions:String[];
  term$ = new Subject<string>();

  title:string;
  year:number;
  director:string;
  star:string;
  searchIcon = faSearch;

  isAuth: any;
  userType: string;

  constructor(
    private searchService: SearchService,
    private router: Router,
    private authService: AuthenticationService
  ) {
    this.term$.pipe(debounceTime(300), distinctUntilChanged()).subscribe(
      term => {
        if (term.length >= 3) {
          console.log("FINDING SUGGESTIONS FOR: "+term);
          
          this.searchService.getSuggestions({"title":term}).subscribe(
            data => {
              this.suggestions = data;
              console.log("SUGGESTIONS" + this.suggestions);
          });
        
        }
      });
  }

  ngOnInit() {
    this.fullTextSearch = true;
    this.authService.isAuth().subscribe(
      data => {
        this.isAuth = data['isAuth'];
        if (this.isAuth == false) {
          this.router.navigate(['/redirect']);
        }
        else {
          if (data['Employee']) {
            this.userType = 'Employee';
          }
          else if (data['Customer']) {
            this.userType = 'Customer';  
          }
        }
      });
  }

  changeSearchType(type:string) {
    if (type == "advanced") {
      this.fullTextSearch = false;
      this.advancedSearch = true;
    }
    else if (type == "fulltext") {
      this.advancedSearch = false;
      this.fullTextSearch = true;
    }
  }

  goToMovie(title:string) {
    console.log("******"+title);
  }

  onSubmit() {
    let searchParams = {};
    let searchType = "";
    if (this.fullTextSearch) {
      searchType = "fulltext";
    }
    else {
      searchType = "advanced";
    }
    searchParams = {...searchParams, ...{'by': searchType}};
    if (this.title) { searchParams = {...searchParams, ...{'title':this.title}};}
    if (this.year) { searchParams = {...searchParams, ...{'year':this.year}};}
    if (this.director) { searchParams = {...searchParams, ...{'director':this.director}};}
    if (this.star) { searchParams = {...searchParams, ...{'star':this.star}};}
    let params = {...{'perPage': 25, 'page': 1}, ...searchParams};
    this.router.navigate(['/movie-list'], { queryParams: params });
  }

}
