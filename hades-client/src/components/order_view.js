import { DropdownButton } from "react-bootstrap";
import { MenuItem } from "react-bootstrap";
import React, { Component } from "react";
import styles from "./order_entry";
import axios from "axios";
import { BootstrapTable, TableHeaderColumn } from "react-bootstrap-table";
import LoginView from "./login_view";
import OrderEntry from "./order_entry";

class OrderView extends Component {
  
  constructor(props) {
    super(props);
    this.state = { orders: [] };
    var jwt = sessionStorage["jwt"];
    console.log(jwt);
    if(jwt != undefined && jwt !== null){
      console.log("Found token");
      this.state = {validSession: true};
      axios.defaults.headers.common['Authorization'] = jwt;
      this.updateOrders = this.updateOrders.bind(this);
      this.updateOrders = this.updateOrders.bind(this);
      this.updateTraderState = this.updateTraderState.bind(this);
      this.updateOrders();
      setInterval(this.updateOrders, 1000);
      setInterval(this.updateTraderState, 1000);

    }
    else {
      console.log("Couldn't find token!!");
      this.setState({validSession: false});
    }

  }


  updateOrders() {
    axios
      .get("http://localhost:8080/order")
      .then(response => {
          console.log(response.data);
          this.setState({ orders: response.data });
      });
  }

  updateTraderState(){
    axios
      .get("http://localhost:8080/user/info")
      .then(response => {
        console.log(response.data)
        //For now only update the capital view
        this.setState({capital: response.data.account.capital});
      })
  }

  render() {
    let options = {
      sortName: "globalOrderId",
      sortOrder: "desc"
    };
    var mainComponent = 
    <div>
    <div>
    <input type="text" id="test" value={this.state.capital}/>

    </div>
     <div>
        <OrderEntry>
        </OrderEntry>
     </div>
      <div>
        <h1>Open Orders</h1>
        <BootstrapTable
          options={options}
          data={this.state.orders}
          width={5}
          height={5}
          scrollTop={"Bottom"}
          replace={true}
        >
          <TableHeaderColumn isKey={true} dataField="globalOrderId">
            Order ID
          </TableHeaderColumn>
          <TableHeaderColumn dataField="ticker">Ticker</TableHeaderColumn>
          <TableHeaderColumn dataField="price">Price</TableHeaderColumn>
          <TableHeaderColumn dataField="bid">Bid </TableHeaderColumn>
        </BootstrapTable>
        </div>
        </div>;

    if(this.state.validSession){
      return mainComponent;
    }
    else {
      console.log(this.state.validSession);
      return <LoginView> </LoginView>;
    }
  }
}
export default OrderView;
