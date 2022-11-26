import React from "react";
import { useNavigate } from "react-router-dom";
import axios from "../../api/axios";
import { RECIPES_URL } from "../../api/urlConstants";
import RecipeForm from "./RecipeForm";
import { Container } from "react-bootstrap";

const AddRecipe = () => {
  const navigate = useNavigate();

  const handleSubmit = async (data) => {
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
        navigate(`/recipes/${res.data.id}`);
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
      <RecipeForm handleSubmit={handleSubmit} />
    </Container>
  );
};

export default AddRecipe;
