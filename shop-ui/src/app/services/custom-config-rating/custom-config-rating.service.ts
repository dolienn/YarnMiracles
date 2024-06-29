import { Injectable, OnInit } from '@angular/core';
import { StarRatingConfigService } from 'angular-star-rating';

@Injectable({
  providedIn: 'root',
})
export class CustomConfigRatingService
  extends StarRatingConfigService
  implements OnInit
{
  constructor() {
    super();
  }
  ngOnInit(): void {}
}
