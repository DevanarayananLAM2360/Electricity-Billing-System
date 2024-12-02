import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Customer class
class Customer {
    private int id;
    private String name;
    private double bill;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
        this.bill = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Bill: $" + bill;
    }
}

// Main system class
public class ElectricityBillingSystem {
    private JFrame frame;
    private ArrayList<Customer> customers = new ArrayList<>();

    public ElectricityBillingSystem() {
        showLoginPage();
    }

    // Login Page
    private void showLoginPage() {
        frame = new JFrame("Electricity Billing System - Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("admin123")) {
                showAdminPage();
            } else if (username.equals("customer") && password.equals("cust123")) {
                showCustomerPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel());
        frame.add(loginButton);

        frame.setVisible(true);
    }

    // Admin Page
    private void showAdminPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Admin Page");
        frame.setLayout(new BorderLayout());

        JButton addCustomerButton = new JButton("Add Customer");
        JButton deleteCustomerButton = new JButton("Delete Customer");
        JButton viewCustomersButton = new JButton("View Customers");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(deleteCustomerButton);
        buttonPanel.add(viewCustomersButton);
        buttonPanel.add(logoutButton);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        addCustomerButton.addActionListener(e -> addCustomer());
        deleteCustomerButton.addActionListener(e -> deleteCustomer(displayArea));
        viewCustomersButton.addActionListener(e -> viewCustomers(displayArea));
        logoutButton.addActionListener(e -> showLoginPage());

        frame.revalidate();
        frame.repaint();
    }

    private void addCustomer() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        Object[] message = {
                "Customer ID:", idField,
                "Customer Name:", nameField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();

                customers.add(new Customer(id, name));
                JOptionPane.showMessageDialog(frame, "Customer added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCustomer(JTextArea displayArea) {
        String input = JOptionPane.showInputDialog(frame, "Enter Customer ID to delete:");
        try {
            int id = Integer.parseInt(input);
            boolean found = customers.removeIf(c -> c.getId() == id);
            if (found) {
                JOptionPane.showMessageDialog(frame, "Customer deleted successfully!");
                displayArea.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewCustomers(JTextArea displayArea) {
        if (customers.isEmpty()) {
            displayArea.setText("No customers available.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Customer c : customers) {
                sb.append(c.toString()).append("\n");
            }
            displayArea.setText(sb.toString());
        }
    }

    // Customer Page
    private void showCustomerPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Customer Page");
        frame.setLayout(new BorderLayout());

        JButton calculateBillButton = new JButton("Calculate Bill");
        JButton viewBillButton = new JButton("View Bill");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.add(calculateBillButton);
        buttonPanel.add(viewBillButton);
        buttonPanel.add(logoutButton);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        calculateBillButton.addActionListener(e -> calculateBill(displayArea));
        viewBillButton.addActionListener(e -> viewBill(displayArea));
        logoutButton.addActionListener(e -> showLoginPage());

        frame.revalidate();
        frame.repaint();
    }

    private void calculateBill(JTextArea displayArea) {
        String input = JOptionPane.showInputDialog(frame, "Enter Customer ID:");
        try {
            int id = Integer.parseInt(input);
            for (Customer c : customers) {
                if (c.getId() == id) {
                    String unitsInput = JOptionPane.showInputDialog(frame, "Enter units consumed:");
                    double units = Double.parseDouble(unitsInput);
                    double bill = units * 5; // Assume $5 per unit
                    c.setBill(bill);
                    JOptionPane.showMessageDialog(frame, "Bill calculated successfully!");
                    displayArea.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewBill(JTextArea displayArea) {
        String input = JOptionPane.showInputDialog(frame, "Enter Customer ID:");
        try {
            int id = Integer.parseInt(input);
            for (Customer c : customers) {
                if (c.getId() == id) {
                    displayArea.setText("Customer Name: " + c.getName() + "\nBill: $" + c.getBill());
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ElectricityBillingSystem::new);
        new ElectricityBillingSystem();
    }
}
