import React, { Component } from 'react';
import './App.css';
import logo from "./coin.png"
import OrderEntry from './components/order_entry'

class App extends Component {


  onSelectAlert(eventKey) {
    // alert(`Alert from menu item.\neventKey: ${eventKey}`);
  }

  
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="" />
          <h1 className="App-title">Welcome to Hades Capital</h1>
        </header>
        <OrderEntry>
        </OrderEntry>
              
      </div>
    );
  }

  
}


export default App;
