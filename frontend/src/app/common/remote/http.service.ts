import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { environment } from "../../../environments/environment";

@Injectable()
export class HttpService {
  public constructor(private http: HttpClient) {
  }

  public get(url): Observable<any> {
    return this.http.get(environment.apiEndpoint + url);
  }
}
