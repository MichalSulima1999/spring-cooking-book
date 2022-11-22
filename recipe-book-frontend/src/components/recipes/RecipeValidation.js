import * as yup from "yup";

const RecipeValidation = () => {
  return yup.object().shape({
    name: yup.string().required("Nazwa przepisu jest wymagana!"),
    description: yup.string().required("Opis jest wymagany!"),
    image: yup
      .mixed()
      .test("is-correct-file", "Dozwolony rozmiar: 10MB", (image) =>
        !image ? true : image.size / 1024 / 1024 < 10
      )
      .test("is-big-file", "Dozwolone typy: webp, jpeg, png", (image) =>
        !image
          ? true
          : ["image/webp", "image/jpeg", "image/png"].includes(image.type)
      ),
    cookingMinutes: yup
      .number()
      .min(1, "Wartość powinna być dodatnia!")
      .required("Pole jest wymagane!"),
    skillLevel: yup
      .string()
      .oneOf(["EASY", "MEDIUM", "HARD"], "Niepoprawny poziom!")
      .required("Pole jest wymagane!"),
    dietName: yup.string().required("Dieta jest wymagana!"),
    stepNumber: yup
      .array()
      .of(
        yup.object().shape({
          stepDescription: yup.string().required("Opis kroku jest wymagany!"),
          stepNumber: yup
            .number()
            .min(1, "Numer kroku nie może być mniejszy od 1!")
            .required("Numer kroku jest wymagany!"),
        })
      )
      .min(1, "Wpisz minimum jeden krok!")
      .required("Kroki są wymagane!"),
    ingredientQuantity: yup
      .array()
      .of(
        yup.object().shape({
          ingredientName: yup
            .string()
            .required("Nazwa składnika jest wymagana!"),
          quantity: yup.string().required("Ilość składników jest wymagana!"),
        })
      )
      .min(1, "Wpisz minimum jeden krok!")
      .required("Kroki są wymagane!"),
    categoryName: yup.string().required("Kategoria jest wymagana!"),
  });
};

export default RecipeValidation;
