package xyz.a5s7.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Queue;
import java.util.TreeMap;

public class OrderBook {
    private final String ticker;
    private final OrderBookListener listener;
    private final NavigableMap<Integer, Queue<Order>> bids = new TreeMap<>();
    private final NavigableMap<Integer, Queue<Order>> asks = new TreeMap<>();

    public OrderBook(String ticker, OrderBookListener listener) {
        this.ticker = ticker;
        this.listener = listener;
    }

    public void addOrder(Order order) {
        Objects.requireNonNull(order);
        if (order.getQuantity() == 0) {
            return;
        }
        if (order.getId() == null) {
            throw new IllegalArgumentException("Order id is required");
        }

        NavigableMap<Integer, Queue<Order>> oppositeSideOrders;
        NavigableMap<Integer, Queue<Order>> restingOrdersMap;
        NavigableMap<Integer, Queue<Order>> orders;
        if (order.getType() == OrderType.ASK) {
            orders = asks;
            oppositeSideOrders = bids;
            restingOrdersMap = oppositeSideOrders.tailMap(order.getPrice(), true).descendingMap();
        } else if (order.getType() == OrderType.BID) {
            orders = bids;
            oppositeSideOrders = asks;
            restingOrdersMap = oppositeSideOrders.headMap(order.getPrice(), true);
        } else {
            throw new IllegalArgumentException("Unknown order type: " + order.getType());
        }
        var aggressingOrderId = order.getId();
        //Orders are first matched in order of price (most aggressive to least aggressive)
        for (Iterator<Queue<Order>> iterator = restingOrdersMap.values().iterator(); iterator.hasNext(); ) {
            var restingOrders = iterator.next();
            // then by arrival time into the book (oldest to newest)
            while (!restingOrders.isEmpty() && order.getQuantity() > 0) {
                Order restingOrder = restingOrders.peek();
                var tradeQuantity = Math.min(order.getQuantity(), restingOrder.getQuantity());
                restingOrder.reduceQuantity(tradeQuantity);
                order.reduceQuantity(tradeQuantity);
                listener.onTrade(new Trade(aggressingOrderId, restingOrder.getId(), restingOrder.getPrice(), tradeQuantity));
                if (restingOrder.getQuantity() == 0) {
                    restingOrders.poll();
                }
            }
            if (restingOrders.isEmpty()) {
                iterator.remove();
            }
        }
        if (order.getQuantity() > 0) {
            orders.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
        }
    }

    public NavigableMap<Integer, Queue<Order>> getAsksMap() {
        //TODO queues are mutable, need to return immutable
        //order is not immutable so it can be changed outside of the order book
        //deep copy of a map or special immutable order class maybe returned here
        return Collections.unmodifiableNavigableMap(asks);
    }

    public NavigableMap<Integer, Queue<Order>> getBidsMap() {
        return Collections.unmodifiableNavigableMap(bids);
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "ticker='" + ticker + '\'' +
                ", bids=" + bids +
                ", asks=" + asks +
                '}';
    }
}
