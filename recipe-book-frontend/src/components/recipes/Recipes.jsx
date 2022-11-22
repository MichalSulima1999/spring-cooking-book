import React, { useState, useEffect } from "react";
import axios from "../../api/axios";
import { RECIPES_URL } from "../../api/urlConstants";
import TablePagination from "@mui/material/TablePagination";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { Button, CardActions } from "@mui/material";
import { Col, Row } from "react-bootstrap";
import RecipesSearch from "./RecipesSearch";

const Recipes = () => {
  const [recipes, setRecipes] = useState([{}]);

  const [numberOfElements, setNumberOfElements] = useState(0);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(9);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getRecipes();
  }, [page, rowsPerPage]);

  const getRecipes = async () => {
    await axios
      .get(`${RECIPES_URL}?pageNum=${page}&pageSize=${rowsPerPage}`)
      .then((res) => {
        console.log(res.data);
        setRecipes(res.data.content);
        setNumberOfElements(res.data.totalElements);
      })
      .catch((err) => {
        console.log(err.response);
      })
      .then(() => {
        setLoading(false);
      });
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(event.target.value);
    setPage(0);
  };

  return (
    <div className="p-3">
      {loading ? (
        <div>Ładowanie...</div>
      ) : (
        <div className="mb-3 p-5 pb-2 dish_search__bg">
          <Row>
            <Col md={4}>
              <RecipesSearch />
            </Col>
            <Col mb={8}>
              {recipes.length > 1 ? (
                <>
                  <Row>
                    {recipes.map((recipe, i) => (
                      <Col
                        md={6}
                        lg={3}
                        className="mb-3 align-self-center"
                        key={i}
                      >
                        <Card
                          sx={{ minHeight: 600 }}
                          className="d-flex flex-column"
                        >
                          <CardMedia
                            component="img"
                            image={`http://localhost:8080/api/dish/image/${recipe.image}`}
                            alt={recipe.name}
                          />
                          <CardContent>
                            <Typography
                              gutterBottom
                              variant="h5"
                              component="div"
                            >
                              {recipe.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              {recipe.description}
                            </Typography>
                          </CardContent>
                          <CardActions className="d-flex justify-content-between mt-auto mb-1">
                            <Button
                              size="small"
                              onClick={() => console.log("Szczegóły")}
                            >
                              Szczegóły
                            </Button>
                            <Typography variant="body2" color="text.secondary">
                              {recipe.cookingMinutes} minut
                            </Typography>
                          </CardActions>
                        </Card>
                      </Col>
                    ))}
                  </Row>
                  <TablePagination
                    className="m-3 bg-success bg-opacity-75 rounded"
                    rowsPerPageOptions={[8, 12, 16]}
                    labelRowsPerPage="Rekordy na stronę"
                    component="div"
                    count={numberOfElements}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                  />
                </>
              ) : (
                <h1 className="bg-light m-5 p-3 rounded shadow text-center">
                  Brak wyników
                </h1>
              )}
            </Col>
          </Row>
        </div>
      )}
    </div>
  );
};

export default Recipes;
