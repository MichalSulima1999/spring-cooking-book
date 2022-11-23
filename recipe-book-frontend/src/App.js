import Footer from "./components/Footer";
import NavBar from "./components/NavBar";
import Pages from "./components/Pages";

function App() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <NavBar />
      <Pages />
      <Footer />
    </div>
  );
}

export default App;
