package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class SupplierDashboard extends JFrame {

    private int supplierUserId;
    private int supplierId;
    private Connection conn;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTable productTable, lowStockTable;
    private DefaultTableModel productTableModel, lowStockTableModel;

    // Define the style colors from the image
    private final Color NAVY_BLUE = new Color(30, 40, 70);
    private final Color LIGHT_LAVENDER = new Color(245, 230, 250);
    private final Color TEXT_WHITE = Color.WHITE;

    public SupplierDashboard(int supplierUserId, int supplierId) {
        this.supplierUserId = supplierUserId;
        this.supplierId = supplierId;

        setTitle("Electronics IMS - Supplier Dashboard");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            System.exit(0);
        }

        initUI();
        loadProducts(productTableModel, this.supplierId);
        loadLowStockAlerts(lowStockTableModel);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(NAVY_BLUE);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        menuBar.add(createMenuLabel("Products", "Products"));
        menuBar.add(Box.createRigidArea(new Dimension(15, 0)));
        menuBar.add(createMenuLabel("Stock Alerts", "LowStock"));

        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(LIGHT_LAVENDER);

        mainPanel.add(createTablePanel("Product Inventory", productTableModel = new DefaultTableModel(
                new String[] { "ID", "Product", "SKU", "Price", "Stock", "Reorder Point", "Status" }, 0),
                productTable = new JTable()), "Products");

        mainPanel.add(createTablePanel("Restocking Needs", lowStockTableModel = new DefaultTableModel(
                new String[] { "ID", "Product Name", "Location", "Stock", "Reorder Point", "Qty to Order" }, 0),
                lowStockTable = new JTable()), "LowStock");

        add(mainPanel, BorderLayout.CENTER);
    }

    // Helper to create the menu labels that act as switchers
    private JLabel createMenuLabel(String text, String cardName) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_WHITE);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, cardName);
            }
        });
        return label;
    }

    // Helper to style the panels and tables like the image
    private JPanel createTablePanel(String title, DefaultTableModel model, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_LAVENDER);

        // Title Header
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 32));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Table Styling
        table.setModel(model);
        table.setRowHeight(30);
        table.setBackground(NAVY_BLUE);
        table.setForeground(TEXT_WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setGridColor(Color.GRAY);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(LIGHT_LAVENDER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
    }

    private void loadProducts(DefaultTableModel model, int loggedInSupplierId) {
        model.setRowCount(0);
        String sql = "SELECT * FROM supplier_product_details WHERE supplierid = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, loggedInSupplierId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[] {
                            rs.getInt("productid"),
                            rs.getString("product_name"),
                            rs.getString("SKU"),
                            rs.getDouble("price"),
                            rs.getDouble("vatrate"),
                            rs.getString("category")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLowStockAlerts(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM low_stock_alerts");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("inventoryid"),
                        rs.getString("product"),
                        rs.getString("location"),
                        rs.getInt("stocklevel"),
                        rs.getInt("reorderpoint"),
                        "Resolve"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierDashboard(2, 1).setVisible(true));
    }
}