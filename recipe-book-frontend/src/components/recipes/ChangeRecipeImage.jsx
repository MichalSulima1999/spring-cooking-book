import React from "react";
import { Formik } from "formik";
import axios from "../../api/axios";
import { imageValidation } from "./RecipeValidation";
import { RECIPES_IMAGE_URL } from "../../api/urlConstants";
import { Button } from "@mui/material";
import { Form } from "react-bootstrap";
import AddAPhotoIcon from '@mui/icons-material/AddAPhoto';

const ChangeRecipeImage = ({ recipe, setRecipe }) => {
  const schema = imageValidation();

  const handleSubmit = async (data) => {
    const formData = new FormData();
    formData.append("img", data.image);

    try {
      const response = await axios.patch(
        `${RECIPES_IMAGE_URL}/${recipe.id}`,
        formData,
        {
          headers: {
            "Content-type": `multipart/form-data; boundary=${formData._boundary}`,
          },
        }
      );
      setRecipe({
        ...recipe,
        image: response.data,
      });
    } catch (err) {
      console.log(err.response);
    }
  };

  return (
    <div>
      <Formik
        initialValues={{ image: null }}
        onSubmit={(values) => handleSubmit(values)}
        validationSchema={schema}
      >
        {({ handleSubmit, setFieldValue, values, errors }) => (
          <Form noValidate onSubmit={handleSubmit} className="mb-3">
            <Form.Group
              md="5"
              controlId="validationFormikImage"
              className="mb-1"
            >
              <Form.Control
                name="image"
                type="file"
                onChange={(event) => {
                  setFieldValue("image", event.currentTarget.files[0]);
                }}
                isInvalid={!!errors.image}
                accept="image/webp, image/jpeg, image/png"
              />
              <Form.Control.Feedback type="invalid">
                {errors.image}
              </Form.Control.Feedback>
            </Form.Group>
            <Button
              startIcon={<AddAPhotoIcon />}
              fullWidth
              variant="contained"
              type="submit"
              disabled={errors.image || !values.image}
            >
              Zmień zdjęcie
            </Button>
          </Form>
        )}
      </Formik>
    </div>
  );
};

export default ChangeRecipeImage;
