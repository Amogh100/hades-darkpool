
# Section 1
HW4 Goal (as per proposal and Homework3.md description) "Implement Matching Algorithm. User Validation.". "There are no changes specified I would like to make to this plan. I do want to specify more on the matching algorithm as per the instructors request. This is a price-time priority algorithm, which manages a list of orders at a given price level (this list is structured so that orders are sorted by time of arrival). When an order on the opposite side arrives (opposite side for a BUY is a SELL, vice versa) and matches the limit price the order book will begin to fill orders so long as the crossing order has volume and is within its limit price if it's a limit order. For market orders, the book will have to immediately match to the best bid or ask depending on the side of the order. Moreover, there should be a separate order book per asset (so an orderbook for BTC, one for ETH, etc.). For more info and a specific example of the matching process refer to the Price-time priority/FIFO answer to this stack overflow question asking about various order matching algorithms. https://stackoverflow.com/questions/13112062/which-are-the-order-matching-algorithms-most-commonly-used-by-electronic-financi"
# Section 2
I implemented the matching algorithm described above as well as user validation, using Spring Security and simple logic that rejects Orders that exceed the user's current capital. In the client, there is a login/signup form. In my proposal, I mentioned storing JWT auth tokens in Redis (an in memory database) but after studying the Spring Security framework I realized that there is no need to actually store them since the algorithm used for verifying these tokens is built into the framework; there doesn't need to be a separate process to store the token and compare them to a database. I extended the architecture, by adding a separate project called the hades engine. This is listening over RPC/RabbitMQ for order messages and then putting them into the order book. The architecture was extended to achieve separation of concerns; having separate code that handles rest api requests and code that actually processes this message makes the application easier to debug, as well as decouples core business logic from REST handlers. In the new architecture, order messsages are accepted on an endpoint, serialized to a string, and then this is passed to an RPC(remote procedure call) client which sends this message over a queue. The Hades Engine listens for this message, and then validates it and puts the order into the appropriate order book.

# Section 3
HW5 Goal as per proposal "TradeReport, send JSON back to GUI (via REST). Display Traderport in list view". This is on track, and I have no intentions of changing this.

# Section 4
![Screenshot_from_2018-03-21_21-26-09.png](https://s14.postimg.org/rcw4x09y9/Screenshot_from_2018-03-21_21-26-09.png)
This is the sign in/up page
![Screenshot_from_2018-03-21_21-25-14.png](https://s14.postimg.org/fb0r2xdld/Screenshot_from_2018-03-21_21-25-14.png)
This is the temporary Order Book snapshot I have for debugging the order book code. 3 buy XRP orders with a quantity of 3000 at 10.04 each are sent through the server (which then routes the orders to the engine) and then a sell 5000 XRP order at 
10.04 is executed. This sell order crosses and then the first buy order is completely filled while the second is partially filled 2000. The third buy order still sits in the book.
![Dashboard.png](https://s14.postimg.org/6u18rpbdt/Dashboard.png)

This is the updated dashboard, although now open orders are not all open orders but open orders for the currently sign in user. There is a sign out functionality, as well as a capital display on the top (it's tiny, I have yet to major styling so the GUI is ugly.)

![jwt.png](https://s14.postimg.org/ivwmlwxht/jwt.png)

User auth is done through JWT (JSON web tokens). The SPring application handles the creation/management of these tokens, but they're cached in the browser and removed upon sign out.


