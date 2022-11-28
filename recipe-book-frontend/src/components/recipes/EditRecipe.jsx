import React, { useState, useEffect } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import axios from "../../api/axios";
import { RECIPES_URL } from "../../api/urlConstants";
import { Container } from "react-bootstrap";
import RecipeForm from "./RecipeForm";

const EditRecipe = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { recipeId } = useParams();

  const [recipe, setRecipe] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (location?.state?.recipe) {
      console.log("Got recipe");
      setRecipe(location?.state?.recipe);
      setLoading(false);
      return;
    }

    getRecipe();
  }, []);

  const getRecipe = async () => {
    await axios
      .get(`${RECIPES_URL}/${recipeId}`)
      .then((res) => {
        res.data.recipeSteps.sort((a, b) => a.stepNumber - b.stepNumber);
        console.log(res.data);
        setRecipe(res.data);
      })
      .catch((err) => {
        console.log(err.response);
      })
      .then(() => {
        setLoading(false);
      });
  };

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
      .put(`${RECIPES_URL}/${recipeId}`, recipe)
      .then((res) => {
        res.data.recipeSteps.sort((a, b) => a.stepNumber - b.stepNumber);
        console.log(res.data);
        navigate(`/recipes/${recipeId}`, {
          state: {
            recipe: res.data,
          },
        });
      })
      .catch((err) => {
        console.log(err.response);
      });
  };

  return (
    <Container>
      {loading ? (
        "Ładowanie"
      ) : (
        <div>
          <h1>Edytuj przepis</h1>
          <RecipeForm handleSubmit={handleSubmit} initialValues={recipe} />
        </div>
      )}
    </Container>
  );
};

export default EditRecipe;
