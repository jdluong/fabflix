import { Component, OnInit } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { ActivatedRoute, Router } from '@angular/router';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-main-search-browse',
  templateUrl: './main-search-browse.component.html',
  styleUrls: ['./main-search-browse.component.css']
})
export class MainSearchBrowseComponent implements OnInit {

  title:string;
  year:number;
  director:string;
  star:string;
  searchIcon = faSearch;

  constructor(
    private searchService: SearchService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  notNumber() {
    if (isNaN(Number(this.year))) {
      console.log(Number(this.year));
      return false;
    }
    return true;
  }

  onSubmit() {
    let searchParams = {};
    if (this.title) { searchParams = {...searchParams, ...{'title':this.title}};}
    if (this.year) { searchParams = {...searchParams, ...{'year':this.year}};}
    if (this.director) { searchParams = {...searchParams, ...{'director':this.director}};}
    if (this.star) { searchParams = {...searchParams, ...{'star':this.star}};}
    let params = {...{'perPage': 25, 'page': 1}, ...searchParams};
    this.router.navigate(['/movie-list'], { queryParams: params});

  }

}
