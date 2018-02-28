import { DropdownButton } from "react-bootstrap";
import { MenuItem } from "react-bootstrap";
import { Button } from "react-bootstrap";
import React, { Component } from "react";
import styles from "./order_entry";
import axios from "axios";
import Order from "./order";

class OrderEntry extends Component {
  constructor(props) {
    super(props);
    this.validOrderTypes = ["MARKET", "LIMIT"];
    this.validAssets = ["BTC", "ETH", "XRP", "LTC"];
    this.state = {
      orderType: "Order Type",
      orderPrice: 0.0,
      size: 0,
      side: "Side",
      asset: "Currency",
      traderId: 1
    };
    this.onTypeSelect = this.onTypeSelect.bind(this);
    this.onSideSelect = this.onSideSelect.bind(this);
    this.onAssetSelect = this.onAssetSelect.bind(this);
    this.enterOrder = this.enterOrder.bind(this);
  }

  enterOrder(eventKey) {
    console.log("Entering order!");
    var bid = false;
    if (this.state.side === "Buy") {
      bid = true;
    }
    var order = new Order(
      this.state.orderPrice,
      this.state.size,
      this.state.traderId,
      this.state.asset,
      bid,
      this.state.orderType
    );
    console.log(JSON.stringify(order));
    axios
      .post("http://localhost:8080/order", order)
      .then(response => alert(response.data.message));
  }

  onTypeSelect(eventKey) {
    this.setState({ orderType: eventKey, orderPrice: 0.0 });
  }

  onSideSelect(eventKey) {
    this.setState({ side: eventKey });
  }

  onAssetSelect(eventKey) {
    this.setState({ asset: eventKey });
  }

  getOrderPriceComponent() {
    if (this.state.orderType === "LIMIT") {
      return (
        <div>
          Limit Price<input
            type="number"
            className={styles.input}
            onChange={newPrice =>
              this.setState({ orderPrice: newPrice.target.value })
            }
            value={this.state.orderPrice}
            step="0.001"
          />{" "}
        </div>
      );
    }
  }

  getSizeComponent() {
    if (this.validOrderTypes.some(type => type === this.state.orderType)) {
      return (
        <div>
          Order Size{" "}
          <input
            type="number"
            onChange={newSize => this.setState({ size: newSize.target.value })}
            value={this.state.size}
            step="0.0001"
          />
        </div>
      );
    }
  }

  getOrderTypeComponents() {
    var components = [];
    for (var i = 0; i < this.validOrderTypes.length; i++) {
      components.push(
        <MenuItem
          eventKey={this.validOrderTypes[i]}
          onSelect={this.onTypeSelect}
        >
          {this.validOrderTypes[i]}
        </MenuItem>
      );
    }
    return components;
  }

  getAssetComponents() {
    var components = [];
    for (var i = 0; i < this.validAssets.length; i++) {
      components.push(
        <MenuItem eventKey={this.validAssets[i]} onSelect={this.onAssetSelect}>
          {this.validAssets[i]}
        </MenuItem>
      );
    }
    return components;
  }

  render() {
    let price = this.getOrderPriceComponent();
    let size = this.getSizeComponent();
    let types = this.getOrderTypeComponents();
    return (
      <div class="container">
        <div>
          <DropdownButton
            bsStyle="primary"
            bsSize="large"
            title={this.state.orderType}
          >
            {types}
          </DropdownButton>
          <DropdownButton
            bsStyle="primary"
            bsSize="large"
            title={this.state.side}
          >
            <MenuItem eventKey="Buy" onSelect={this.onSideSelect}>
              Buy
            </MenuItem>
            <MenuItem eventKey="Sell" onSelect={this.onSideSelect}>
              Sell
            </MenuItem>
          </DropdownButton>
          <DropdownButton
            bsStyle="primary"
            bsSize="large"
            title={this.state.asset}
          >
            {this.getAssetComponents()}
          </DropdownButton>
          <Button bsStyle="primary" bsSize="large" onClick={this.enterOrder}>
            Enter Order
          </Button>
        </div>
        {size}
        {price}
      </div>
    );
  }
}

export default OrderEntry;
