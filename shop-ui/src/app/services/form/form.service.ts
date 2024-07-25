import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Country } from '../../common/country/country';

@Injectable({
  providedIn: 'root',
})
export class FormService {
  private readonly countriesUrl = 'http://192.168.1.162:8088/api/v1/countries';

  constructor(private httpClient: HttpClient) {}

  getCountries(): Observable<Country[]> {
    return this.httpClient
      .get<GetResponseCountries>(this.countriesUrl)
      .pipe(map((response) => response._embedded.countries));
  }
}

interface GetResponseCountries {
  _embedded: {
    countries: Country[];
  };
}
