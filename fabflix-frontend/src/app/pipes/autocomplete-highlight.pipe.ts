import { Pipe, PipeTransform } from '@angular/core';
import { SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'autocompleteHighlight'
})
export class AutocompleteHighlightPipe implements PipeTransform {
  transform(text: string, search): string {
    const pattern = search
      .replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&")
      .split(' ')
      .filter(t => t.length > 0)
      .join('|');
    const regex = new RegExp(pattern, 'gi');
    // console.log(search ? text.replace(regex, match => `<b>${match}</b>`) : text);
    return search ? text.replace(regex, match => `<b class='highlight'>${match}</b>`) : text;
  }
  
}

