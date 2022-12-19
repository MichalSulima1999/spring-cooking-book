import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddRecipeComponent } from './pages/add-recipe/add-recipe.component';
import { MainMenuComponent } from './pages/main-menu/main-menu.component';
import { MyRecipesComponent } from './pages/my-recipes/my-recipes.component';
import { RecipeComponent } from './pages/recipe/recipe.component';

const routes: Routes = [
  { path: '', component: MainMenuComponent },
  { path: 'recipes', component: MyRecipesComponent },
  { path: 'add-recipe', component: AddRecipeComponent },
  { path: 'recipe/:id', component: RecipeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
