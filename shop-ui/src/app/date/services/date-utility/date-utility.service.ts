import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DateUtilityService {
  constructor() {}

  calculateDateISO(currentDate: Date, years: number): string {
    const date = new Date(currentDate);
    date.setFullYear(date.getFullYear() - years);
    return date.toISOString().split('T')[0];
  }

  calculateFormattedDate(date: Date, yearsAgo: number): string {
    const year = date.getFullYear() - yearsAgo;
    const month = `0${date.getMonth() + 1}`.slice(-2);
    const day = `0${date.getDate()}`.slice(-2);

    return `${year}-${month}-${day}`;
  }
}
