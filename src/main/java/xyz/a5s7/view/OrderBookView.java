package xyz.a5s7.view;

import xyz.a5s7.model.Order;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.SortedMap;
import java.util.function.BiFunction;

public class OrderBookView {
    static final String BLANK = String.format("%18s", ""); //  000,000,000 000000
    private final DecimalFormat formatter;
    private final SortedMap<Integer, Queue<Order>> buyOrders;
    private final SortedMap<Integer, Queue<Order>> sellOrders;

    //TODO better to provide OrderBook, instead of two maps
    public OrderBookView(SortedMap<Integer, Queue<Order>> buyOrders, SortedMap<Integer, Queue<Order>> sellOrders) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        formatter = new DecimalFormat("#,###", otherSymbols);
        this.buyOrders = buyOrders;
        this.sellOrders = sellOrders;
    }

    /**
     * Generates a view of the order book. The view is formatted to align the following format:
     * Bids (Buying)         Asks (Selling)
     * qty         price  | price  qty
     * 000,000,000 000000 | 000000 000,000,000
     * Example:
     *      50,000     99 |    100         500
     *      25,500     98 |    100      10,000
     *                    |    103         100
     *                    |    105      20,000
     * @return the formatted order book view
     */
    public String generateView() {
        StringBuilder view = new StringBuilder();

        List<String> bids = reversed(formattedSide(buyOrders, this::qtyPrice));
        List<String> asks = formattedSide(sellOrders, this::priceQty);

        int max = Math.max(bids.size(), asks.size());
        for (int i = 0; i < max; i++) {
            String bid = i < bids.size() ? bids.get(i) : BLANK;
            String ask = i < asks.size() ? asks.get(i) : BLANK;
            view.append(String.format("%s | %s\n", bid, ask));
        }

        return view.toString();
    }

    private List<String> reversed(final List<String> list) {
        var out = new ArrayList<>(list);
        Collections.reverse(out);
        return out;
    }

    private List<String> formattedSide(final SortedMap<Integer, Queue<Order>> orders,
                                                final BiFunction<Integer, Integer, String> formatQtyPrice) {
        return orders.values()
                .stream()
                .flatMap(Collection::stream)
                .map(order -> formatQtyPrice.apply(order.getPrice(), order.getQuantity()))
                .toList();
    }

    private String qtyPrice(final Integer price, final int totalQty) {
        return String.format("%11s %6d", formatter.format(totalQty), price);
    }

    private String priceQty(final Integer price, final int totalQty) {
        return String.format("%6d %11s", price, formatter.format(totalQty));
    }
}
