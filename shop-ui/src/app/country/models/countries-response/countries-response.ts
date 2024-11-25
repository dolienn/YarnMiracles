import { Country } from '../country/country';

export interface CountriesResponse {
  _embedded: {
    countries: Country[];
  };
}
