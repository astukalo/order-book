package xyz.a5s7.model;

/**
 * Interface for listening to order book events.
 */
public interface OrderBookListener {
    void onTrade(Trade trade);
}
