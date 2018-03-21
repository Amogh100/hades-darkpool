import React, { Component } from "react";
import axios from "axios";
import { BootstrapTable, TableHeaderColumn } from "react-bootstrap-table";
import OrderEntry from "./OrderEntry";
import AuthorizationService from "../services/AuthorizationService";

/** 
 * OrderView is a component for displaying open orders and capital positions.
*/
class OrderView extends Component {
  
  constructor(props) {
    super(props);
    this.state = { orders: [] };
    this.auth = new AuthorizationService();
    var jwt = this.auth.getToken();

    if(jwt !== undefined && jwt !== null){
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
      /**
     * Should theoretically never reach this case
     * since this component is only routed when a valid token exists, do
     * to componentWillMount
     * but better safe then sorry.
     */
    else {
      console.log("Couldn't find token!!");
      this.setState({validSession: false});
    }

  }

  componentWillMount(){
    if(!this.auth.validTokenExists()){
      window.location.replace("/login");
    }
  }

  /**
   * This function is a scheduled function for polling for open order data.
   */
  updateOrders() {
    axios
      .get("http://localhost:8080/order")
      .then(response => {
          console.log(response.data);
          this.setState({ orders: response.data });
      });
  }

  /** 
   * Scheduled function for poling for trader state information (for now just capital)
  */
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
          <TableHeaderColumn dataField="size">Size </TableHeaderColumn>

          
        </BootstrapTable>
        </div>
        </div>;
    return mainComponent;
  }
}
export default OrderView;
