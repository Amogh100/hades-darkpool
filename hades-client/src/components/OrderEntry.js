import { DropdownButton } from "react-bootstrap";
import { MenuItem } from "react-bootstrap";
import { Button } from "react-bootstrap";
import React, { Component } from "react";
import styles from "./OrderEntry";
import axios from "axios";
import Order from "./Order";
import AuthorizationService from "../services/AuthorizationService";

/** 
 * Component for entering in orders.
*/
class OrderEntry extends Component {
  constructor(props) {
    super(props);
    /**
     * Not ideal, hard coded values will eventually have to get this
     * data from the service.
     */
    this.validOrderTypes = ["MARKET", "LIMIT"];
    this.validAssets = ["BTC", "ETH", "XRP", "LTC"];
    this.state = {
      orderType: "Order Type",
      orderPrice: 0.0,
      size: 0,
      side: "Side",
      asset: "Currency"
    };
    this.onTypeSelect = this.onTypeSelect.bind(this);
    this.onSideSelect = this.onSideSelect.bind(this);
    this.onAssetSelect = this.onAssetSelect.bind(this);
    this.enterOrder = this.enterOrder.bind(this);
    this.auth = new AuthorizationService();
  }

  /**
   * 
   * @param {*} eventKey needed for react-bootstrap event identification (not used here)
   */
  enterOrder(eventKey) {
    //Set bid boolean based on what the user has selected
    var bid = false;
    if (this.state.side === "Buy") {
      bid = true;
    }
    var order = new Order(
      this.state.orderPrice,
      this.state.size,
      this.state.asset,
      bid,
      this.state.orderType
    );
    //Send order to post endpoint.
    axios
      .post("http://localhost:8080/order", order)
      .then(response => alert(response.data.message));
  }

  /**
   * 
   * @param {*} eventKey unique identifier for event
   * Updates state based on the orderType chosen 
   */
  onTypeSelect(eventKey) {
    this.setState({ orderType: eventKey, orderPrice: 0.0 });
  }

  /**
   * 
   * @param {*} eventKey unique identifier for event
   * Updates state based on the side chosen
   */
  onSideSelect(eventKey) {
    this.setState({ side: eventKey });
  }

  /**
   * 
   * @param {*} eventKey unique identifier for event
   * Updates state based on the asset chosen.
   */
  onAssetSelect(eventKey) {
    this.setState({ asset: eventKey });
  }

  /** 
   * Price is only relevant for limit orders (market orders will get
   * entered in the order book when the best bid/ask is discovered but this 
   * is hidden to the user)
  */
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

  /** 
   * If the order type is valid display the input box for size.
  */
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
  /** 
   * Generate menu options for order type
  */
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

  /** 
   * Generate menu options for the various assets
  */
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

  /** 
   * Main method for a React component that actually generates
   * the html components.
  */
  render() {
    let price = this.getOrderPriceComponent();
    let size = this.getSizeComponent();
    let types = this.getOrderTypeComponents();
    //Basically create OrderType, Side, Currency, and Submission buttons.
    return (
      <div class="container">
        <div>
          <DropdownButton
            bsStyle="danger"
            bsSize="large"
            title={this.state.orderType}
          >
            {types}
          </DropdownButton>
          <DropdownButton
            bsStyle="danger"
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
            bsStyle="danger"
            bsSize="large"
            title={this.state.asset}
          >
            {this.getAssetComponents()}
          </DropdownButton>
          <Button bsStyle="danger" bsSize="large" onClick={this.enterOrder}>
            Enter Order
          </Button>

            <Button bsStyle="danger" bsSize="large" onClick={this.auth.signOut}>
            Sign Out
          </Button>
        </div>
        {size}
        {price}
      </div>
    );
  }
}

export default OrderEntry;
