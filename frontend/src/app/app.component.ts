import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private static readonly DEFAULT_LANG: string = 'en';

  title = 'app';

  constructor(translate: TranslateService) {
    translate.setDefaultLang(AppComponent.DEFAULT_LANG);
    translate.use(AppComponent.DEFAULT_LANG);
  }
}
