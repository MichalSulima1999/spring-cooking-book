import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import Image from "react-bootstrap/Image";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button";
import axios from "../../api/axios";
import { RECIPES_URL } from "../../api/urlConstants";
import { SKILL_MAP } from "./recipesConstants";
import { Container } from "react-bootstrap";
import ChangeRecipeImage from "./ChangeRecipeImage";

const Recipe = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { recipeId } = useParams();

  const [recipe, setRecipe] = useState([{}]);
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

  return (
    <Container className="rounded mt-3 shadow" style={{ backgroundColor: "#f0f2f0" }}>
      {loading ? (
        <p>Ładowanie</p>
      ) : (
        <>
          <Row className="mt-3">
            <Col md={4} className="mb-3">
              <Image  style={{ marginBottom: "1rem" }}
                fluid
                rounded
                src={
                  recipe.image
                    ? `http://localhost:8080/api/recipe/image/${recipe.image}`
                    : "/chicken.png"
                }
                alt={`${recipe.name} photo`}
              />
              <ChangeRecipeImage recipe={recipe} setRecipe={setRecipe} />
              <h1 className="bg-opacity-50 rounded p-1" style={{ backgroundColor: "#4CAF50" }}>
                {recipe.name}
              </h1>
              <h4><i>{recipe.description}</i></h4>
              <h4>Kategoria: <i>{recipe.category.name}</i></h4>
              <h4>Czas gotowania: <i>{recipe.cookingMinutes} minut</i></h4>
              <h4>Dieta: <i>{recipe.diet.name}</i></h4>
              <h4 style={{ marginBottom: "1rem" }}>Poziom trudności: <i>{SKILL_MAP.get(recipe.skillLevel)}</i></h4>
              <Button style={{ width: "100%" }}
                variant="primary"
                onClick={() =>
                  navigate(`/recipes/edit/${recipe.id}`, {
                    state: {
                      recipe,
                    },
                  })
                }
              >
                Edytuj przepis
              </Button>
            </Col>
            <Col md={8} className="p-3">
              <h2 className="text-center">Składniki</h2>
              {recipe.recipeIngredients.map((ingredient, i) => (
                <div key={i} className="d-flex justify-content-between" style={{ marginLeft: "5rem" ,width: "75%" }}>
                  <h4>{ingredient.ingredient.name}</h4>
                  <h4>{ingredient.quantity} szt.</h4>
                </div>
              ))}

              <h2 className="text-center" style={{ marginTop: "2rem" }}>Przygotowanie</h2>
              {recipe.recipeSteps.map((step, i) => (
                <div className="d-flex justify-content-left" key={i}>
                  <h4>{`${step.stepNumber}. ${step.step.description}`}.</h4>
                </div>
              ))}
            </Col>
          </Row>
        </>
      )}
    </Container>
  );
};

export default Recipe;
