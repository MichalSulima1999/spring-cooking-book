import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
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
  const navigate = useNavigate();

  const [recipes, setRecipes] = useState([{}]);
  const [searchBody, setSearchBody] = useState({
    searchCriteriaList: [],
    dataOption: "all",
  });

  const [numberOfElements, setNumberOfElements] = useState(0);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(9);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getRecipes();
  }, [page, rowsPerPage, searchBody]);

  const getRecipes = async () => {
    await axios
      .post(
        `${RECIPES_URL}/search?pageNum=${page}&pageSize=${rowsPerPage}`,
        searchBody
      )
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
        <div className="mb-3 p-5 pb-2">
          <Row>
            <Col md={3}>
              <RecipesSearch setSearchBody={setSearchBody} />
            </Col>
            <Col mb={9}>
              {recipes.length > 0 ? (
                <>
                  <Row>
                    {recipes.map((recipe, i) => (
                      <Col
                        md={6}
                        lg={4}
                        className="mb-3 align-self-center"
                        key={i}
                      >
                        <Card className="d-flex flex-column">
                          <CardMedia
                            component="img"
                            height="200"
                            image={
                              recipe.image
                                ? `http://localhost:8080/api/recipe/image/${recipe.image}`
                                : "/chicken.png"
                            }
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
                              onClick={() => navigate(`/recipes/${recipe.id}`)}
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
