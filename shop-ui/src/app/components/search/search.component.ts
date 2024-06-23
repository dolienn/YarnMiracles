import { Component } from '@angular/core';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss',
})
export class SearchComponent {
  toggleSearch() {
    document
      .querySelector('.search-bar')
      ?.classList.toggle('active-search-bar');
    document
      .querySelector('.search-input')
      ?.classList.toggle('active-search-input');

    if (
      window.innerWidth <= 1000 &&
      window.getComputedStyle(document.querySelector('.navmenu')!).position ===
        'relative'
    ) {
      document.querySelector('header')?.classList.add('mobile-search-active');
    }

    if (
      window.innerWidth <= 750 &&
      window.getComputedStyle(document.querySelector('.navmenu')!).position ===
        'absolute'
    ) {
      document
        .querySelector('header')
        ?.classList.remove('mobile-search-active');
    }
  }
}
