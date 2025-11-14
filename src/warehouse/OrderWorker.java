package warehouse;

import java.util.concurrent.BlockingQueue;

public class OrderWorker implements Runnable {

    private BlockingQueue<OrderTask> queue;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;

    public OrderWorker(BlockingQueue<OrderTask> queue,
                       ProductDAO productDAO,
                       OrderDAO orderDAO) {

        this.queue = queue;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    @Override
    public void run() {

        while (true) {
            try {
                OrderTask task = queue.take();

                productDAO.updateStock(task.productId, task.qty);
                orderDAO.updateStatus(task.orderId, "CONFIRMED");

                System.out.println("DEBUG: processed order " + task.orderId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
