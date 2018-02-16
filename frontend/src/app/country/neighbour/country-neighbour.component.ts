import { Component, OnInit } from '@angular/core';
import { StringUtils } from '../../common/util/string-utils';
import { ICountryPair } from '../country';
import { CountryService } from '../country.service';
import { MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-country-neighbour',
  templateUrl: './country-neighbour.component.html',
  styleUrls: ['./country-neighbour.component.css']
})
export class CountryNeighbourComponent implements OnInit {
  public displayedColumns = ['countryLeft', 'countryRight'];
  public isoCode: string;
  public neighbourPairs: MatTableDataSource<ICountryPair> = new MatTableDataSource<ICountryPair>();

  public isLoading: boolean = false;
  public isError: boolean = false;

  constructor(private countryService: CountryService) { }

  ngOnInit() {
  }

  findNeighbours(): void {
    if (StringUtils.isEmpty(this.isoCode)) {
      return;
    }

    console.log('findNeighbours(' + this.isoCode + ')');

    this.isError = false;
    this.isLoading = true;
    this.countryService.getNeighbours(this.isoCode.trim())
      .subscribe(
        (countryPairs: ICountryPair[]) => {
          console.log('Countries retrieved');
          this.neighbourPairs.data = countryPairs;
          this.isLoading = false;
        },
        (error: any) => {
          console.log('Error while retrieving pairs: ' + error.message);
          this.isLoading = false;
          this.isError = true;
        });
  }
}
