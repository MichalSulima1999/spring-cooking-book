import React, { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";

const NavBar = () => {
  const location = useLocation();
  const [url, setUrl] = useState(null);

  useEffect(() => {
    setUrl(location.pathname);
  }, [location]);

  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand>
          <Link to="/" className={"nav-link" + (url === "/" ? " active" : "")}>
            Książka kucharska
          </Link>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link
              className={"nav-link" + (url === "/" ? " active" : "")}
              as={Link}
              to="/"
            >
              Strona główna
            </Nav.Link>
            <Nav.Link
              className={url === "/recipes" ? " active" : ""}
              as={Link}
              to="/recipes"
            >
              Moje przepisy
            </Nav.Link>
            <Nav.Link
              className={url === "/recipes/add" ? "active" : ""}
              as={Link}
              to="/recipes/add"
            >
              Dodaj przepis
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavBar;
