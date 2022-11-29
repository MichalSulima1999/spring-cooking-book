import { useState, useEffect } from "react";
import axios from "../../api/axios";
import TextField from "@mui/material/TextField";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import FormHelperText from "@mui/material/FormHelperText";
import Select from "@mui/material/Select";
import Button from "@mui/material/Button";
import { InputAdornment } from "@mui/material";
import { Formik, Form } from "formik";
import { RecipeSearchValidation } from "./RecipeValidation";
import { CATEGORIES_URL, DIET_URL } from "../../api/urlConstants";

const schema = RecipeSearchValidation();

const RecipesSearch = ({ setSearchBody }) => {
  const [categories, setCategories] = useState([{}]);
  const [diets, setDiets] = useState([{}]);

  useEffect(() => {
    getCategories();
    getDiets();
  }, []);

  const getCategories = async () => {
    try {
      const response = await axios.get(`${CATEGORIES_URL}/all`);
      setCategories(response.data);
    } catch (err) {
      console.log(err.response);
    }
  };

  const getDiets = async () => {
    try {
      const response = await axios.get(`${DIET_URL}/all`);
      setDiets(response.data);
    } catch (err) {
      console.log(err.response);
    }
  };

  const handleSubmit = async (data) => {
    let search = {
      searchCriteriaList: [],
      dataOption: "all",
      orderByField: "name",
      orderByAscending: true,
    };

    const order = JSON.parse(data.sort);
    search = {
      ...search,
      orderByField: order.orderByField,
      orderByAscending: order.orderByAscending,
    };

    if (data.category) {
      search.searchCriteriaList.push({
        filterKey: "category",
        value: data.category,
        operation: "eq",
      });
    }

    if (data.name) {
      search.searchCriteriaList.push({
        filterKey: "name",
        value: data.name,
        operation: "cn",
      });
    }

    if (data.cookingMinutes) {
      search.searchCriteriaList.push({
        filterKey: "cookingMinutes",
        value: data.cookingMinutes,
        operation: "le",
      });
    }

    if (data.skillLevel) {
      search.searchCriteriaList.push({
        filterKey: "skillLevel",
        value: data.skillLevel,
        operation: "eq",
      });
    }

    if (data.diet) {
      search.searchCriteriaList.push({
        filterKey: "diet",
        value: data.diet,
        operation: "eq",
      });
    }

    console.log(search);

    setSearchBody(search);
  };

  return (
    <div className="m-3 bg-light bg-opacity-75 rounded p-2">
      <Formik
        initialValues={{
          category: "",
          name: "",
          diet: "",
          skillLevel: "",
          cookingMinutes: "",
          sort: JSON.stringify({
            orderByField: "name",
            orderByAscending: true,
          }),
        }}
        validateOnBlur={false}
        validateOnChange={false}
        onSubmit={(values) => handleSubmit(values)}
        validationSchema={schema}
      >
        {({
          handleSubmit,
          handleChange,
          handleBlur,
          values,
          errors,
          touched,
        }) => (
          <Form noValidate onSubmit={handleSubmit}>
            <TextField style={{ marginBottom: "1rem" }}
              fullWidth
              variant="outlined"
              label="Nazwa przepisu"
              name="name"
              value={values.name}
              error={Boolean(touched.name && errors.name)}
              helperText={errors.name}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            <FormControl fullWidth>
              <InputLabel id="category-label">Kategoria</InputLabel>
              <Select style={{ marginBottom: "1rem" }}
                variant="outlined"
                labelId="category-label"
                label="Kategoria"
                name="category"
                value={values.category}
                onChange={handleChange}
                onBlur={handleBlur}
                error={Boolean(touched.category && errors.category)}
              >
                <MenuItem value="">Wybierz kategorię</MenuItem>
                {categories.map((category, i) => (
                  <MenuItem key={i} value={category.name}>
                    {category.name}
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>{errors.category}</FormHelperText>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel id="diet-label">Dieta</InputLabel>
              <Select style={{ marginBottom: "1rem" }}
                variant="outlined"
                labelId="diet-label"
                label="Dieta"
                name="diet"
                value={values.diet}
                onChange={handleChange}
                onBlur={handleBlur}
                error={Boolean(touched.diet && errors.diet)}
              >
                <MenuItem value="">Wybierz dietę</MenuItem>
                {diets.map((diet, i) => (
                  <MenuItem key={i} value={diet.name}>
                    {diet.name}
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>{errors.diet}</FormHelperText>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel id="skill-label">Poziom trudności</InputLabel>
              <Select style={{ marginBottom: "1rem" }}
                variant="outlined"
                labelId="skill-label"
                label="Poziom trudności"
                name="skillLevel"
                value={values.skillLevel}
                onChange={handleChange}
                onBlur={handleBlur}
                error={Boolean(touched.skillLevel && errors.skillLevel)}
              >
                <MenuItem value="">Wybierz poziom trudności</MenuItem>
                <MenuItem value="EASY">Łatwy</MenuItem>
                <MenuItem value="MEDIUM">Średni</MenuItem>
                <MenuItem value="HARD">Trudny</MenuItem>
              </Select>
              <FormHelperText>{errors.skillLevel}</FormHelperText>
            </FormControl>
            <TextField style={{ marginBottom: "1rem" }}
              fullWidth
              variant="outlined"
              label="Maksymalny czas gotowania"
              name="cookingMinutes"
              endAdornment={<InputAdornment position="end">minut</InputAdornment>}
              value={values.cookingMinutes}
              error={Boolean(touched.cookingMinutes && errors.cookingMinutes)}
              helperText={errors.cookingMinutes}
              onChange={handleChange}
              onBlur={handleBlur}
            />
            <FormControl fullWidth>
              <InputLabel id="sort-label">Sortuj po</InputLabel>
              <Select style={{ marginBottom: "1rem" }}
                variant="outlined"
                labelId="sort-label"
                label="Sortuj po"
                name="sort"
                value={values.sort}
                onChange={handleChange}
                onBlur={handleBlur}
                error={Boolean(touched.sort && errors.sort)}
              >
                <MenuItem
                  value={JSON.stringify({
                    orderByField: "name",
                    orderByAscending: true,
                  })}
                >
                  Nazwa
                </MenuItem>
                <MenuItem
                  value={JSON.stringify({
                    orderByField: "cookingMinutes",
                    orderByAscending: true,
                  })}
                >
                  Czas gotowania
                </MenuItem>
              </Select>
              <FormHelperText>{errors.sort}</FormHelperText>
            </FormControl>
            <div className="d-grid gap-2 col-6 mx-auto" style={{ marginBottom: "1rem" }}>
              <Button variant="outlined" type="submit">
                Wyszukaj
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
};

export default RecipesSearch;
