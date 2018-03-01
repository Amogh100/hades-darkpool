1.) Goal (as per the original proposal) "Server that accepts orders over REST endpoint “/order”and puts them in an order book. 
Simple Order Entry Form. (No matching algorithm yet)"</br></br></br>
2.) The one minor change I have made was in my original class specification I had OrderGateway as a Singleton class; after studying more about the Spring Boot framework, I have come to understand that the @Controller annotation for a class(which OrderGateway uses) by default constructs a singleton underneath within the framework. See here https://stackoverflow.com/questions/11508405/are-spring-mvc-controllers-singletons. I plan on making an OrderQueue class for HW4 to pipe orders to the relevant matching engine; this will be a singleton class that I will create as opposed to the framework creating it for me. Other than this, I have basically accomplished the goal for this homework, as the code currently has a simple order entry form that allows a (single)
user to enter orders and puts it in a single order book. This was a relatively straightforward task. I even added
an additional feature that displays a table view of all open orders. This was done so that I can see all the orders
in the database without manually logging into the postgres instance and doing a "SELECT * from orders". This is going to have to get done
in the future anyways, when querying open orders for a given user so I thought just to go ahead and do it early. The more complex work will happen
in the next part, where there will be multiple order books for the various assets (it makes no sense/is impossible to only have 1 for matching all),
managing multiple users and then the actual matching algorithm. More on this on the next part of this document.</br></br></br>
3.) HW4 Goal (as per the original proposal) "Implement Matching Algorithm. User Validation.". There are no changes specified I would
like to make to this plan. I do want to specify more on the matching algorithm. This is a price-time priority algorithm, which essentially
manages a list of orders at a given price level (this list is structured so that orders are sorted by time of arrival) and when an order on the opposite side (so the opposite side for a BUY is a SELL and vice versa) matches the 
price or better comes in and then begins filling orders so long as the crossing order has volume and is within its limit price if it's a 
limit order. Moreover, there should be a separate order book per asset (so an orderbook for BTC, one for ETH, etc.). For more info and a specific example of the matching process refer to the Price-time priority/FIFO answer to this stack overflow question asking about various order matching algorithms. https://stackoverflow.com/questions/13112062/which-are-the-order-matching-algorithms-most-commonly-used-by-electronic-financi</br></br></br>

4.) Spring Boot Server ![image](https://s17.postimg.org/m0xmc78n3/Spring_Boot.png)</br></br>
Front end react client ![image](https://s17.postimg.org/4ox9qghn3/hades-cli.png) disregard missing order id 2. I placed order 2 under a different trader id.</br></br>
Postgres view of the data ![image](https://s17.postimg.org/7vrta924v/psql.png)</br></br>