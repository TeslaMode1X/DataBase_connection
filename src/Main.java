import java.sql.*;
import java.time.*;
import java.util.Scanner;

public class Main {

    private static final String connectionUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "admin123";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice = 1;
        while (choice > 0) {
            menu();
            System.out.print("Enter value you need (0 to exit): ");
            choice = sc.nextInt();
            if (choice == 1) {
                getAllTasks();
            } else if (choice == 2) {
                createNewTask();
            } else if (choice == 3) {
                updateTask();
            } else if (choice == 4) {
                deleteTask();
            } else if (choice == 0) {
                System.out.println("Exiting...");
            } else {
                System.out.println("Wrong value!");
            }

        }
    }
    public static void menu() {
        System.out.println("\nWelcome to TaskManager database");
        System.out.println("1. Get all the tasks");
        System.out.println("2. Create a new task");
        System.out.println("3. Update some task");
        System.out.println("4. Delete some task\n");
    }
    public static void getAllTasks() {
            try (Connection con = DriverManager.getConnection(connectionUrl, username, password);
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Tasks;")) {

                while (rs.next()) {
                    int taskId = rs.getInt("task_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    String priority = rs.getString("priority");
                    LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    LocalDate date = createdAt.toLocalDateTime().toLocalDate();

                    System.out.printf("Task ID: %d, Title: %s, Description: %s, Status: %s, Priority: %s, Due Date: %s, Created At: %s%n",
                            taskId, title, description, status.equals("f") ? "In progress" : "Completed", priority, dueDate, date);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void createNewTask() {
        try (Connection con = DriverManager.getConnection(connectionUrl, username, password);
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO Tasks (title, description, status, priority, due_date, created_at) VALUES (?, ?, ?, ?, ?, ?)")) {

            Scanner input = new Scanner(System.in);

            System.out.println("Enter your title: ");
            String title = input.nextLine();

            System.out.println("Enter your description: ");
            String description = input.nextLine();

            System.out.println("Enter your status (true or false): ");
            boolean status = input.nextBoolean();

            input.nextLine();

            System.out.println("Enter your priority (high, medium, low): ");
            String priority = input.nextLine();

            System.out.println("Enter your due date (YYYY-MM-DD): ");
            String dueDateStr = input.nextLine();

            Date dueDate = Date.valueOf(dueDateStr);

            Timestamp createdAt = new Timestamp(System.currentTimeMillis());

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setBoolean(3, status);
            pstmt.setString(4, priority);
            pstmt.setDate(5, dueDate);
            pstmt.setTimestamp(6, createdAt);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task created successfully.");
            } else {
                System.out.println("Failed to create task.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask() {
        try (Connection con = DriverManager.getConnection(connectionUrl, username, password);
             PreparedStatement pstmt = con.prepareStatement("DELETE FROM Tasks WHERE task_id = ?")) {

            Scanner input = new Scanner(System.in);
            getAllTasks();

            System.out.println("Enter the task you want to delete: ");
            int taskToDelete = input.nextInt();

            pstmt.setInt(1, taskToDelete);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Failed to delete task. Task with ID " + taskToDelete + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateTask() {
        try (Connection con = DriverManager.getConnection(connectionUrl, username, password);
             PreparedStatement pstmt = con.prepareStatement("UPDATE Tasks SET title = ?, description = ?, status = ?, priority = ?, due_date = ? WHERE task_id = ?")) {

            Scanner input = new Scanner(System.in);
            getAllTasks();

            System.out.println("Enter the ID of the task you want to update: ");
            int taskIdToUpdate = input.nextInt();
            input.nextLine();

            System.out.println("Enter new title: ");
            String newTitle = input.nextLine();

            System.out.println("Enter new description: ");
            String newDescription = input.nextLine();

            System.out.println("Enter new status (true or false): ");
            boolean newStatus = input.nextBoolean();

            System.out.println("Enter new priority (high, medium, low): ");
            String newPriority = input.nextLine();

            System.out.println("Enter new due date (YYYY-MM-DD): ");
            String newDueDateStr = input.nextLine();

            Date newDueDate = Date.valueOf(newDueDateStr);

            pstmt.setString(1, newTitle);
            pstmt.setString(2, newDescription);
            pstmt.setBoolean(3, newStatus);
            pstmt.setString(4, newPriority);
            pstmt.setDate(5, newDueDate);
            pstmt.setInt(6, taskIdToUpdate);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task updated successfully.");
            } else {
                System.out.println("Failed to update task. Task with ID " + taskIdToUpdate + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

