package com.ClothesMangement.view;

import com.ClothesMangement.controller.ClothesController;
import com.ClothesMangement.entity.Clothes;
import com.ClothesMangement.entity.Clothes.Size;
import com.ClothesMangement.entity.ClothesType;
import com.ClothesMangement.repository.ClothesTypeRepository;
import com.formdev.flatlaf.FlatClientProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class ClothesFrame extends JPanel {

    @Autowired private ClothesController clothesController;
    @Autowired private ClothesTypeRepository typeRepo;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId = new JTextField();
    private JTextField txtName = new JTextField();
    private JTextField txtPrice = new JTextField();
    private JTextField txtQuantity = new JTextField();
    private JTextField txtColor = new JTextField();
    private JComboBox<Size> cbSize = new JComboBox<>(Size.values());
    private JComboBox<ClothesType> cbType = new JComboBox<>();

    public ClothesFrame() {
        setLayout(new BorderLayout(15, 15));
        initUI();
    }

    @PostConstruct
    public void initData() {
        loadTypesToComboBox();
        btnReloadTable();
    }

    private void initUI() {
        // --- TABLE SETUP ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tên", "Loại", "Giá", "SL", "Màu", "Size"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());

        // --- FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(340, 0));
        formPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 5, 5);

        txtId.setEditable(false);
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID tự động");

        addLabelAndComp(formPanel, "ID:", txtId, gbc, 0);
        addLabelAndComp(formPanel, "Tên SP:", txtName, gbc, 1);
        addLabelAndComp(formPanel, "Loại:", cbType, gbc, 2);
        addLabelAndComp(formPanel, "Giá:", txtPrice, gbc, 3);
        addLabelAndComp(formPanel, "Số lượng:", txtQuantity, gbc, 4);
        addLabelAndComp(formPanel, "Màu sắc:", txtColor, gbc, 5);
        addLabelAndComp(formPanel, "Size:", cbSize, gbc, 6);

        // --- BUTTONS ---
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background: #27ae60; foreground: #ffffff; arc: 10");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, "background: #2980b9; foreground: #ffffff; arc: 10");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "background: #c0392b; foreground: #ffffff; arc: 10");

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear);

        gbc.gridy = 7; gbc.gridwidth = 2; gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);

        // --- ACTIONS ---
        btnAdd.addActionListener(e -> {
            try {
                clothesController.saveClothes(getClothesFromForm());
                JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm!");
                btnReloadTable(); clearForm();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", 0); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if(txtId.getText().isEmpty()) throw new Exception("Hãy chọn sản phẩm trong bảng!");
                Clothes c = getClothesFromForm();
                c.setId(Integer.parseInt(txtId.getText()));
                clothesController.saveClothes(c);
                JOptionPane.showMessageDialog(this, "Đã cập nhật!");
                btnReloadTable();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", 0); }
        });

        btnDelete.addActionListener(e -> {
            if(!txtId.getText().isEmpty()){
                int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
                if(confirm == JOptionPane.YES_OPTION) {
                    clothesController.deleteClothes(Integer.parseInt(txtId.getText()));
                    btnReloadTable(); clearForm();
                }
            }
        });

        btnClear.addActionListener(e -> {
            clearForm();
            loadTypesToComboBox();
        });
    }

    private Clothes getClothesFromForm() throws Exception {
        if (txtName.getText().isBlank() || txtPrice.getText().isBlank() || txtQuantity.getText().isBlank())
            throw new Exception("Vui lòng điền đủ Tên, Giá và SL!");

        ClothesType selectedType = (ClothesType) cbType.getSelectedItem();
        if (selectedType == null) throw new Exception("Chưa có Loại sản phẩm!");

        Clothes c = new Clothes();
        try {
            c.setName(txtName.getText().trim());
            c.setPrice(Double.parseDouble(txtPrice.getText().replace(",", "")));
            c.setQuantity(Integer.parseInt(txtQuantity.getText()));
            c.setColor(txtColor.getText().trim());
            c.setSize((Size) cbSize.getSelectedItem());
            c.setType(selectedType);
        } catch (NumberFormatException e) {
            throw new Exception("Giá hoặc Số lượng phải là số!");
        }
        return c;
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtId.setText(table.getValueAt(row, 0).toString());
            txtName.setText(table.getValueAt(row, 1).toString());
            txtPrice.setText(table.getValueAt(row, 3).toString());
            txtQuantity.setText(table.getValueAt(row, 4).toString());
            txtColor.setText(table.getValueAt(row, 5).toString());
            cbSize.setSelectedItem(table.getValueAt(row, 6));

            String typeInTable = table.getValueAt(row, 2).toString();
            for (int i = 0; i < cbType.getItemCount(); i++) {
                if (cbType.getItemAt(i).getName().equals(typeInTable)) {
                    cbType.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void loadTypesToComboBox() {
        List<ClothesType> types = typeRepo.findAll();
        DefaultComboBoxModel<ClothesType> model = new DefaultComboBoxModel<>();
        for (ClothesType t : types) model.addElement(t);
        cbType.setModel(model);
        cbType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof ClothesType t) setText(t.getName());
                return this;
            }
        });
    }

    public void btnReloadTable() {
        clothesController.refreshTable(tableModel);
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPrice.setText("");
        txtQuantity.setText(""); txtColor.setText(""); cbSize.setSelectedIndex(0);
        table.clearSelection();
    }

    private void addLabelAndComp(JPanel p, String l, JComponent c, GridBagConstraints g, int y) {
        g.gridx = 0; g.gridy = y; g.gridwidth = 1; p.add(new JLabel(l), g);
        g.gridx = 1; p.add(c, g);
    }
}