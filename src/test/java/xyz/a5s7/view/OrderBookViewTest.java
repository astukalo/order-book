package xyz.a5s7.view;

import xyz.a5s7.model.Order;
import xyz.a5s7.model.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderBookViewTest {

    private OrderBookView orderBookView;
    private SortedMap<Integer, Queue<Order>> buyOrders;
    private SortedMap<Integer, Queue<Order>> sellOrders;

    @BeforeEach
    public void setup() {
        buyOrders = new TreeMap<>();
        sellOrders = new TreeMap<>();
        orderBookView = new OrderBookView(buyOrders, sellOrders);
    }

    @Test
    public void shouldGenerateEmptyViewWhenNoOrders() {
        String view = orderBookView.generateView();
        assertThat(view).isEmpty();
    }

    @Test
    public void shouldGenerateViewWithSingleBuyOrder() {
        Queue<Order> orders = new LinkedList<>();
        orders.add(new Order("1", OrderType.BID, 999999, 999999999));
        buyOrders.put(999999, orders);

        String view = orderBookView.generateView();
        assertThat(view).isEqualTo("999,999,999 999999 | " + OrderBookView.BLANK + "\n");
    }

    @Test
    public void shouldGenerateViewWithSingleSellOrder() {
        Queue<Order> orders = new LinkedList<>();
        orders.add(new Order("1", OrderType.ASK, 999999, 999999));
        sellOrders.put(999999, orders);

        String view = orderBookView.generateView();
        assertThat(view).isEqualTo(OrderBookView.BLANK + " | 999999     999,999\n");
    }

    //Other scenarios tested with integration tests
}