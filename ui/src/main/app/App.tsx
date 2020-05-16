import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
import './App.css';
import {LoginPage} from "./components/login-page/login-page";
import {RegisterPage} from"./components/register-page/register-page"
import {MainPage} from "./components/main-page/main-page";

function App() {
  return (
    <Router>
      <Switch>
        <Route path='/login' component={LoginPage} />
        <Route path='/register' component={RegisterPage}/>
        <Route path='/' component={MainPage} exact/>
      </Switch>
    </Router>
  );
}

export default App;