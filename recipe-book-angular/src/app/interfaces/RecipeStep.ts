export interface RecipeStep {
  id?: number;
  step: {
    id?: number;
    description: string;
  };
  stepNumber: number;
}
