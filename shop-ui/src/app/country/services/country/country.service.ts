import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { CountriesResponse } from '../../models/countries-response/countries-response';
import { Country } from '../../models/country/country';

@Injectable({
  providedIn: 'root',
})
export class CountryService {
  private readonly countriesUrl = `${environment.url}/countries`;

  constructor(private httpClient: HttpClient) {}

  getCountries(): Observable<Country[]> {
    return this.httpClient
      .get<CountriesResponse>(this.countriesUrl)
      .pipe(map((response) => response._embedded.countries));
  }
}
