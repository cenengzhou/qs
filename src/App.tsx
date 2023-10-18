import { ROUTE_LIST } from "./constants/route"
import Header from "./components/Header";
import RouteNavigator from "./components/RouteNavigator"
import './App.css';

function App() {
  return (
    <>
      <Header></Header>
      <RouteNavigator routeList={ROUTE_LIST} />
    </>
  );
}

export default App;
