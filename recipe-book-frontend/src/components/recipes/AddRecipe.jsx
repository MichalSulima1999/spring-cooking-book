import React from "react";
import axios from "../../api/axios";
import RecipeValidation from "./RecipeValidation";
import { RECIPES_URL } from "../../api/urlConstants";
import RecipeForm from "./RecipeForm";
import { Container } from "react-bootstrap";

const AddRecipe = () => {
  const schema = RecipeValidation();

  const handleSubmit = async (data) => {
    console.log(data);

    const recipe = {
      recipe: {
        id: 0,
        name: data.name,
        description: data.description,
        cookingMinutes: data.cookingMinutes,
        skillLevel: data.skillLevel,
      },
      category: {
        name: data.categoryName,
      },
      diet: {
        name: data.dietName,
      },
      ingredientQuantityDtos: data.ingredientQuantity,
      stepNumberDtos: data.stepNumber,
    };

    await axios
      .post(RECIPES_URL, recipe)
      .then((res) => {
        console.log("Poprawnie dodano przepis");
      })
      .catch((err) => {
        console.log(err.response);
      });
  };

  return (
    <Container className="bg-light rounded shadow p-3 mt-3">
      <h1 className="text-center bg-success bg-gradient text-light rounded p-2">
        Dodaj przepis
      </h1>
      <RecipeForm handleSubmit={handleSubmit} schema={schema} />
    </Container>
  );
};

export default AddRecipe;
