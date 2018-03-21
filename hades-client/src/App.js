import React, { Component } from 'react';
import './App.css';
import logo from "./coin.png";
import OrderView from './components/OrderView';

class App extends Component {

  render() {
    
    return (
      <div className="App">
      {/* <h1> Home </h1> */}
        <header className="App-header">
          <img src={logo} className="App-logo" alt="" />
          <h1 className="App-title">Welcome to Hades Capital</h1>
        </header>

          <OrderView>
          </OrderView>
            
      </div>
    );
  }

  
}


export default App;
