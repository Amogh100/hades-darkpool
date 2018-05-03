package main

/**
  * This is a simulation script that spawns three traders who put in random (but fairly reasonable) orders
  * It sends the orders through the dark pool rest api, which in turn is processed by the engine.
  */
import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math"
	"math/rand"
	"net/http"
	"sync"
	"time"
	"os"
)


//Struct that wraps a trader, with a local id (not the one in the database) and the JWT token  
type Trader struct {
	traderId string
	token    string
}

//Struct that represents an order
type Order struct {
	Price     float64 `json:"price"`
	Size      float64 `json:"size"`
	Ticker    string  `json:"ticker"`
	Bid       bool    `json:"bid"`
	OrderType string  `json:"type"`
}

var sharedClient *http.Client
var r *rand.Rand

/**
  * Generates a random price, which are loosely based on current prices
  */
func randomPrice(ticker string) float64 {
	switch ticker {
	case "BTC":
		return 1000*r.Float64() + 9000
	case "ETH":
		return 200*r.Float64() + 600
	case "LTC":
		return 150*r.Float64() + 150
	case "XRP":
		return 0.95*r.Float64() + 0.05
	}
	return 0.0
}

/**
  * Generate a random order size between a given range
  * @param min minimum size
  * @param max maximum size
  */
func randomSize(min, max float64) float64 {
	return r.Float64()*(max-min) + min
}

/**
  * Generate a random side for an order
  * returns true if the order is a buy
  */
func randSide() bool {
	return int(math.Round(r.Float64())) != 0
}

/**
  * Function that places order for a trader
  * @param order Pointer to order to place.
  */
func (trader *Trader) placeOrder(order *Order) {
	//Serialize order to JSON
	serialized, err := json.Marshal(order)
	if err != nil {
		fmt.Println("Error serializing order ")
	}
	//Make request.
	req, err := http.NewRequest("POST", "http://35.165.62.166:8080/order", bytes.NewBuffer(serialized))
	fmt.Println(string(serialized))
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", trader.token)
	resp, err := sharedClient.Do(req)
	//Log response to stdout.
	if err != nil {
		fmt.Println("Error executing request")
	} else {
		body, _ := ioutil.ReadAll(resp.Body)
		fmt.Println("Got msg: ", string(body))
	}

}

/**
  *This function chooses random ticker from a list of cryptocurrencies
  *@param cryptos list of string tickers
  */
func getRandomTicker(cryptos []string) string {
	index := rand.Intn(len(cryptos))
	return cryptos[index]
}

/**
  *This function repeatedly inputs random orders (random price, random side) and then sleeps.
  *@param cryptos list of string tickers that are available for the strategy
  */
func (trader *Trader) continuouslyExecuteRandomStrategy(cryptos []string) {
		for {
			fmt.Println(trader.traderId)
			ticker := getRandomTicker(cryptos)
			price := randomPrice(ticker)
			size := randomSize(0.0001, 10)
			side := randSide()
			trader.placeOrder(&Order{Price: price, Size: size, Ticker: ticker, OrderType: "LIMIT", Bid: side})
			time.Sleep(5 * time.Second)
		}	
	}

func main() {
	rand.Seed(time.Now().Unix())
	s := rand.NewSource(time.Now().Unix())
	r = rand.New(s)
	//Create traders. Tokens are obtained from environment variables.
	t1 := Trader{traderId: "t1", token: os.Getenv("t1")}
	t2 := Trader{traderId: "t2", token: os.Getenv("t2")}
	t3 := Trader{traderId: "t3", token: os.Getenv("t3")}
	cryptos := []string{"BTC", "ETH", "XRP", "LTC"}
	sharedClient = &http.Client{}
	traders := []Trader{t1, t2, t3}
	var wg sync.WaitGroup
	//Start each trader on their own goroutine and let them continuously put in random orders.
	for _, t := range traders {
		wg.Add(1)
		go func(trader Trader){
			trader.continuouslyExecuteRandomStrategy(cryptos)
		}(t)
	}
	wg.Wait()
}
