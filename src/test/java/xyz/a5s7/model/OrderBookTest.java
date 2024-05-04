package xyz.a5s7.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderBookTest {
    private OrderBook orderBook;
    private OrderBookListener listener;
    @Captor
    private ArgumentCaptor<Trade> tradeCaptor;

    @BeforeEach
    public void setup() {
        listener = Mockito.mock(OrderBookListener.class);
        orderBook = new OrderBook("ticker", listener);
        tradeCaptor = ArgumentCaptor.forClass(Trade.class);
    }

    @Test
    public void shouldMatchOrdersWhenPricesAreEqual() {
        Order buyOrder = new Order("1", OrderType.BID, 100, 10);
        Order sellOrder = new Order("2", OrderType.ASK, 100, 10);
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        assertThat(orderBook.getAsksMap()).isEmpty();
        assertThat(orderBook.getBidsMap()).isEmpty();
        verify(listener, times(1)).onTrade(tradeCaptor.capture());
        assertThat(tradeCaptor.getValue()).isEqualTo(new Trade("2", "1", 100, 10));
    }

    @Test
    public void shouldNotMatchOrdersWhenPricesAreNotEqual() {
        Order buyOrder = new Order("1", OrderType.BID, 100, 10);
        Order sellOrder = new Order("2", OrderType.ASK, 102, 5);
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        assertThat(orderBook.getBidsMap()).hasSize(1);
        assertThat(orderBook.getAsksMap()).hasSize(1);
        verify(listener, never()).onTrade(any(Trade.class));
    }

    @Test
    public void shouldPartiallyMatchOrdersWhenSellQuantityIsLessThanBuyQuantity() {
        Order buyOrder = new Order("1", OrderType.BID, 100, 10);
        Order sellOrder = new Order("2", OrderType.ASK, 100, 5);
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        assertThat(orderBook.getBidsMap()).hasSize(1);
        assertThat(orderBook.getBidsMap().get(100)).hasSize(1);
        assertThat(orderBook.getBidsMap().get(100).peek().getQuantity()).isEqualTo(5);
        assertThat(orderBook.getAsksMap()).isEmpty();
        verify(listener).onTrade(tradeCaptor.capture());
        assertThat(tradeCaptor.getValue()).isEqualTo(new Trade("2", "1", 100, 5));
    }

    @Test
    public void shouldPartiallyMatchOrdersWhenAskPriceIsLowerBestBidPrice() {
        Order buyOrder = new Order("1", OrderType.BID, 100, 5);
        Order sellOrder = new Order("2", OrderType.ASK, 98, 10);
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        assertThat(orderBook.getBidsMap()).isEmpty();
        assertThat(orderBook.getAsksMap()).hasSize(1);
        assertThat(orderBook.getAsksMap().get(98)).hasSize(1);
        assertThat(orderBook.getAsksMap().get(98).peek().getQuantity()).isEqualTo(5);
        verify(listener).onTrade(tradeCaptor.capture());
        assertThat(tradeCaptor.getValue()).isEqualTo(new Trade("2", "1", 100, 5));
    }

    @Test
    public void shouldPartiallyMatchOrdersWhenBidPriceIsHigherBestAskPrice() {
        Order sellOrder = new Order("1", OrderType.ASK, 98, 5);
        Order buyOrder = new Order("2", OrderType.BID, 100, 10);
        orderBook.addOrder(sellOrder);
        orderBook.addOrder(buyOrder);

        assertThat(orderBook.getBidsMap()).hasSize(1);
        assertThat(orderBook.getBidsMap().get(100)).hasSize(1);
        assertThat(orderBook.getBidsMap().get(100).peek().getQuantity()).isEqualTo(5);
        verify(listener).onTrade(tradeCaptor.capture());
        assertThat(tradeCaptor.getValue()).isEqualTo(new Trade("2", "1", 98, 5));
    }

    @Test
    void shouldNotAllowEmptyOrders() {
        assertThrows(NullPointerException.class, () -> orderBook.addOrder(null));
    }

    @Test
    void shouldNotAllowOrderWithoutId() {
        assertThrows(IllegalArgumentException.class, () -> orderBook.addOrder(new Order(null, null, 0, 10)));
    }

    @Test
    void shouldNotAllowOrderWithoutType() {
        assertThrows(IllegalArgumentException.class, () -> orderBook.addOrder(new Order("1", null, 0, 10)));
    }
}