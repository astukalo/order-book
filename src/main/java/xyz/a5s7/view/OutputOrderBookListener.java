package xyz.a5s7.view;

import xyz.a5s7.model.OrderBookListener;
import xyz.a5s7.model.Trade;

/**
 * OrderBookListener implementation that outputs trades to the console.
 */
public class OutputOrderBookListener implements OrderBookListener {
    @Override
    public void onTrade(Trade trade) {
        //trade 10006,10001,100,500
        System.out.printf("trade %s,%s,%d,%d%n", trade.aggressingId(), trade.restingId(), trade.price(), trade.quantity());
    }
}
