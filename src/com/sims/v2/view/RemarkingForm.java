package com.sims.v2.view;

import com.sims.v2.controller.RemarkingController;
import com.sims.v2.model.Remarking;
import com.sims.v2.util.ClickListener;
import com.sims.v2.util.CustomDate;
import com.sims.v2.util.DateTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RemarkingForm extends JPanel {
    private RemarkingController controller;
    private ClickListener clickListener;
    private List<Remarking> list;
    DefaultTableModel model;
    private JPanel panel;
    private JButton addButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel openingLabel;
    private JLabel closingLabel;
    private JScrollPane jScrollPane1;
    private JTable remarkingTable;
    private DateTextField openingField;
    private DateTextField closingField;
    private JTextField searchField;
    private JButton deleteButton;
    private JButton removeButton;

    private TableRowSorter<TableModel> rowSorter;
    private Remarking selectedRemarking;
    private Integer selectedRowIndex;

    public RemarkingForm() {
        initComponents();
        clickListener = new ClickListener();
        this.controller = new RemarkingController(this);
        this.list = controller.getList();
        this.model = (DefaultTableModel) remarkingTable.getModel();
        showDataTable();
    }

    private void showDataTable() {
        model.setColumnIdentifiers(new Object[]{
                "STT", "Bắt đầu", "Kết thúc"
        });
        int i = 1;
        for (Remarking remarking : list) {
            model.addRow(new Object[]{
                    i++, CustomDate.serialize(remarking.getOpening()), CustomDate.serialize(remarking.getClosing())
            });
        }

        rowSorter = new TableRowSorter<>(remarkingTable.getModel());
        remarkingTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        remarkingTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        remarkingTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        remarkingTable.addMouseListener(getDataRow());
    }

    private MouseAdapter getDataRow() {
        return (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && remarkingTable.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) remarkingTable.getModel();
                    selectedRowIndex = remarkingTable.getSelectedRow();
                    selectedRowIndex = remarkingTable.convertRowIndexToModel(selectedRowIndex);

                    selectedRemarking = list.get(selectedRowIndex);

                    openingField.setText(model.getValueAt(selectedRowIndex, 1).toString());
                    closingField.setText(model.getValueAt(selectedRowIndex, 2).toString());

                }
            }

        });
    }

    private DocumentListener search() {
        return (new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }


    private void save() {
        String rs = "Lưu thất bại!";
        String opening = openingField.getText().trim();
        String closing = closingField.getText().trim();

        if (opening.isEmpty() || closing.isEmpty()) {
            rs = "Vui lòng nhập vào thời gian!";
        } else {
            boolean response = false;
            boolean checkId = true;
            List<Remarking> newList = new ArrayList<>();

            if (selectedRemarking != null) {
                selectedRemarking.setOpening(CustomDate.deserialize(opening));
                selectedRemarking.setClosing(CustomDate.deserialize(closing));

                for (Remarking s : list) {
                    if (selectedRemarking.getOpening().equals(s.getOpening()) && selectedRemarking.getClosing().equals(s.getClosing())) {
                        newList.add(selectedRemarking);
                    } else {
                        if (opening.equals(s.getOpening()) && closing.equals(s.getClosing())) {
                            checkId = false;
                            break;
                        }
                        newList.add(s);
                    }
                    if (checkId == true){
                        response = controller.update(selectedRemarking);
                    }
                }
            } else {
                for (Remarking s : list) {
                    if (opening.equals(s.getOpening()) && closing.equals(s.getClosing())) {
                        checkId = false;
                        break;
                    }
                }
                if (checkId == true) {
                    Remarking newRemarking = new Remarking();
                    newRemarking.setOpening(CustomDate.deserialize(opening));
                    newRemarking.setClosing(CustomDate.deserialize(closing));
                    response = controller.create(newRemarking);
                    newList = controller.getList();
                }
            }

            if (checkId == true) {
                if (response == true) {
                    if (selectedRemarking != null) {
                        model.setValueAt(opening, selectedRowIndex, 1);
                        model.setValueAt(closing, selectedRowIndex, 2);
                    } else {
                        Integer i = 0;
                        if (model.getRowCount() > 0) {
                            i = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString());
                        }
                        model.addRow(new Object[]{
                                ++i, opening, closing
                        });
                    }

                    model.fireTableDataChanged();
                    remarkingTable.repaint();
                    rs = "Lưu thành công!";
                    list = newList;
                }
            }
            else {
                rs = "Thời gian phúc khảo đã tồn tại!";
            }

        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            List<Remarking> newList = new ArrayList<>();
            if (selectedRemarking != null) {
                for (Remarking s : list) {
                    if (!(selectedRemarking.getOpening().equals(s.getOpening()) && selectedRemarking.getClosing().equals(s.getClosing()))) {
                        newList.add(s);
                    }
                }
            }

            boolean response = controller.delete(selectedRemarking);
            if (response == true) {
                model.removeRow(selectedRowIndex);
                model.fireTableDataChanged();
                remarkingTable.repaint();
                rs = "Xóa thành công!";
                list = newList;
            }
            clickListener.showMessage(rs);
            this.refresh();
        }
    }

    private void clearAll() {
        if (clickListener.deleteClick()) {
            String rs = "Có lỗi xảy ra!";
            boolean response = controller.deleteAll();
            if (response == true) {
                list.removeAll(list);
                model.setRowCount(0);
                rs = "Đã xóa thành công!";
            }
            clickListener.showMessage(rs);
            this.refresh();
        }
    }


    private void refresh() {
        openingField.setText("");
        closingField.setText("");
        selectedRemarking = null;
        selectedRowIndex = null;
        remarkingTable.getSelectionModel().clearSelection();
        openingField.requestFocus();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("LỊCH PHÚC KHẢO");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton("+ Thêm mới");
        addButton.addActionListener(e -> refresh());

        openingLabel = new JLabel("Bắt đầu: ");
        closingLabel = new JLabel("Kết thúc: ");

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());

        openingField = new DateTextField();
        closingField = new DateTextField();

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(search());

        jScrollPane1 = new JScrollPane();
        remarkingTable = new JTable();
        remarkingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        remarkingTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "STT", "Bắt đầu", "Kết thúc"
                }
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        });

        jScrollPane1.setViewportView(remarkingTable);

        deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> delete());

        removeButton = new JButton("Xóa tất cả");
        removeButton.addActionListener(e -> clearAll());

        GroupLayout layout = new GroupLayout(panel);
        settingLayout(layout);

        add(panel);
    }

    private void settingLayout(GroupLayout layout) {
        panel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(addButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(openingLabel)
                                                        .addComponent(closingLabel))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(openingField)
                                                        .addComponent(closingField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
                                                .addGap(27, 27, 27))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(saveButton)
                                                .addGap(45, 45, 45)
                                                .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addGap(55, 55, 55))
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(titleLabel)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(500, 500, 500)
                                                .addComponent(searchField)
                                        )
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(deleteButton)
                                                .addComponent(removeButton)
                                        )
                                )
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titleLabel)
                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(addButton))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(openingLabel)
                                                        .addComponent(openingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(closingLabel)
                                                        .addComponent(closingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(25, 25, 25)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(resetButton)
                                                        .addComponent(saveButton))
                                                .addGap(73, 73, 73))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                                        )
                                                )
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(deleteButton)
                                                                .addComponent(removeButton))
                                                )
                                                .addContainerGap(54, Short.MAX_VALUE))))
        );

    }
}
