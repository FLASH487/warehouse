package warehouse;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private BlockingQueue<OrderTask> queue;

    public ClientHandler(Socket socket,
                         ProductDAO productDAO,
                         OrderDAO orderDAO,
                         BlockingQueue<OrderTask> queue) {

        this.socket = socket;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.queue = queue;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(
                     socket.getOutputStream(), true)) {

            out.println("WELCOME");
            out.println("Commands: LIST | ORDER <pid> <qty> | STATUS <id> | QUIT");

            String line;

            while ((line = in.readLine()) != null) {

                String[] p = line.split(" ");
                if (p.length == 0) continue;

                System.out.println("DEBUG: Received command: " + line);

                // ---------- LIST ----------
                if (p[0].equalsIgnoreCase("LIST")) {

                    var list = productDAO.findAll();
                    out.println("PRODUCTS:");

                    for (Product pr : list) {
                        out.println(pr.getId() + " " + pr.getName() +
                                " stock=" + pr.getStock() +
                                " price=" + pr.getPrice());
                    }
                    out.println();
                }

                // ---------- ORDER ----------
                else if (p[0].equalsIgnoreCase("ORDER") && p.length == 3) {
                    try {
                        int pid = Integer.parseInt(p[1]);
                        int qty = Integer.parseInt(p[2]);

                        int orderId = orderDAO.createOrder(pid, qty);

                        queue.put(new OrderTask(orderId, pid, qty));

                        out.println("ORDER_ACCEPTED " + orderId);
                        out.println();

                        System.out.println("DEBUG: ORDER_ACCEPTED " + orderId);

                    } catch (Exception e) {
                        out.println("ERROR processing order");
                        out.println();
                    }
                }

                // ---------- STATUS ----------
                else if (p[0].equalsIgnoreCase("STATUS") && p.length == 2) {

                    int id = Integer.parseInt(p[1]);
                    String status = orderDAO.getStatus(id);

                    out.println("STATUS " + id + " " + status);
                    out.println();
                }

                // ---------- QUIT ----------
                else if (p[0].equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Client disconnected.");
    }
}
