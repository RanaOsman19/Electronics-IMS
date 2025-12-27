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
        // ---------------- Menu Bar ----------------
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

        JMenu menuSuppliers = new JMenu("Suppliers");
        menuSuppliers.setFont(menuFont);
        menuSuppliers.setForeground(Color.WHITE);

        JMenuItem supplierMenuItem = new JMenuItem("Manage Suppliers");

        JMenuItem menuItemInventory = new JMenuItem("Manage Inventory");
        JMenuItem menuItemSales = new JMenuItem("Sales Orders");
        JMenuItem menuItemLowStock = new JMenuItem("Low Stock Alerts");
        JMenuItem menuItemLogout = new JMenuItem("Logout");
        JMenuItem productsItem = new JMenuItem("Manage Products");
        JMenuItem purchaseOrdersItem = new JMenuItem("Purchase Orders");
        JMenuItem generateReportItem = new JMenuItem("Generate csv Report");

        Font menuItemFont = new Font("Serif", Font.PLAIN, 20);
        menuItemInventory.setFont(menuItemFont);
        menuItemSales.setFont(menuItemFont);
        menuItemLowStock.setFont(menuItemFont);
        menuItemLogout.setFont(menuItemFont);
        productsItem.setFont(menuItemFont);
        purchaseOrdersItem.setFont(menuItemFont);
        generateReportItem.setFont(menuItemFont);
        supplierMenuItem.setFont(menuItemFont);

        menuProducts.add(productsItem);
        menuInventory.add(menuItemInventory);
        menuPurchases.add(purchaseOrdersItem);
        menuSales.add(menuItemSales);
        menuAlerts.add(menuItemLowStock);
        menuReports.add(generateReportItem);
        menuAccount.add(menuItemLogout);
        menuSuppliers.add(supplierMenuItem);

        menuBar.add(menuProducts);
        menuBar.add(menuInventory);
        menuBar.add(menuPurchases);
        menuBar.add(menuSales);
        menuBar.add(menuAlerts);
        menuBar.add(menuReports);
        menuBar.add(menuSuppliers);
        menuBar.add(menuAccount);

        setJMenuBar(menuBar);

        // ---------------- Main Panel ----------------
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(230, 230, 230));

        // ---------------- Inventory Panel ----------------
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(new Color(245, 245, 245));
        JLabel inventoryTitle = new JLabel("Inventory Overview", SwingConstants.CENTER);
        inventoryTitle.setFont(new Font("Serif", Font.BOLD, 32));
        inventoryTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        inventoryPanel.add(inventoryTitle, BorderLayout.NORTH);

        String[] columns = { "Inventory ID", "Product", "Location", "Stock", "Reorder Point", "Status" };
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columns, 0);
        JTable inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setFont(new Font("Serif", Font.PLAIN, 25));
        inventoryTable.setRowHeight(28);
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(245, 245, 245));
        JButton addButton = new JButton("Add Inventory");
        JButton editButton = new JButton("Edit Stock");
        JButton deleteButton = new JButton("Delete Inventory");
        JButton editReorderButton = new JButton("Edit Reorder Point");
        addButton.setFont(new Font("Serif", Font.BOLD, 25));
        editButton.setFont(new Font("Serif", Font.BOLD, 25));
        deleteButton.setFont(new Font("Serif", Font.BOLD, 25));
        editReorderButton.setFont(new Font("Serif", Font.BOLD, 25));
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editReorderButton);
        inventoryPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(inventoryPanel, "Inventory");

        // ---------------- Supplier Panel ----------------
        JPanel supplierPanel = new JPanel(new BorderLayout());
        String[] supplierCols = { "ID", "Name", "Email", "Phone", "City", "Country", "Min Order" };
        DefaultTableModel supplierModel = new DefaultTableModel(supplierCols, 0);
        JTable supplierTable = new JTable(supplierModel);
        supplierTable.setFont(new Font("Serif", Font.PLAIN, 18));
        supplierTable.setRowHeight(25);

        JLabel supplierTitle = new JLabel("Supplier Management", SwingConstants.CENTER);
        supplierTitle.setFont(new Font("Serif", Font.BOLD, 32));
        supplierPanel.add(supplierTitle, BorderLayout.NORTH);
        supplierPanel.add(new JScrollPane(supplierTable), BorderLayout.CENTER);

        JPanel suppBtns = new JPanel();
        JButton addSupp = new JButton("Add Supplier");
        JButton editSupp = new JButton("Edit Contact");
        JButton deleteSupp = new JButton("Delete");
        suppBtns.add(addSupp);
        suppBtns.add(editSupp);
        suppBtns.add(deleteSupp);
        supplierPanel.add(suppBtns, BorderLayout.SOUTH);

        mainPanel.add(supplierPanel, "Suppliers");

        // ---------------- Sales Panel ----------------
        JPanel salesPanel = new JPanel(new BorderLayout());
        String[] salesCols = { "Order ID", "Customer", "Date", "Total" };
        DefaultTableModel salesModel = new DefaultTableModel(salesCols, 0);
        JTable salesTable = new JTable(salesModel);
        salesTable.setFont(new Font("Serif", Font.PLAIN, 25));
        salesTable.setRowHeight(28);

        JLabel salesTitle = new JLabel("Sales Orders", SwingConstants.CENTER);
        salesTitle.setFont(new Font("Serif", Font.BOLD, 32));
        salesTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        salesPanel.add(salesTitle, BorderLayout.NORTH);

        salesPanel.add(new JScrollPane(salesTable), BorderLayout.CENTER);

        JPanel salesBtnPanel = new JPanel();
        JButton addSaleBtn = new JButton("Add Sale");
        JButton editSaleBtn = new JButton("Edit Sale");
        JButton deleteSaleBtn = new JButton("Delete Sale");
        addSaleBtn.setFont(menuItemFont);
        editSaleBtn.setFont(menuItemFont);
        deleteSaleBtn.setFont(menuItemFont);
        salesBtnPanel.add(addSaleBtn);
        salesBtnPanel.add(editSaleBtn);
        salesBtnPanel.add(deleteSaleBtn);
        salesPanel.add(salesBtnPanel, BorderLayout.SOUTH);

        mainPanel.add(salesPanel, "Sales");

        // ---------------- Low Stock Panel ----------------
        JPanel alertPanel = new JPanel(new BorderLayout());
        String[] alertCols = { "Inventory ID", "Product", "Location", "Stock", "Reorder Point", "Action" };
        DefaultTableModel alertModel = new DefaultTableModel(alertCols, 0);
        JTable alertTable = new JTable(alertModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only "Action" column editable for button
            }
        };
        alertTable.setFont(new Font("Serif", Font.PLAIN, 25));
        alertTable.setRowHeight(28);
        alertPanel.add(new JScrollPane(alertTable), BorderLayout.CENTER);
        mainPanel.add(alertPanel, "Alerts");

        JLabel alertTitle = new JLabel("Low Stock Alerts", SwingConstants.CENTER);
        alertTitle.setFont(new Font("Serif", Font.BOLD, 32));
        alertTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        alertPanel.add(alertTitle, BorderLayout.NORTH);

        // ---------------- Purchase Orders Panel ----------------
        JPanel purchasePanel = new JPanel(new BorderLayout());
        String[] poCols = { "PO ID", "Supplier", "Order Date", "Expected Date", "Status" };
        DefaultTableModel poModel = new DefaultTableModel(poCols, 0);
        JTable poTable = new JTable(poModel);
        poTable.setFont(new Font("Serif", Font.PLAIN, 25));
        poTable.setRowHeight(28);

        JLabel purchaseTitle = new JLabel("Purchase Orders", SwingConstants.CENTER);
        purchaseTitle.setFont(new Font("Serif", Font.BOLD, 32));
        purchaseTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        purchasePanel.add(purchaseTitle, BorderLayout.NORTH);

        purchasePanel.add(new JScrollPane(poTable), BorderLayout.CENTER);

        JButton createPO = new JButton("Create PO");
        JButton updatePOStatus = new JButton("Update Status");
        createPO.setFont(new Font("Serif", Font.BOLD, 25));
        updatePOStatus.setFont(new Font("Serif", Font.BOLD, 25));
        JPanel poBtns = new JPanel();
        poBtns.add(createPO);
        poBtns.add(updatePOStatus);
        purchasePanel.add(poBtns, BorderLayout.SOUTH);

        mainPanel.add(purchasePanel, "Purchases");

        // ---------------- Products Panel ----------------
        JPanel productsPanel = new JPanel(new BorderLayout());
        String[] productCols = { "Product ID", "Name", "SKU", "Price", "VAT", "Category" };
        DefaultTableModel productModel = new DefaultTableModel(productCols, 0);
        JTable productTable = new JTable(productModel);
        productTable.setFont(new Font("Serif", Font.PLAIN, 25));
        productTable.setRowHeight(28);

        JLabel productsTitle = new JLabel("Product Management", SwingConstants.CENTER);
        productsTitle.setFont(new Font("Serif", Font.BOLD, 32));
        productsTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        productsPanel.add(productsTitle, BorderLayout.NORTH);

        productsPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JButton addProduct = new JButton("Add");
        JButton editProduct = new JButton("Edit");
        JButton deleteProduct = new JButton("Delete");
        addProduct.setFont(new Font("Serif", Font.BOLD, 25));
        editProduct.setFont(new Font("Serif", Font.BOLD, 25));
        deleteProduct.setFont(new Font("Serif", Font.BOLD, 25));

        JPanel productBtns = new JPanel();
        productBtns.add(addProduct);
        productBtns.add(editProduct);
        productBtns.add(deleteProduct);
        productsPanel.add(productBtns, BorderLayout.SOUTH);

        mainPanel.add(productsPanel, "Products");

        add(mainPanel);

        // ---------------- Load Data ----------------
        loadInventoryData(inventoryTableModel);
        loadProducts(productModel);
        loadPurchaseOrders(poModel);
        loadSales(salesModel);
        loadLowStockAlerts(alertModel);
        loadSuppliers(supplierModel);

        // ---------------- Button Actions ----------------
        menuItemInventory.addActionListener(e -> cardLayout.show(mainPanel, "Inventory"));
        menuItemSales.addActionListener(e -> cardLayout.show(mainPanel, "Sales"));
        menuItemLowStock.addActionListener(e -> cardLayout.show(mainPanel, "Alerts"));
        productsItem.addActionListener(e -> cardLayout.show(mainPanel, "Products"));
        purchaseOrdersItem.addActionListener(e -> cardLayout.show(mainPanel, "Purchases"));
        generateReportItem.addActionListener(e -> exportReport());
        menuItemLogout.addActionListener(e -> logout());

        addButton.addActionListener(e -> addInventory(inventoryTableModel));
        editButton.addActionListener(e -> editInventory(inventoryTable, inventoryTableModel));
        deleteButton.addActionListener(e -> deleteInventory(inventoryTable, inventoryTableModel));
        editReorderButton.addActionListener(e -> editReorderPoint(inventoryTable, inventoryTableModel));

        supplierMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Suppliers"));
        addSupp.addActionListener(e -> addSupplier(supplierModel));
        editSupp.addActionListener(e -> editSupplier(supplierTable, supplierModel));
        deleteSupp.addActionListener(e -> deleteSupplier(supplierTable, supplierModel));

        addProduct.addActionListener(e -> addProduct(productModel));
        editProduct.addActionListener(e -> editProduct(productTable, productModel));
        deleteProduct.addActionListener(e -> deleteProduct(productTable, productModel));

        updatePOStatus.addActionListener(e -> updatePOStatus(poTable, poModel));

        createPO.addActionListener(e -> {
            int supplierId = Integer.parseInt(JOptionPane.showInputDialog(this, "Supplier ID:"));
            createPurchaseOrder(userId, supplierId, poTable); // poTable is your JTable in Purchases panel
            loadPurchaseOrders((DefaultTableModel) poTable.getModel()); // refresh table after creation
        });

        addSaleBtn.addActionListener(e -> {
            String customerEmail = JOptionPane.showInputDialog(this, "Customer Email:");
            addSale(userId, customerEmail, salesTable); // salesTable is your JTable in Sales panel
            loadSales((DefaultTableModel) salesTable.getModel()); // refresh table after adding sale
        });

        editSaleBtn.addActionListener(e -> editSale(salesTable, salesModel, inventoryTableModel, alertModel));
        deleteSaleBtn.addActionListener(e -> deleteSale(salesTable, salesModel, inventoryTableModel, alertModel));
    }

    // ---------------- Load Methods ----------------
    private void loadInventoryData(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM inventory_status");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("inventoryid"),
                        rs.getString("product_name"),
                        rs.getString("location_name"),
                        rs.getInt("stocklevel"),
                        rs.getInt("reorderpoint"),
                        rs.getString("stock_status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProducts(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM product_details");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("productid"),
                        rs.getString("product_name"),
                        rs.getString("sku"),
                        rs.getDouble("price"),
                        rs.getDouble("vatrate"),
                        rs.getString("category")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPurchaseOrders(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM purchase_order_summary");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("purchaseorderid"),
                        rs.getString("supplier_name"),
                        rs.getDate("orderdate"),
                        rs.getDate("expecteddate"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSales(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM sales_order_summary");
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

    // ---------------- Inventory CRUD ----------------
    private void addInventory(DefaultTableModel model) {
        JTextField productIdField = new JTextField();
        JTextField locationIdField = new JTextField();
        JTextField reorderField = new JTextField();

        Object[] msg = {
                "Product ID:", productIdField,
                "Location ID:", locationIdField,
                "Reorder Point:", reorderField
        };
        int option = JOptionPane.showConfirmDialog(this, msg, "Add Inventory", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL add_inventory(?, ?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, Integer.parseInt(productIdField.getText()));
                cs.setInt(3, Integer.parseInt(locationIdField.getText()));
                cs.setInt(4, Integer.parseInt(reorderField.getText()));
                cs.execute();
                JOptionPane.showMessageDialog(this, "Inventory added successfully!");
                loadInventoryData(model);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void editInventory(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit");
            return;
        }
        int inventoryId = (int) table.getValueAt(row, 0);
        String currentStock = table.getValueAt(row, 3).toString();
        String newStock = JOptionPane.showInputDialog(this, "New stock level:", currentStock);
        if (newStock != null) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL update_inventory_stock(?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, inventoryId);
                cs.setInt(3, Integer.parseInt(newStock));
                cs.execute();
                JOptionPane.showMessageDialog(this, "Stock updated!");
                loadInventoryData(model);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void editReorderPoint(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit");
            return;
        }
        int inventoryId = (int) table.getValueAt(row, 0);
        String current = table.getValueAt(row, 4).toString();
        String newVal = JOptionPane.showInputDialog(this, "New Reorder Point:", current);
        if (newVal != null) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL update_inventory_reorderpoint(?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, inventoryId);
                cs.setInt(3, Integer.parseInt(newVal));
                cs.execute();
                JOptionPane.showMessageDialog(this, "Reorder point updated!");
                loadInventoryData(model);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void deleteInventory(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete");
            return;
        }
        int inventoryId = (int) table.getValueAt(row, 0);
        int option = JOptionPane.showConfirmDialog(this, "Delete selected inventory?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL delete_inventory(?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, inventoryId);
                cs.execute();
                JOptionPane.showMessageDialog(this, "Inventory deleted!");
                loadInventoryData(model);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void loadSuppliers(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM supplier_profile")) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("supplierid"), rs.getString("name"), rs.getString("email"),
                        rs.getString("phone"), rs.getString("city"), rs.getString("country"),
                        rs.getDouble("minordervalue")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSupplier(DefaultTableModel model) {
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField city = new JTextField();
        JTextField country = new JTextField();
        JTextField minOrder = new JTextField();

        Object[] msg = { "Name:", name, "Email:", email, "Phone:", phone, "City:", city, "Country:", country,
                "Min Order:", minOrder };
        if (JOptionPane.showConfirmDialog(this, msg, "New Supplier",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL add_supplier(?,?,?,?,?,?,?,?,?)}")) {
                cs.setInt(1, userId);
                cs.setString(2, name.getText());
                cs.setString(3, email.getText());
                cs.setString(4, phone.getText());
                cs.setString(5, ""); // Street (optional)
                cs.setString(6, city.getText());
                cs.setString(7, country.getText());
                cs.setDouble(8, Double.parseDouble(minOrder.getText()));
                cs.setString(9, "USD"); // Currency
                cs.execute();
                loadSuppliers(model);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void editSupplier(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a supplier to edit");
            return;
        }

        int supplierId = (int) table.getValueAt(row, 0);

        JTextField name = new JTextField((String) table.getValueAt(row, 1));
        JTextField email = new JTextField((String) table.getValueAt(row, 2));
        JTextField phone = new JTextField((String) table.getValueAt(row, 3));
        JTextField city = new JTextField((String) table.getValueAt(row, 4));
        JTextField country = new JTextField((String) table.getValueAt(row, 5));
        JTextField minOrder = new JTextField(table.getValueAt(row, 6).toString());

        Object[] msg = {
                "Name:", name,
                "Email:", email,
                "Phone:", phone,
                "City:", city,
                "Country:", country,
                "Min Order:", minOrder
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Edit Supplier",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL update_supplier(?,?,?,?,?,?,?,?)}")) {

                cs.setInt(1, userId);
                cs.setInt(2, supplierId);
                cs.setString(3, name.getText());
                cs.setString(4, email.getText());
                cs.setString(5, phone.getText());
                cs.setString(6, city.getText());
                cs.setString(7, country.getText());
                cs.setDouble(8, Double.parseDouble(minOrder.getText()));

                cs.execute();
                JOptionPane.showMessageDialog(this, "Supplier updated!");
                loadSuppliers(model);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void deleteSupplier(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a supplier to delete");
            return;
        }

        int supplierId = (int) table.getValueAt(row, 0);

        int option = JOptionPane.showConfirmDialog(this, "Delete selected supplier?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL delete_supplier(?, ?)}")) {

                cs.setInt(1, userId);
                cs.setInt(2, supplierId);
                cs.execute();
                JOptionPane.showMessageDialog(this, "Supplier deleted!");
                loadSuppliers(model);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    // ---------------- Products CRUD ----------------
    private void addProduct(DefaultTableModel model) {
        JTextField name = new JTextField();
        JTextField sku = new JTextField();
        JTextField price = new JTextField();
        JTextField vat = new JTextField();
        JTextField category = new JTextField();

        Object[] msg = { "Name:", name, "SKU:", sku, "Price:", price, "VAT:", vat, "Category:", category };
        if (JOptionPane.showConfirmDialog(this, msg, "Add Product",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL add_product(?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setString(2, name.getText());
                cs.setString(3, sku.getText());
                cs.setString(4, "");
                cs.setDouble(5, Double.parseDouble(price.getText()));
                cs.setDouble(6, Double.parseDouble(vat.getText()));
                cs.setString(7, category.getText());
                cs.execute();
                loadProducts(model);
                loadInventoryData(model);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void editProduct(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int productId = (int) table.getValueAt(row, 0);

        JTextField name = new JTextField((String) table.getValueAt(row, 1));
        JTextField sku = new JTextField((String) table.getValueAt(row, 2));
        JTextField price = new JTextField(table.getValueAt(row, 3).toString());
        JTextField vat = new JTextField(table.getValueAt(row, 4).toString());
        JTextField category = new JTextField((String) table.getValueAt(row, 5));

        Object[] msg = { "Name:", name, "SKU:", sku, "Price:", price, "VAT:", vat, "Category:", category };
        if (JOptionPane.showConfirmDialog(this, msg, "Edit Product",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL update_product_details(?,?,?,?,?,?,?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, productId);
                cs.setString(3, name.getText());
                cs.setString(4, sku.getText());
                cs.setDouble(5, Double.parseDouble(price.getText()));
                cs.setDouble(6, Double.parseDouble(vat.getText()));
                cs.setString(7, category.getText());
                cs.execute();
                loadProducts(model);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void deleteProduct(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int productId = (int) table.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete product?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL delete_product(?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, productId);
                cs.execute();
                loadProducts(model);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    // ---------------- Purchase Orders ----------------
    public void createPurchaseOrder(int userId, int supplierId, JTable itemsTable) {
        try (Connection conn = DBConnection.getConnection()) {

            // Build JSON array from JTable
            DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
            StringBuilder json = new StringBuilder("[");

            for (int i = 0; i < model.getRowCount(); i++) {
                int inventoryId = (int) model.getValueAt(i, 0); // inventoryid column
                int productId = (int) model.getValueAt(i, 1); // productid column
                int qty = (int) model.getValueAt(i, 2); // qty column
                double cost = (double) model.getValueAt(i, 3); // unit cost column

                json.append("{")
                        .append("\"inventoryid\":").append(inventoryId).append(",")
                        .append("\"productid\":").append(productId).append(",")
                        .append("\"qty\":").append(qty).append(",")
                        .append("\"cost\":").append(cost)
                        .append("}");

                if (i < model.getRowCount() - 1)
                    json.append(",");
            }
            json.append("]");

            // Call stored procedure
            CallableStatement cs = conn.prepareCall("{call create_purchase_order(?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, supplierId);
            cs.setString(3, json.toString());

            cs.execute();
            JOptionPane.showMessageDialog(null, "Purchase Order created successfully!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void updatePOStatus(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a PO");
            return;
        }
        int poId = (int) table.getValueAt(row, 0);
        String currentStatus = (String) table.getValueAt(row, 4);
        String[] options = { "Pending", "Received", "Cancelled" };
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select status:", "Update Status",
                JOptionPane.PLAIN_MESSAGE, null, options, currentStatus);
        if (newStatus != null) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL update_purchase_order_status(?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, poId);
                cs.setString(3, newStatus);
                cs.execute();
                JOptionPane.showMessageDialog(this, "PO Status Updated!");
                loadPurchaseOrders(model);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    // ---------------- Sales CRUD ----------------
    private void addSale(int userId, String customerEmail, JTable itemsTable) {
        try (Connection conn = DBConnection.getConnection()) {

            // Build JSON array from JTable
            DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
            StringBuilder json = new StringBuilder("[");

            for (int i = 0; i < model.getRowCount(); i++) {
                int inventoryId = (int) model.getValueAt(i, 0); // inventoryid column
                int productId = (int) model.getValueAt(i, 1); // productid column
                int qty = (int) model.getValueAt(i, 2); // qty column
                double price = (double) model.getValueAt(i, 3); // price column

                json.append("{")
                        .append("\"inventoryid\":").append(inventoryId).append(",")
                        .append("\"productid\":").append(productId).append(",")
                        .append("\"qty\":").append(qty).append(",")
                        .append("\"price\":").append(price)
                        .append("}");

                if (i < model.getRowCount() - 1)
                    json.append(",");
            }
            json.append("]");

            // Call stored procedure
            CallableStatement cs = conn.prepareCall("{call create_sales_order(?,?,?)}");
            cs.setInt(1, userId);
            cs.setString(2, customerEmail);
            cs.setString(3, json.toString());

            cs.execute();
            JOptionPane.showMessageDialog(null, "Sales Order created successfully!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void editSale(JTable table, DefaultTableModel salesModel, DefaultTableModel invModel,
            DefaultTableModel alertModel) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a sale");
            return;
        }
        int saleId = (int) table.getValueAt(row, 0);
        String newEmail = JOptionPane.showInputDialog(this, "New customer email:", table.getValueAt(row, 1));
        if (newEmail != null) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL sell_product(?, ?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, saleId);
                cs.setString(3, newEmail);
                cs.execute();
                JOptionPane.showMessageDialog(this, "Sale updated!");
                loadSales(salesModel);
                loadInventoryData(invModel);
                loadLowStockAlerts(alertModel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void deleteSale(JTable table, DefaultTableModel salesModel, DefaultTableModel invModel,
            DefaultTableModel alertModel) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a sale");
            return;
        }
        int saleId = (int) table.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete selected sale?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                    CallableStatement cs = conn.prepareCall("{CALL delete_sales_order(?, ?)}")) {
                cs.setInt(1, userId);
                cs.setInt(2, saleId);
                cs.execute();
                JOptionPane.showMessageDialog(this, "Sale deleted!");
                loadSales(salesModel);
                loadInventoryData(invModel);
                loadLowStockAlerts(alertModel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void exportReport() {
        String[] reportTypes = { "Inventory Turnover", "Low Stock Alerts", "Restocking Needs" };
        String selectedReport = (String) JOptionPane.showInputDialog(
                this,
                "Select report to export:",
                "Export Report",
                JOptionPane.PLAIN_MESSAGE,
                null,
                reportTypes,
                reportTypes[0]);

        if (selectedReport == null)
            return; // User canceled

        // Choose file location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV Report");
        fileChooser.setSelectedFile(new java.io.File(selectedReport.replace(" ", "_") + ".csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION)
            return;

        java.io.File fileToSave = fileChooser.getSelectedFile();

        String query = "";
        switch (selectedReport) {
            case "Inventory Turnover":
                query = "SELECT * FROM inventory_turnover";
                break;
            case "Low Stock Alerts":
                query = "SELECT * FROM low_stock_alerts";
                break;
            case "Restocking Needs":
                query = "SELECT * FROM restocking_needs";
                break;
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                java.io.FileWriter fw = new java.io.FileWriter(fileToSave)) {

            // Write CSV header
            java.sql.ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                fw.append(meta.getColumnName(i));
                if (i < columnCount)
                    fw.append(",");
            }
            fw.append("\n");

            // Write data rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fw.append(rs.getString(i));
                    if (i < columnCount)
                        fw.append(",");
                }
                fw.append("\n");
            }

            fw.flush();
            JOptionPane.showMessageDialog(this, "Report exported successfully to:\n" + fileToSave.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting report:\n" + e.getMessage());
        }
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard(1).setVisible(true));
    }
}
