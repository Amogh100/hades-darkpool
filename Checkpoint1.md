
# What I said I Would Do
After discussing with the instructor about reducing some of the original (tasks mentioned in the original proposal for this checkpoint), we agreed upon the following tasks "server side of TradeReport, send JSON back to GUI (via REST)."

# What I Did
I implemented most of the features of managing Trades. This stores trades in a separate SQL table. There is a rest endpoint which allows traders to obtain their trade reports via JSON. I was not able to implement the Position manager, which updates a trader's position in an asset after a trade occurs. As I will discuss in the next section, I believe I'll have to implement some in memory structures (in addition to persistence in a DB) for position monitoring. Moreover, admittedly the robustness of my solution is not particularly good. I was not able to effectively unit test my code, and I believe there to be several bugs in my implementation. In particular, when orders rapidly enter the market and match, there are some concurrency bugs. I fell behind, as I did not put in any work over break. As I will mention in the goals for the next checkpoint I plan on exhaustivley testing this current implementation and isolating critical bugs. Furthermore, my code is not up to my style. Particularly in the engine, there are several SQL accesses via JDBC. These should be refactored into nice data access objects. Once again, I believe I can effectively rollover these modifications into the goals for the next checkpoint.

# Goals for the next checkpoint
The goal for final checkpoint 2 that I had was to implement the "Remaining REST
Endpoints for GUI". There are several noteworthy modifications to this checkpoint that I plan on making. First, I believe there are not any more significant endpoints that I actually have to implement for a user. I plan on shifting my focus towards robustly testing my Trade Managing engine. I also plan on refactoring a lot of the Trade management code. In particular, I will have to implement some in-memory data structures that are constantly listening for events and updating the database. I will implement a PositionMonitor class, which contains the positions for all the traders. This will be an easier abstraction for me to work with in order to create, update, or remove posiitions for any trader in the system. In a nutshell, I plan on cleaning up the implementation for this checkpoint as I believe the current implementation is not satisfactory.

# Screenshots  
[![database.png](https://s17.postimg.org/wvjizfhhb/database.png)](https://postimg.org/image/6n8ea1xdn/)
This is a snapshot of the Trade database (effectively, the trade reports)

[![trade_report_json.png](https://s17.postimg.org/4nwdw9pu7/trade_report_json.png)](https://postimg.org/image/4nwdw9pu3/)
This is the result of making request to the "trades" endpoint. I am getting all the trades for my current user and then displaying that in JSON which was one of my core goals.

