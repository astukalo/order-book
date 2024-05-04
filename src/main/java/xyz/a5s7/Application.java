package xyz.a5s7;

import xyz.a5s7.model.Order;
import xyz.a5s7.model.OrderBook;
import xyz.a5s7.view.OutputOrderBookListener;
import xyz.a5s7.parser.CsvParser;
import xyz.a5s7.view.OrderBookView;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        CsvParser csvParser = new CsvParser(",");
        Scanner scanner = new Scanner(System.in);
        OrderBook orderBook = new OrderBook("TST", new OutputOrderBookListener());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                Order order = csvParser.parse(line);
                orderBook.addOrder(order);
            } catch (Exception e) {
                //do not crash the application on invalid input
            }
        }
        OrderBookView orderBookView = new OrderBookView(orderBook.getBidsMap(), orderBook.getAsksMap());
        System.out.print(orderBookView.generateView());
    }
}