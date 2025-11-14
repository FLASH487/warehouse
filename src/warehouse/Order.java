package warehouse;

public class Order {
    private int id;
    private int productId;
    private int qty;
    private OrderStatus status;
    private long timestamp;

    public Order(int id, int productId, int qty, OrderStatus status, long timestamp) {
        this.id = id;
        this.productId = productId;
        this.qty = qty;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public int getQty() { return qty; }
    public OrderStatus getStatus() { return status; }
    public long getTimestamp() { return timestamp; }
}
