import java.sql.*;
import java.util.Scanner;
public class ProductCRUD 
{
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "username";
    private static final String PASSWORD = "password";
    public static void main(String[] args) 
    {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) 
        {
            boolean exit = false;
            while (!exit) 
            {
                System.out.println("\nProduct Management System");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        createProduct(conn, scanner);
                        break;
                    case 2:
                        readProducts(conn);
                        break;
                    case 3:
                        updateProduct(conn, scanner);
                        break;
                    case 4:
                        deleteProduct(conn, scanner);
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    private static void createProduct(Connection conn, Scanner scanner) 
    {
        System.out.print("Enter Product Name: ");
        String name = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        String sql = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            conn.setAutoCommit(false);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Product added successfully!");
        }
        catch (SQLException e) 
        {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }
    private static void readProducts(Connection conn) 
    {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) 
        {
            System.out.println("ProductID | ProductName | Price | Quantity");
            System.out.println("------------------------------------------");
            while (rs.next()) 
            {
                System.out.println(rs.getInt("ProductID") + " | " + rs.getString("ProductName") + " | " + rs.getDouble("Price") + " | " + rs.getInt("Quantity"));
            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    private static void updateProduct(Connection conn, Scanner scanner) 
    {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();
        String sql = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            conn.setAutoCommit(false);
            pstmt.setDouble(1, price);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Product updated successfully!");
        } 
        catch (SQLException e) 
        {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }
    private static void deleteProduct(Connection conn, Scanner scanner) 
    {
        System.out.print("Enter Product ID to delete: ");
        int id = scanner.nextInt();
        String sql = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            conn.setAutoCommit(false);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Product deleted successfully!");
        } 
        catch (SQLException e) 
        {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }
}
