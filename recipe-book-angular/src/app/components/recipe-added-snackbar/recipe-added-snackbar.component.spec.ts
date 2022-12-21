import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeAddedSnackbarComponent } from './recipe-added-snackbar.component';

describe('RecipeAddedSnackbarComponent', () => {
  let component: RecipeAddedSnackbarComponent;
  let fixture: ComponentFixture<RecipeAddedSnackbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecipeAddedSnackbarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeAddedSnackbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
