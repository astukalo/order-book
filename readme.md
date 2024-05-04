# Order Book
The project is a simple order book that matches buy and sell orders. 
The orders are matched in price time priority (first by price, then by arrival time into the book).
The execution price is at the best available price.

## Input
Order inputs is provided as comma separated values, one order per line of the input, delimited by a new line character.
The fields are: order-id, side, price, quantity. Side will have a value of 'B' for **Buy** or 'S' for **Sell**.
Price and quantity are integers. Order-id is a string.

## Output
The program prints the trades as they occur. 
Trade output indicates the aggressing order-id, the resting order-id, the price of the match and the quantity traded, followed by a newline.
Once standard input ends, the program prints the final contents of the order book.

## Example
### Input
```sh
10000,B,98,25500
10005,S,105,20000
10001,S,100,500
10002,S,100,10000
10003,B,99,50000
10004,S,103,100
10006,B,105,16000
```

### Output
```sh
trade 10006,10001,100,500
trade 10006,10002,100,10000
trade 10006,10004,103,100
trade 10006,10005,105,5400
     50,000     99 |    105      14,600
     25,500     98 |                   
```

## How to run
Build jar file:
```sh
./gradlew jar
```

To run the application, execute the following command from the root folder:
```sh
java -jar ./build/libs/orderbook-1.0-SNAPSHOT.jar < ./src/test/resources/test_cases/input/2.txt 
```
