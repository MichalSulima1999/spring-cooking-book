import React, { useState, useEffect } from "react";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import Button from "@mui/material/Button";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { Formik, FieldArray, getIn } from "formik";
import axios from "../../api/axios";
import {
  CATEGORIES_URL,
  DIET_URL,
  INGREDIENTS_URL,
  STEPS_URL,
} from "../../api/urlConstants";
import RecipeValidation from "./RecipeValidation";
import { FormHelperText } from "@mui/material";

const RecipeForm = ({ handleSubmit, initialValues }) => {
  const schema = RecipeValidation();
  const [ingredientHints, setIngredientHints] = useState([]);
  const [init, setInit] = useState({
    name: "",
    description: "",
    cookingMinutes: 0,
    skillLevel: "EASY",
    categoryName: "",
    dietName: "",
    ingredientQuantity: [
      {
        ingredientName: "",
        quantity: "",
      },
    ],
    stepNumber: [
      {
        stepDescription: "",
        stepNumber: 1,
      },
    ],
  });
  const [categoryHints, setCategoryHints] = useState([]);
  const [stepHints, setStepHints] = useState([]);
  const [dietHints, setDietHints] = useState([]);

  useEffect(() => {
    if (initialValues) {
      console.log(initialValues);
      setInit({
        name: initialValues.name,
        description: initialValues.description,
        cookingMinutes: initialValues.cookingMinutes,
        skillLevel: initialValues.skillLevel,
        categoryName: initialValues.category.name,
        dietName: initialValues.diet.name,
        ingredientQuantity: initialValues.recipeIngredients.map((ingr) => ({
          ingredientName: ingr.ingredient.name,
          quantity: ingr.quantity,
        })),
        stepNumber: initialValues.recipeSteps.map((step) => ({
          stepDescription: step.step.description,
          stepNumber: step.stepNumber,
        })),
      });
      console.log(initialValues);
    }
  }, []);

  const getIngredientHints = async (ingredientName) => {
    if (ingredientName.length < 3) {
      setIngredientHints([]);
      return;
    }

    try {
      const response = await axios.get(
        `${INGREDIENTS_URL}?name=${ingredientName}`
      );
      setIngredientHints(response.data);
    } catch (err) {
      console.log(err.response);
    }
  };

  const getCategoryHints = async (categoryName) => {
    if (categoryName.length < 3) {
      setCategoryHints([]);
      return;
    }

    try {
      const response = await axios.get(
        `${CATEGORIES_URL}?name=${categoryName}`
      );
      setCategoryHints(response.data);
    } catch (err) {
      console.log(err.response);
    }
  };

  const getStepHints = async (stepDescription) => {
    if (stepDescription.length < 3) {
      setStepHints([]);
      return;
    }

    try {
      const response = await axios.get(
        `${STEPS_URL}?description=${stepDescription}`
      );
      setStepHints(response.data);
    } catch (err) {
      console.log(err.response);
    }
  };

  const getDietHints = async (dietName) => {
    if (dietName.length < 3) {
      setDietHints([]);
      return;
    }

    try {
      const response = await axios.get(`${DIET_URL}?name=${dietName}`);
      setDietHints(response.data);
    } catch (err) {
      console.log(err.response);
    }
  };

  return (
    <div className="text-start">
      <Formik
        initialValues={init}
        enableReinitialize={true}
        onSubmit={(values, { resetForm }) => handleSubmit(values, resetForm)}
        validationSchema={schema}
      >
        {({
          handleSubmit,
          handleChange,
          handleBlur,
          values,
          touched,
          errors,
        }) => (
          <Form noValidate onSubmit={handleSubmit}>
            <TextField
              fullWidth
              variant="outlined"
              label="Nazwa przepisu"
              name="name"
              value={values.name}
              required
              error={Boolean(touched.name && errors.name)}
              helperText={errors.name}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            <TextField
              fullWidth
              variant="outlined"
              label="Opis przepisu"
              name="description"
              value={values.description}
              required
              error={Boolean(touched.description && errors.description)}
              helperText={errors.description}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            <TextField
              fullWidth
              variant="outlined"
              type="number"
              label="Czas gotowania w minutach"
              name="cookingMinutes"
              value={values.cookingMinutes}
              required
              error={Boolean(touched.cookingMinutes && errors.cookingMinutes)}
              helperText={errors.cookingMinutes}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            <FormControl fullWidth>
              <InputLabel id="skill-label">Poziom trudności</InputLabel>
              <Select
                variant="outlined"
                labelId="skill-label"
                label="Poziom trudności"
                name="skillLevel"
                value={values.skillLevel}
                onChange={handleChange}
                onBlur={handleBlur}
                required
                error={Boolean(touched.skillLevel && errors.skillLevel)}
              >
                <MenuItem value="EASY">Łatwy</MenuItem>
                <MenuItem value="MEDIUM">Średni</MenuItem>
                <MenuItem value="HARD">Trudny</MenuItem>
              </Select>
              <FormHelperText>{errors.skillLevel}</FormHelperText>
            </FormControl>
            <Autocomplete
              freeSolo
              id="categoryName"
              disableClearable
              options={categoryHints.map((el) => el.name)}
              value={values.categoryName}
              getItemValue={values.categoryName}
              renderInput={(params) => (
                <TextField
                  {...params}
                  variant="outlined"
                  label="Kategoria"
                  name="categoryName"
                  required
                  error={Boolean(touched.categoryName && errors.categoryName)}
                  helperText={errors.categoryName}
                  onChange={(e) => {
                    handleChange(e);
                    getCategoryHints(e.target.value);
                  }}
                  onBlur={handleBlur}
                />
              )}
            />
            <Autocomplete
              freeSolo
              id="dietName"
              disableClearable
              options={dietHints.map((el) => el.name)}
              value={values.dietName}
              getItemValue={values.dietName}
              renderInput={(params) => (
                <TextField
                  {...params}
                  variant="outlined"
                  label="Dieta"
                  name="dietName"
                  required
                  error={Boolean(touched.dietName && errors.dietName)}
                  helperText={errors.dietName}
                  onChange={(e) => {
                    handleChange(e);
                    getDietHints(e.target.value);
                  }}
                  onBlur={handleBlur}
                />
              )}
            />

            <h1>Składniki:</h1>
            <FieldArray name="ingredientQuantity">
              {({ push, remove }) => (
                <div>
                  {values.ingredientQuantity.map((p, index) => {
                    const ingredientName = `ingredientQuantity[${index}].ingredientName`;
                    const touchedIngredientName = getIn(
                      touched,
                      ingredientName
                    );
                    const errorIngredientName = getIn(errors, ingredientName);

                    const quantity = `ingredientQuantity[${index}].quantity`;
                    const touchedQuantity = getIn(touched, quantity);
                    const errorQuantity = getIn(errors, quantity);

                    return (
                      <div key={p.id}>
                        <Row>
                          <Col md={9}>
                            <Autocomplete
                              freeSolo
                              id={`Name ${index}`}
                              disableClearable
                              options={ingredientHints.map((el) => el.name)}
                              getItemValue={p.ingredientName}
                              value={p.ingredientName}
                              renderInput={(params) => (
                                <TextField
                                  {...params}
                                  variant="outlined"
                                  label={`Składnik ${index + 1}`}
                                  name={ingredientName}
                                  required
                                  error={Boolean(
                                    touchedIngredientName && errorIngredientName
                                  )}
                                  helperText={errorIngredientName}
                                  onChange={(e) => {
                                    handleChange(e);
                                    getIngredientHints(e.target.value);
                                  }}
                                  onBlur={handleBlur}
                                />
                              )}
                            />
                          </Col>
                          <Col md={2}>
                            <TextField
                              variant="outlined"
                              label={`Ilość ${index + 1}`}
                              name={quantity}
                              value={p.quantity}
                              required
                              error={Boolean(touchedQuantity && errorQuantity)}
                              helperText={errorQuantity}
                              onChange={handleChange}
                              onBlur={handleBlur}
                            />
                          </Col>
                          <Col md={1}>
                            <Button
                              margin="normal"
                              type="button"
                              color="secondary"
                              variant="outlined"
                              onClick={() => remove(index)}
                            >
                              x
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    );
                  })}
                  <Button
                    type="button"
                    variant="outlined"
                    onClick={() => push({ ingredientName: "", quantity: "" })}
                  >
                    Dodaj składnik
                  </Button>
                </div>
              )}
            </FieldArray>

            <h1>Przygotowanie:</h1>
            <FieldArray name="stepNumber">
              {({ push, remove }) => (
                <div>
                  {values.stepNumber.map((p, index) => {
                    const stepDescription = `stepNumber[${index}].stepDescription`;
                    const touchedStepDescription = getIn(
                      touched,
                      stepDescription
                    );
                    const errorStepDescription = getIn(errors, stepDescription);

                    const stepNumber = `stepNumber[${index}].stepNumber`;
                    const touchedStepNumber = getIn(touched, stepNumber);
                    const errorStepNumber = getIn(errors, stepNumber);

                    return (
                      <div key={p.id}>
                        <Row>
                          <Col md={10}>
                            <Autocomplete
                              freeSolo
                              id={`Step-description-${index}`}
                              disableClearable
                              options={stepHints.map((el) => el.description)}
                              value={p.stepDescription}
                              getItemValue={p.stepDescription}
                              renderInput={(params) => (
                                <TextField
                                  {...params}
                                  variant="outlined"
                                  multiline
                                  label={`Krok ${index + 1}`}
                                  name={stepDescription}
                                  required
                                  error={Boolean(
                                    touchedStepDescription &&
                                      errorStepDescription
                                  )}
                                  helperText={errorStepDescription}
                                  onChange={(e) => {
                                    handleChange(e);
                                    getStepHints(e.target.value);
                                  }}
                                  onBlur={handleBlur}
                                />
                              )}
                            />
                          </Col>
                          <Col md={1}>
                            <TextField
                              variant="outlined"
                              disabled
                              name={stepNumber}
                              value={p.stepNumber}
                              required
                              error={Boolean(
                                touchedStepNumber && errorStepNumber
                              )}
                              onChange={handleChange}
                              onBlur={handleBlur}
                            />
                          </Col>
                          <Col md={1}>
                            <Button
                              margin="normal"
                              type="button"
                              color="secondary"
                              variant="outlined"
                              onClick={() => remove(index)}
                            >
                              x
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    );
                  })}
                  <Button
                    type="button"
                    variant="outlined"
                    onClick={() =>
                      push({
                        stepDescription: "",
                        stepNumber: values.stepNumber.length + 1,
                      })
                    }
                  >
                    Dodaj krok
                  </Button>
                </div>
              )}
            </FieldArray>
            <Button variant="outlined" color="success" type="submit">
              Zapisz przepis
            </Button>
          </Form>
        )}
      </Formik>
    </div>
  );
};

export default RecipeForm;
