package warehouse;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class WarehouseServer {

    public static void main(String[] args) throws Exception {

        
        BlockingQueue<OrderTask> orderQueue = new LinkedBlockingQueue<>();

        
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        
        OrderWorker worker = new OrderWorker(orderQueue, productDAO, orderDAO);
        new Thread(worker).start();

        
        ExecutorService clientPool = Executors.newCachedThreadPool();

        System.out.println("Server running on port 5050");

        try (ServerSocket server = new ServerSocket(5050)) {

            while (true) {
                Socket client = server.accept();

                clientPool.submit(new ClientHandler(
                        client,
                        productDAO,
                        orderDAO,
                        orderQueue
                ));
            }
        }
    }
}
