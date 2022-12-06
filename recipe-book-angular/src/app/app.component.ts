import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  constructor(private translate: TranslateService, private router: Router) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  languages = [
    { label: 'English', value: 'en' },
    { label: 'Polski', value: 'pl' },
  ];

  goToPage(pageName: string): void {
    this.router.navigate([`${pageName}`]);
  }

  languageChange(language: any) {
    this.translate.use(language.value);
  }

  hasRoute(route: string) {
    return this.router.url === route;
  }
}
