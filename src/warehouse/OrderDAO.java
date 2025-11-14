package warehouse;

import java.sql.*;

public class OrderDAO {

    private Connection conn;

    public OrderDAO() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/warehouse",
                "root",
                ""
        );
    }

    public int createOrder(int productId, int qty) throws Exception {
        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO orders(product_id, qty, status, timestamp) VALUES (?, ?, 'PENDING', ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        st.setInt(1, productId);
        st.setInt(2, qty);
        st.setLong(3, System.currentTimeMillis());
        st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    public void updateStatus(int id, String status) throws Exception {
        PreparedStatement st = conn.prepareStatement(
                "UPDATE orders SET status = ? WHERE id = ?"
        );
        st.setString(1, status);
        st.setInt(2, id);
        st.executeUpdate();
    }

    public String getStatus(int id) throws Exception {
        PreparedStatement st = conn.prepareStatement(
                "SELECT status FROM orders WHERE id = ?"
        );
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();
        if (rs.next()) return rs.getString("status");
        return "NOT_FOUND";
    }
}
