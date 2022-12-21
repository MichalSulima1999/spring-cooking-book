import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxPaginationModule } from 'ngx-pagination';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MainMenuComponent } from './pages/main-menu/main-menu.component';
import { MyRecipesComponent } from './pages/my-recipes/my-recipes.component';
import { AddRecipeComponent } from './pages/add-recipe/add-recipe.component';
import { RecipeCardComponent } from './components/recipe-card/recipe-card.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MaterialExampleModule } from './material.module';
import { RecipeAddedSnackbarComponent } from './components/recipe-added-snackbar/recipe-added-snackbar.component';
import { RecipeComponent } from './pages/recipe/recipe.component';
import { RecipeStepComponent } from './components/recipe-step/recipe-step.component';
import { RecipeIngredientsComponent } from './components/recipe-ingredients/recipe-ingredients.component';
import { RecipeFormComponent } from './components/recipe-form/recipe-form.component';
import { EditRecipeComponent } from './pages/edit-recipe/edit-recipe.component';

@NgModule({
  declarations: [
    AppComponent,
    MainMenuComponent,
    MyRecipesComponent,
    AddRecipeComponent,
    RecipeCardComponent,
    RecipeAddedSnackbarComponent,
    RecipeComponent,
    RecipeStepComponent,
    RecipeIngredientsComponent,
    RecipeFormComponent,
    EditRecipeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    NgxPaginationModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialExampleModule,
    DragDropModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}
