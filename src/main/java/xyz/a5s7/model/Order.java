package xyz.a5s7.model;

public class Order {
    private final String id;
    private final OrderType type;
    private final int price;
    private int quantity;

    public Order(String id, OrderType type, int price, int quantity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public OrderType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    public void reduceQuantity(int tradeQuantity) {
        quantity -= tradeQuantity;
    }
}
