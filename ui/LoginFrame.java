package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        setTitle("Electronics IMS - Login");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 40, 70));

        // Welcome label at top
        JLabel welcomeLabel = new JLabel("Welcome to Electronics IMS", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 40));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Center panel with left image and right login form
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.setBackground(new Color(30, 40, 70));

        // Left panel for image
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(30, 40, 70));
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("C:\\Users\\OPT\\Documents\\IMS\\ui\\lg.png");
        Image img = icon.getImage().getScaledInstance(500, 350, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        imagePanel.add(imageLabel);

        // Right panel for login form
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(220, 220, 220));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 20));
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        loginPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 20));
        loginPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 80, 160));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Serif", Font.BOLD, 22));
        loginPanel.add(loginButton, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 16));
        messageLabel.setForeground(Color.RED);
        loginPanel.add(messageLabel, gbc);

        // Add panels to center
        centerPanel.add(imagePanel);
        centerPanel.add(loginPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action
        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection();
                CallableStatement stmt = conn.prepareCall("{CALL login_user(?, ?)}")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("userid");
                String role = rs.getString("role");
                Integer supplierId = rs.getObject("supplierid") != null ? rs.getInt("supplierid") : null;

                messageLabel.setText("Login successful!");
                messageLabel.setForeground(new Color(0, 150, 0));

                if (role.equals("admin")) {
                    SwingUtilities.invokeLater(() -> new AdminDashboard(userId).setVisible(true));
                }
                // else if (role.equals("supplier")) {
                // SwingUtilities.invokeLater(() -> new SupplierDashboard(userId,
                // supplierId).setVisible(true));
                // }

                this.dispose();
            } else {
                messageLabel.setText("Invalid username or password");
                messageLabel.setForeground(Color.RED);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Database error");
            messageLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
