import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { ICountryPair } from './country-pair';
import { HttpService } from '../common/remote/http.service';

import 'rxjs/add/operator/map';

@Injectable()
export class CountryService {
  private readonly baseServiceUrl: string = 'countries/';

  public constructor(private httpService: HttpService) {
  }

  public getNeighbours(isoCode: string): Observable<ICountryPair[]> {
    return this.httpService.get(`${this.baseServiceUrl}${isoCode}/neighbours`);
  }
}
