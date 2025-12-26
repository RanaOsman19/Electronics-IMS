package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private int userId;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AdminDashboard(int userId) {
        this.userId = userId;

        setTitle("Electronics IMS - Admin Dashboard");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 40, 70));

        Font menuFont = new Font("Serif", Font.BOLD, 18);
        JMenu menuInventory = new JMenu("Inventory");
        menuInventory.setFont(menuFont);
        menuInventory.setForeground(Color.WHITE);

        JMenu menuProducts = new JMenu("Products");
        menuProducts.setFont(menuFont);
        menuProducts.setForeground(Color.WHITE);

        JMenu menuPurchases = new JMenu("Purchases");
        menuPurchases.setFont(menuFont);
        menuPurchases.setForeground(Color.WHITE);

        JMenu menuSales = new JMenu("Sales");
        menuSales.setFont(menuFont);
        menuSales.setForeground(Color.WHITE);

        JMenu menuAlerts = new JMenu("Alerts");
        menuAlerts.setFont(menuFont);
        menuAlerts.setForeground(Color.WHITE);

        JMenu menuReports = new JMenu("Reports");
        menuReports.setFont(menuFont);
        menuReports.setForeground(Color.WHITE);

        JMenu menuAccount = new JMenu("Account");
        menuAccount.setFont(menuFont);
        menuAccount.setForeground(Color.WHITE);

        JMenuItem menuItemInventory = new JMenuItem("Manage Inventory");
        JMenuItem menuItemSales = new JMenuItem("Sales Orders");
        JMenuItem menuItemLowStock = new JMenuItem("Low Stock Alerts");
        JMenuItem menuItemLogout = new JMenuItem("Logout");
        JMenuItem productsItem = new JMenuItem("Manage Products");
        JMenuItem purchaseOrdersItem = new JMenuItem("Purchase Orders");
        JMenuItem generateReportItem = new JMenuItem("Generate PDF Report");

        Font menuItemFont = new Font("Serif", Font.PLAIN, 16);
        menuItemInventory.setFont(menuItemFont);
        menuItemSales.setFont(menuItemFont);
        menuItemLowStock.setFont(menuItemFont);
        menuItemLogout.setFont(menuItemFont);
        productsItem.setFont(menuItemFont);
        purchaseOrdersItem.setFont(menuItemFont);
        generateReportItem.setFont(menuItemFont);

        menuProducts.add(productsItem);
        menuInventory.add(menuItemInventory);
        menuPurchases.add(purchaseOrdersItem);
        menuSales.add(menuItemSales);
        menuAlerts.add(menuItemLowStock);
        menuReports.add(generateReportItem);
        menuAccount.add(menuItemLogout);

        menuBar.add(menuProducts);
        menuBar.add(menuInventory);
        menuBar.add(menuPurchases);
        menuBar.add(menuSales);
        menuBar.add(menuAlerts);
        menuBar.add(menuReports);
        menuBar.add(menuAccount);

        setJMenuBar(menuBar);

        // Main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(230, 230, 230));

        // -------------------- Inventory Panel --------------------
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(new Color(245, 245, 245));

        // Title above table
        JLabel inventoryTitle = new JLabel("Inventory Overview", SwingConstants.CENTER);
        inventoryTitle.setFont(new Font("Serif", Font.BOLD, 32));
        inventoryTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        inventoryPanel.add(inventoryTitle, BorderLayout.NORTH);

        // Table
        String[] columns = { "Inventory ID", "Product", "Location", "Stock", "Reorder Point", "Status" };
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columns, 0);
        JTable inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setFont(new Font("Serif", Font.PLAIN, 25));
        inventoryTable.setRowHeight(28);
        JScrollPane tableScroll = new JScrollPane(inventoryTable);
        inventoryPanel.add(tableScroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(245, 245, 245));
        JButton addButton = new JButton("Add Inventory");
        JButton editButton = new JButton("Edit Stock");
        JButton deleteButton = new JButton("Delete Inventory");
        addButton.setFont(new Font("Serif", Font.BOLD, 25));
        editButton.setFont(new Font("Serif", Font.BOLD, 25));
        deleteButton.setFont(new Font("Serif", Font.BOLD, 25));
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        inventoryPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(inventoryPanel, "Inventory");

        // -------------------- Sales Panel --------------------
        JPanel salesPanel = new JPanel(new BorderLayout());

        String[] salesCols = { "Order ID", "Customer", "Date", "Total" };
        DefaultTableModel salesModel = new DefaultTableModel(salesCols, 0);
        JTable salesTable = new JTable(salesModel);

        salesPanel.add(new JLabel("Sales Orders", SwingConstants.CENTER), BorderLayout.NORTH);
        salesPanel.add(new JScrollPane(salesTable), BorderLayout.CENTER);

        mainPanel.add(salesPanel, "Sales");

        loadSales(salesModel);

        // -------------------- Low Stock Panel --------------------
        JPanel alertPanel = new JPanel(new BorderLayout());

        String[] alertCols = { "Inventory ID", "Product", "Location", "Stock", "Reorder Point" };
        DefaultTableModel alertModel = new DefaultTableModel(alertCols, 0);
        JTable alertTable = new JTable(alertModel);

        alertPanel.add(new JLabel("Low Stock Alerts", SwingConstants.CENTER), BorderLayout.NORTH);
        alertPanel.add(new JScrollPane(alertTable), BorderLayout.CENTER);

        mainPanel.add(alertPanel, "Alerts");
        loadLowStockAlerts(alertModel);

        // -------------------- Load inventory from view --------------------
        loadInventoryData(inventoryTableModel);

        // -------------------- Purchase Orders Panel --------------------
        JPanel purchasePanel = new JPanel(new BorderLayout());

        String[] poCols = { "PO ID", "Supplier", "Date", "Status" };
        DefaultTableModel poModel = new DefaultTableModel(poCols, 0);
        JTable poTable = new JTable(poModel);

        purchasePanel.add(new JLabel("Purchase Orders", SwingConstants.CENTER), BorderLayout.NORTH);
        purchasePanel.add(new JScrollPane(poTable), BorderLayout.CENTER);

        mainPanel.add(purchasePanel, "Purchases");

        loadPurchaseOrders(poModel);

        // -------------------- Products Panel --------------------
        JPanel productsPanel = new JPanel(new BorderLayout());

        String[] productCols = { "Product ID", "Name", "SKU", "Price" };
        DefaultTableModel productModel = new DefaultTableModel(productCols, 0);
        JTable productTable = new JTable(productModel);

        productsPanel.add(new JLabel("Products", SwingConstants.CENTER), BorderLayout.NORTH);
        productsPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JButton addProduct = new JButton("Add");
        JButton editProduct = new JButton("Edit");
        JButton deleteProduct = new JButton("Delete");

        JPanel productBtns = new JPanel();
        productBtns.add(addProduct);
        productBtns.add(editProduct);
        productBtns.add(deleteProduct);

        productsPanel.add(productBtns, BorderLayout.SOUTH);

        mainPanel.add(productsPanel, "Products");
        loadProducts(productModel);

        // -------------------- Button Actions --------------------
        addButton.addActionListener(e -> addInventory(inventoryTableModel));
        editButton.addActionListener(e -> editInventory(inventoryTable, inventoryTableModel));
        deleteButton.addActionListener(e -> deleteInventory(inventoryTable, inventoryTableModel));

        // Menu actions
        menuItemInventory.addActionListener(e -> cardLayout.show(mainPanel, "Inventory"));
        menuItemSales.addActionListener(e -> cardLayout.show(mainPanel, "Sales"));
        menuItemLowStock.addActionListener(e -> cardLayout.show(mainPanel, "Alerts"));
        productsItem.addActionListener(e -> cardLayout.show(mainPanel, "Products"));
        purchaseOrdersItem.addActionListener(e -> cardLayout.show(mainPanel, "Purchases"));
        menuItemLogout.addActionListener(e -> logout());

        add(mainPanel);

    }

    private void loadInventoryData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM inventory_status");
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("inventoryid"),
                        rs.getString("product_name"),
                        rs.getString("location_name"),
                        rs.getInt("stocklevel"),
                        rs.getInt("reorderpoint"),
                        rs.getString("stock_status")
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load inventory data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProducts(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT productid, product_name, sku, price FROM product_details");
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("productid"),
                        rs.getString("product_name"),
                        rs.getString("sku"),
                        rs.getDouble("price")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPurchaseOrders(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT purchaseorderid, supplier_name, orderdate, status FROM purchase_order_summary");
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("purchaseorderid"),
                        rs.getString("supplier_name"),
                        rs.getDate("orderdate"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSales(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT salesorderid, customer_email, orderdate, order_total FROM sales_order_summary");
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("salesorderid"),
                        rs.getString("customer_email"),
                        rs.getDate("orderdate"),
                        rs.getDouble("order_total")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addInventory(DefaultTableModel tableModel) {
        JTextField productIdField = new JTextField();
        JTextField locationIdField = new JTextField();
        JTextField reorderField = new JTextField();

        Object[] message = {
                "Product ID:", productIdField,
                "Location ID:", locationIdField,
                "Reorder Point:", reorderField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Inventory", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL add_inventory(?, ?, ?, ?)}")) {

                stmt.setInt(1, userId);
                stmt.setInt(2, Integer.parseInt(productIdField.getText()));
                stmt.setInt(3, Integer.parseInt(locationIdField.getText()));
                stmt.setInt(4, Integer.parseInt(reorderField.getText()));

                stmt.execute();
                JOptionPane.showMessageDialog(this, "Inventory added successfully!");
                loadInventoryData(tableModel);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add inventory: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editInventory(JTable table, DefaultTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an inventory row to edit");
            return;
        }

        int inventoryId = (int) table.getValueAt(selectedRow, 0);
        String currentStock = table.getValueAt(selectedRow, 3).toString();

        String newStock = JOptionPane.showInputDialog(
                this,
                "Enter new stock level:",
                currentStock);

        if (newStock != null) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL update_inventory_stock(?, ?, ?)}")) {

                stmt.setInt(1, userId);
                stmt.setInt(2, inventoryId);
                stmt.setInt(3, Integer.parseInt(newStock));

                stmt.execute();

                JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                loadInventoryData(tableModel);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update stock: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteInventory(JTable table, DefaultTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an inventory row to delete");
            return;
        }

        int inventoryId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected inventory?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL delete_inventory(?, ?)}")) {

                stmt.setInt(1, userId);
                stmt.setInt(2, inventoryId);
                stmt.execute();

                JOptionPane.showMessageDialog(this, "Inventory deleted successfully!");
                loadInventoryData(tableModel);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete inventory: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private void loadLowStockAlerts(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM low_stock_alerts");
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("inventoryid"),
                        rs.getString("product"),
                        rs.getString("location"),
                        rs.getInt("stocklevel"),
                        rs.getInt("reorderpoint")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}