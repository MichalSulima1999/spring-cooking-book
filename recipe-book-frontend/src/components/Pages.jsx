import React from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import AddRecipe from "./recipes/AddRecipe";
import Recipes from "./recipes/Recipes";
import Home from "./home/Home";
import Recipe from "./recipes/Recipe";
import EditRecipe from "./recipes/EditRecipe";

function Pages() {
  const location = useLocation();
  return (
    <Routes location={location} key={location.pathname}>
      <Route path="/" element={<Home />} />
      <Route path="/recipes" element={<Recipes />} />
      <Route path="/recipes/:recipeId" element={<Recipe />} />
      <Route path="/recipes/add" element={<AddRecipe />} />
      <Route path="/recipes/edit/:recipeId" element={<EditRecipe />} />
    </Routes>
  );
}

export default Pages;
