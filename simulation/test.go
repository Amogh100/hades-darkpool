package main

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
)

type Trader struct {
	traderId string
	token    string
}

type Order struct {
	Price     float64 `json:"price"`
	Size      float64 `json:"size"`
	Ticker    string  `json:"ticker"`
	Bid       bool    `json:"bid"`
	OrderType string  `json:"type"`
}

var sharedClient *http.Client
var r *rand.Rand

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

func randomSize(min, max float64) float64 {
	return r.Float64()*(max-min) + min
}

func randSide() bool {
	return int(math.Round(r.Float64())) != 0
}

func (trader *Trader) placeOrder(order *Order) {
	serialized, err := json.Marshal(order)
	if err != nil {
		fmt.Println("Error serializing order ")
	}
	req, err := http.NewRequest("POST", "http://localhost:8080/order", bytes.NewBuffer(serialized))
	fmt.Println(string(serialized))
	req.Header.Set("Content-Type", "application/json")

	req.Header.Set("Authorization", trader.token)
	resp, err := sharedClient.Do(req)
	if err != nil {
		fmt.Println("Error executing request")
	} else {
		body, _ := ioutil.ReadAll(resp.Body)
		fmt.Println("Got msg: ", string(body))
	}

}

func getRandomTicker(cryptos []string) string {
	index := rand.Intn(len(cryptos))
	return cryptos[index]
}

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
	t1 := Trader{traderId: "t1", token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmFkZXIxIiwiY3JlYXRlZCI6MTUyNTA1NTQ3NTY3NywiZXhwIjoxNTI1NzU1NDc1fQ.2NM-nLehpncZd1PWs7xrGB0StuVJHnTjkpqaNNnlBqJO6FlHX-2zo5t6zW8R7uKOzGGvJ-GzuIP1jN68iDKDYw"}
	t2 := Trader{traderId: "t2", token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmFkZXIyIiwiY3JlYXRlZCI6MTUyNTA1NTUwOTkxNCwiZXhwIjoxNTI1NzU1NTA5fQ.uzR2ws0qFtfJkRPQwiIQpF6eFqUojzD0raLpXVj57M-37q5ATs7KUEVsbSWOIQbJVpAM-YIxfC_EKwIa9dkdig"}
	t3 := Trader{traderId: "t3", token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmFkZXIzIiwiY3JlYXRlZCI6MTUyNTA1NTUzODAyOSwiZXhwIjoxNTI1NzU1NTM4fQ.Z8wFLcoXW1fOKVhodtHkHWF0PWYnKBA6zybgxBio9vfWEeTDy-n4jPNnlMbfT6NkZsR4rTzpjJGFQ3kCaYIJlA"}
	cryptos := []string{"BTC", "ETH", "XRP", "LTC"}
	sharedClient = &http.Client{}
	traders := []Trader{t1, t2, t3}
	var wg sync.WaitGroup
	for _, t := range traders {
		wg.Add(1)
		go func(trader Trader){
			trader.continuouslyExecuteRandomStrategy(cryptos)
		}(t)
	}
	wg.Wait()
}
