import {DropdownButton} from 'react-bootstrap'
import {MenuItem} from 'react-bootstrap'
import React, { Component } from 'react';
import styles from './order_entry'

class OrderEntry extends Component {
    

    constructor(props){
        super(props);
        this.validOrderTypes = ["Market", "Limit"];
        this.validAssets = ["BTC", "ETH", "XRP", "LTC"]
        this.state = {orderType: "Order Type", orderPrice: 0.00, size: 0, 
                      side: "Side", asset: "Currency"};
        this.onTypeSelect = this.onTypeSelect.bind(this);
        this.onSideSelect = this.onSideSelect.bind(this);
        this.onAssetSelect = this.onAssetSelect.bind(this);
    }

    onTypeSelect(eventKey){
        this.setState({orderType: eventKey});
    }

    onSideSelect(eventKey){
        this.setState({side: eventKey});
    }

    onAssetSelect(eventKey){
        this.setState({asset: eventKey});
    }


    getOrderPriceComponent(){
        if(this.state.orderType === 'Limit'){
            return (<div>Limit Price<input type="number" className={styles.input}
             onChange={(newPrice) => this.setState({orderPrice: newPrice.target.value})} 
             value={this.state.orderPrice} step="0.001"/> </div>);
        }
    }

    getSizeComponent(){
        if(this.validOrderTypes.some(type => type === this.state.orderType)){
            return (<div>Order Size <input type="number"
            onChange={(newSize) => this.setState({size: newSize.target.value})} 
            value={this.state.size} step="0.0001"/></div>);
        }
    }

    getOrderTypeComponents(){
        var components = []
        for(var i=0; i < this.validOrderTypes.length; i++){
            components.push(<MenuItem eventKey={this.validOrderTypes[i]}
            onSelect={this.onTypeSelect}> 
            {this.validOrderTypes[i]}
            </MenuItem>)
        }
        return components
    }

    getAssetComponents(){
        var components = []
        for(var i=0; i < this.validAssets.length; i++){
            components.push(<MenuItem eventKey={this.validAssets[i]}
            onSelect={this.onAssetSelect}> 
            {this.validAssets[i]}
            </MenuItem>)
        }
        return components
    }

    render(){
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
            <MenuItem eventKey="Buy" onSelect={this.onSideSelect}>Buy</MenuItem>
            <MenuItem eventKey="Sell" onSelect={this.onSideSelect}>Sell</MenuItem>
            </DropdownButton>
            <DropdownButton
                bsStyle="primary"
                bsSize="large"
                title={this.state.asset}
            >
            {this.getAssetComponents()}
            </DropdownButton>
            </div>
            {size}
            {price}
        </div>
        );
    }
}

export default OrderEntry;