package warehouse;

public class OrderTask {

    public int orderId;
    public int productId;
    public int qty;

    public OrderTask(int orderId, int productId, int qty) {
        this.orderId = orderId;
        this.productId = productId;
        this.qty = qty;
    }
}
