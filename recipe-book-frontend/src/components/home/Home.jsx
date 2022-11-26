import React from "react";
import { Card, Button, Row } from "react-bootstrap";
import Lottie from "react-lottie"
import animationData2 from './lottie_plus.json';
import animationData3 from './lottie_recipes.json';

const Home = () => {
  const addOptions = {
    loop: true,
    autoplay: true,
    animationData: animationData2,
    rendererSettings: {
      preserveAspectRatio: "xMidYMid slice"
    }
  };
  const recipeOptions = {
    loop: true,
    autoplay: true,
    animationData: animationData3,
    rendererSettings: {
      preserveAspectRatio: "xMidYMid slice"
    }
  };
  return (
    <>
    <Row>
    <Card border="success" bg="light" className="text-center" style={{ width: '20rem', height: '22rem', marginLeft: '15rem', marginTop: '5rem'}}>
      <Card.Body>
        <Card.Title>
        <Lottie 
	    options={recipeOptions}
        height={100}
        width={200}
      />
          Moje przepisy</Card.Title>
        <Card.Text style={{ marginTop: '2rem' }}>
          Wyświetl zapisane przez siebie przepisy!          
        </Card.Text>
        <Button variant="success" style={{ marginTop: '3rem', marginBottom: '2rem' }}>Moje przepisy</Button>
      </Card.Body>
    </Card>
    <Card border="success" bg="light" className="text-center" style={{ width: '20rem', height: '22rem', marginLeft: '15rem', marginTop: '5rem'}}>
        <Card.Body>
          <Card.Title>
          <Lottie 
	    options={addOptions}
        height={100}
        width={100}
      />
            Dodaj przepis
            </Card.Title>
          <Card.Text style={{ marginTop: '2rem' }}>
            Rozszerz swoją książkę kulinarską o nowe potrawy!
          </Card.Text>
          <Button variant="success" style={{ marginTop: '3rem', marginBottom: '2rem' }}>Dodaj przepis</Button>
        </Card.Body>
    </Card>
    </Row>
    
    </>
  );
};

export default Home;
