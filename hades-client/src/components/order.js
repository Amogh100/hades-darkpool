class Order {
    constructor(price, size, traderId, ticker, bid, type) {
      this.price = price;
      this.size = size;
      this.traderId = traderId;
      this.ticker = ticker;
      this.bid = bid;
      this.type = type;
    }
}

export default Order;
