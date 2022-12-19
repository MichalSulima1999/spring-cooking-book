export interface RecipeIngredient {
  id?: number;
  ingredient: {
    id?: number;
    name: string;
  };
  quantity: string;
}
