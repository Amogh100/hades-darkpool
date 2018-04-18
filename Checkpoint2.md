
# What I said I Would Do
I said I would implement various in-memory structures and data access objects in order to facilitate proper checks for Trade  management. In addition, I said I would verify/ensure that the Trades are properly created/reported. Essentialy, this included refactoring/testing and in general making Trade reporting more robust.

# What I Did 
I accomplished my goals in addition to adding the Trade Report to the GUI (whose information is obtained via a Rest Endpoint and JSON). To ensure if the Trade feature was robust I wrote various unit tests and if they failed I would go back and fix the code. I also implemented various caching structures, such as the OpenOrderCache, and PositionCache. This allows for easier abstraction as well as performance boosts by obtaining neccessary data in-memory. I added functionality which rejects orders when a trader's open order values are greater than their current capital. This handles the edge case where a trader's open orders can potentially be quickly filled (which changes their capital) and if a new order were to come in, the trader would have negative capital. This is fixed. I also handled self-trade prevention. In addition, I made some GUI changes (stylistic) and added a Trade View table which displays the trade reports in a simple table on the GUI. You can see this in the Screenshots.

# Goals for the next checkpoint
The goals for the next checkpoint were for React to send REST requests to obtain various information and to write Python simulation scripts that simulate many traders inputting orders, as well as static/dynamic analysis. I still plan on writing the Python simulation scripts. I also sitll plan on analyzing performance for both the hades engine and the rest api. The react client has already been polling the REST endpoints and displaying information, so in my original proposal this is kind of a redundant point. That being said, I could always style the client better, and introduce some nicer GUI changes.

# Screenshots
[![Trade_Report_Gui.png](https://s14.postimg.cc/ac6j7wyjl/Trade_Report_Gui.png)](https://postimg.cc/image/7uus0nen1/)
[![Screenshot_from_2018-04-17_23-06-47.png](https://s14.postimg.cc/ac6j7ygk1/Screenshot_from_2018-04-17_23-06-47.png)](https://postimg.cc/image/6ft7byvkd/)
