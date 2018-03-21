import React, { Component } from 'react';
import './App.css';
import logo from "./coin.png";
import OrderEntry from './components/order_entry';
import OrderView from './components/order_view';
import LoginView from './components/login_view';
import {Grid} from 'react-bootstrap';
import {Row} from 'react-bootstrap';
class App extends Component {

  render() {
    
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="" />
          <h1 className="App-title">Welcome to Hades Capital</h1>
        </header>
        <div>
        <OrderView>
        </OrderView>
        </div>
        {/* <Grid>

        <Row className="text-center"> <LoginView> </LoginView> </Row>

        </Grid> */}
              
      </div>
    );
  }

  
}


export default App;
