export interface RecipeToAdd {
  recipe: {
    name: string;
    description: string;
    cookingMinutes: number;
    skillLevel: string;
  };
  category: {
    name: string;
  };
  diet: {
    name: string;
  };
  ingredientQuantityDtos: [
    {
      ingredientName: string;
      quantity: string;
    }
  ];
  stepNumberDtos: [
    {
      stepDescription: string;
      stepNumber: number;
    }
  ];
}
