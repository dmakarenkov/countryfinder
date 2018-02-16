import { CountryService } from './country.service';
import { CountryNeighbourComponent } from './neighbour/country-neighbour.component';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    CountryNeighbourComponent
  ],
  entryComponents: [
    CountryNeighbourComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatTableModule,
    TranslateModule
  ],
  exports: [
    CountryNeighbourComponent
  ],
  providers: [
    CountryService
  ]
})
export class CountryModule {
}
