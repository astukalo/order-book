package xyz.a5s7.parser;

import xyz.a5s7.model.Order;
import xyz.a5s7.model.OrderType;

import java.util.Objects;

/**
 * Read a CSV line and parse it into an Order object
 */
public class CsvParser {
    private final String separator;

    public CsvParser(String separator) {
        this.separator = separator;
    }

    //TODO is it better to return AddOrderRequest object
    public Order parse(String line) {
        String[] parts = line.split(separator);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid input: " + line);
        }

        String orderId = parts[0];
        OrderType orderType = Objects.equals(parts[1], "S") ? OrderType.ASK : OrderType.BID;
        int price = Integer.parseInt(parts[2]);
        int quantity = Integer.parseInt(parts[3]);
        return new Order(orderId, orderType, price, quantity);
    }
}
