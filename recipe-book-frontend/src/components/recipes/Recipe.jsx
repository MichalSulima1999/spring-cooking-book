import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Image from "react-bootstrap/Image";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Button from "react-bootstrap/Button";
import { useParams } from "react-router-dom";
import axios from "../../api/axios";
import { RECIPES_URL } from "../../api/urlConstants";
import { SKILL_MAP } from "./recipesConstants";
import { Container } from "react-bootstrap";

const Recipe = () => {
  const navigate = useNavigate();
  const { recipeId } = useParams();

  const [recipe, setRecipe] = useState([{}]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
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
    <Container className="bg-light rounded mt-3 shadow">
      {loading ? (
        <p>Ładowanie</p>
      ) : (
        <>
          <Row className="mt-3">
            <Col md={4} className="mb-3">
              <Image
                fluid
                rounded
                src={
                  recipe.image
                    ? `http://localhost:8080/api/recipe/image/${recipe.image}`
                    : "/chicken.png"
                }
                alt={`${recipe.name} photo`}
              />
              <h1 className="bg-success bg-opacity-50 rounded p-2">
                {recipe.name}
              </h1>
              <h3>{recipe.description}</h3>
              <h3>Kategoria: {recipe.category.name}</h3>
              <h3>Czas gotowania: {recipe.cookingMinutes} minut</h3>
              <h3>Dieta: {recipe.diet.name}</h3>
              <h3>Poziom trudności: {SKILL_MAP.get(recipe.skillLevel)}</h3>
              <Button
                variant="primary"
                onClick={() => navigate(`/recipes/edit/${recipe.id}`)}
              >
                Edytuj przepis
              </Button>
            </Col>
            <Col md={8} className="p-3">
              <h2 className="text-center">Składniki</h2>
              {recipe.recipeIngredients.map((ingredient, i) => (
                <div key={i} className="d-flex justify-content-between">
                  <h4>{ingredient.ingredient.name}</h4>
                  <h4>{ingredient.quantity}</h4>
                </div>
              ))}

              <h2 className="text-center">Przygotowanie</h2>
              {recipe.recipeSteps.map((step, i) => (
                <div key={i}>
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
