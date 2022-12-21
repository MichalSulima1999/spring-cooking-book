import { RecipeIngredient } from './RecipeIngredient';
import { RecipeStep } from './RecipeStep';

export interface RecipeDetails {
  id?: number;
  name: string;
  description: string;
  cookingMinutes: number;
  skillLevel: string;
  image: string;
  category: {
    id?: number;
    name: string;
  };
  diet: {
    id?: number;
    name: string;
  };
  recipeIngredients: [RecipeIngredient];
  recipeSteps: [RecipeStep];
}
