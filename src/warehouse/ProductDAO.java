package warehouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Connection conn;

    public ProductDAO() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/warehouse",
                "root",
                ""   // EMPTY PASSWORD
        );
    }

    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();

        PreparedStatement st = conn.prepareStatement("SELECT * FROM products");
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getDouble("price")
            ));
        }

        return list;
    }

    public void updateStock(int id, int qty) throws Exception {
        PreparedStatement st = conn.prepareStatement(
                "UPDATE products SET stock = stock - ? WHERE id = ?"
        );
        st.setInt(1, qty);
        st.setInt(2, id);
        st.executeUpdate();
    }
}
