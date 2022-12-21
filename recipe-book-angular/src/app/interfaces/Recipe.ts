export interface Recipe {
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
}
