import java.sql.*;
import java.util.Scanner;
class Student 
{
    private int studentID;
    private String name;
    private String department;
    private double marks;
    public Student(int studentID, String name, String department, double marks) 
    {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}
class StudentController 
{
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "username";
    private static final String PASSWORD = "password";
    public void createStudent(Student student) 
    {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            conn.setAutoCommit(false);
            pstmt.setInt(1, student.getStudentID());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setDouble(4, student.getMarks());
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Student added successfully!");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public void readStudents() 
    {
        String sql = "SELECT * FROM Student";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("StudentID | Name | Department | Marks");
            System.out.println("--------------------------------------");
            while (rs.next()) 
            {
                System.out.println(rs.getInt("StudentID") + " | " + rs.getString("Name") + " | " + rs.getString("Department") + " | " + rs.getDouble("Marks"));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public void updateStudent(int studentID, double marks) 
    {
        String sql = "UPDATE Student SET Marks = ? WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            conn.setAutoCommit(false);
            pstmt.setDouble(1, marks);
            pstmt.setInt(2, studentID);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Student updated successfully!");
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public void deleteStudent(int studentID) 
    {
        String sql = "DELETE FROM Student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            conn.setAutoCommit(false);
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Student deleted successfully!");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
public class StudentManagement 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        StudentController controller = new StudentController();
        boolean exit = false;
        while (!exit) 
        {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            switch (choice) 
            {
                case 1:
                    System.out.print("Enter Student ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter Name: ");
                    String name = scanner.next();
                    System.out.print("Enter Department: ");
                    String department = scanner.next();
                    System.out.print("Enter Marks: ");
                    double marks = scanner.nextDouble();
                    controller.createStudent(new Student(id, name, department, marks));
                    break;
                case 2:
                    controller.readStudents();
                    break;
                case 3:
                    System.out.print("Enter Student ID to update: ");
                    int updateID = scanner.nextInt();
                    System.out.print("Enter new Marks: ");
                    double newMarks = scanner.nextDouble();
                    controller.updateStudent(updateID, newMarks);
                    break;
                case 4:
                    System.out.print("Enter Student ID to delete: ");
                    int deleteID = scanner.nextInt();
                    controller.deleteStudent(deleteID);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
        scanner.close();
    }
}
