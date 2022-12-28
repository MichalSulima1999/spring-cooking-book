import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { LANGUAGE_KEY } from './constants/local-storage-constants';
import { LocalStorageService } from './services/local-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  constructor(
    private translate: TranslateService,
    private router: Router,
    private localStorageService: LocalStorageService
  ) {
    translate.setDefaultLang('en');
    const localStorageLanguage = localStorageService.getData(LANGUAGE_KEY);
    if (localStorageLanguage) {
      translate.use(localStorageLanguage);
    } else {
      translate.use('en');
    }
  }

  languages = [
    { label: 'English', value: 'en' },
    { label: 'Polski', value: 'pl' },
  ];

  goToPage(pageName: string): void {
    this.router.navigate([`${pageName}`]);
  }

  languageChange(language: any) {
    this.localStorageService.saveData(LANGUAGE_KEY, language.value);
    this.translate.use(language.value);
  }

  hasRoute(route: string) {
    return this.router.url === route;
  }
}
