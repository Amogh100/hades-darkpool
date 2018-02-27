import { DropdownButton } from "react-bootstrap";
import { MenuItem } from "react-bootstrap";
import React, { Component } from "react";
import styles from "./order_entry";
import axios from "axios";
import { BootstrapTable, TableHeaderColumn } from "react-bootstrap-table";

class OrderView extends Component {
  constructor(props) {
    super(props);
    this.state = { orders: [] };
    this.updateOrders();
    this.updateOrders = this.updateOrders.bind(this);
    setInterval(this.updateOrders, 1000);
  }

  updateOrders() {
    axios
      .get("http://localhost:8080/order?traderId=1")
      .then(response => this.setState({ orders: response.data }));
  }

  render() {
    let options = {
      sortName: "orderId",
      sortOrder: "desc"
    };
    return (
      <div>
        <h1>Open Orders</h1>
        <BootstrapTable
          options={options}
          data={this.state.orders}
          width={5}
          height={5}
          scrollTop={"Bottom"}
        >
          <TableHeaderColumn isKey={true} dataField="orderId">
            Order ID
          </TableHeaderColumn>
          <TableHeaderColumn dataField="ticker">Ticker</TableHeaderColumn>
          <TableHeaderColumn dataField="price">Price</TableHeaderColumn>
          <TableHeaderColumn dataField="bid">Bid </TableHeaderColumn>
        </BootstrapTable>
      </div>
    );
  }
}
export default OrderView;
