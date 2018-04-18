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
    this.state = { orders: [], trades: []};
    this.auth = new AuthorizationService();
    var jwt = this.auth.getToken();

    if(jwt !== undefined && jwt !== null){
      console.log("Found token");
      this.setState({validSession: true});
      axios.defaults.headers.common['Authorization'] = jwt;
      this.updateOrders = this.updateOrders.bind(this);
      this.updateOrders = this.updateOrders.bind(this);
      this.updateTraderState = this.updateTraderState.bind(this);
      this.updateTrades = this.updateTrades.bind(this);

      this.updateOrders();
      setInterval(this.updateOrders, 1000);
      setInterval(this.updateTraderState, 1000);
      setInterval(this.updateTrades, 1000);


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

  updateTrades() {
    axios
      .get("http://localhost:8080/trades")
      .then(response => {
          console.log(response.data);
          this.setState({ trades: response.data });
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
    <div style={{color: '#e6e600', fontSize: '20px'}}>
    <label> Current Capital: {this.state.capital} USD</label>
    </div>
     <div style={{margin: "10px"}}>
       
        <OrderEntry>
        </OrderEntry>
     </div>
      <div>
        <h1>Open Orders</h1>
        <BootstrapTable  
          options={options}
          data={this.state.orders}
          scrollTop={"Bottom"}
          replace={true}         
          tableStyle={ { border: '#ffffff 2.5px solid', font: '#ffffff'} }
          bodyStyle={ { border: 'green 1px solid', font: '#ffffff'} }> 
        
          <TableHeaderColumn isKey={true} dataField="globalOrderId" width='5'>Order ID</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="ticker">Ticker</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="price">Price</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="bid">Bid </TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="size">Size </TableHeaderColumn>

          
        </BootstrapTable>
        </div>
        <div>
        <h1>Trade Reports</h1>
        <BootstrapTable  
          data={this.state.trades}
          scrollTop={"Bottom"}
          replace={true}         
          tableStyle={ { border: '#ffffff 2.5px solid', font: '#ffffff'} }
          bodyStyle={ { border: 'green 1px solid', font: '#ffffff'} }> 
        
          <TableHeaderColumn isKey={true} dataField="trader1Id" width='5'>Trader 1 ID</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="trader2Id">Trader 2 Id</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="order1Id">Order 1 Id</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="order2Id">Order 2 Id </TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="fillSize">Size</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="price">Price</TableHeaderColumn>
          <TableHeaderColumn width='1' dataField="ticker">Asset </TableHeaderColumn>


        </BootstrapTable>
        </div>
        </div>;
    return mainComponent;
  }
}
export default OrderView;
