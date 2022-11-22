import React from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import AddRecipe from "./recipes/AddRecipe";
import Recipes from "./recipes/Recipes";

function Pages() {
  const location = useLocation();
  return (
    <Routes location={location} key={location.pathname}>
      <Route path="/recipes" element={<Recipes />} />
      <Route path="/recipes/add" element={<AddRecipe />} />
    </Routes>
  );
}

export default Pages;