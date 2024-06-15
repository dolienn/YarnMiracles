import { Component, HostListener } from '@angular/core';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.scss',
})
export class NavigationBarComponent {
  @HostListener('window:scroll', [])
  onWindowScroll() {
    const header = document.querySelector('header');
    header?.classList.toggle('sticky', window.scrollY > 0);
  }

  toggleMenu() {
    document.querySelector('#menu-icon')?.classList.toggle('bx-x');
    document.querySelector('.navmenu')?.classList.toggle('open');
  }
}
