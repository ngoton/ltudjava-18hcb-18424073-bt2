package com.sims.v2.view;

import com.sims.v2.controller.CheckingController;
import com.sims.v2.model.Application;
import com.sims.v2.model.Remarking;
import com.sims.v2.util.ClickListener;
import com.sims.v2.util.ColumnGroup;
import com.sims.v2.util.CustomDate;
import com.sims.v2.util.GroupableTableHeader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckingForm extends JPanel {
    private static Float basicMark = 5.0f;
    private CheckingController controller;
    private ClickListener clickListener;
    private List<Application> list;
    DefaultTableModel model;
    private List<Remarking> remarkingList;
    DefaultComboBoxModel remarkingBoxModel;
    private JPanel panel;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JComboBox remarkingBox;
    private JScrollPane jScrollPane1;
    private JTable applicationTable;
    private JTextField searchField;
    private JButton deleteButton;
    private JButton removeButton;
    private JTextField mdMarkField;
    private JTextField fnMarkField;
    private JTextField otMarkField;
    private JTextField markField;
    private JLabel mdMarkLabel;
    private JLabel fnMarkLabel;
    private JLabel otMarkLabel;
    private JLabel markLabel;
    private JTextField mdExpectField;
    private JTextField fnExpectField;
    private JTextField otExpectField;
    private JTextField markExpectField;
    private JLabel mdExpectLabel;
    private JLabel fnExpectLabel;
    private JLabel otExpectLabel;
    private JLabel markExpectLabel;
    private JComboBox statusField;
    private JLabel statusLabel;
    private JComboBox resultBox;

    private TableRowSorter<TableModel> rowSorter;
    private Application selectedApplication;
    private Integer selectedRowIndex;

    public CheckingForm() {
        initComponents();
        clickListener = new ClickListener();
        this.controller = new CheckingController();
        this.list = controller.getList();
        loadRemarkingList();
        this.model = (DefaultTableModel) applicationTable.getModel();
        showDataTable();
    }

    private void loadRemarkingList(){
        this.remarkingBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        this.remarkingList = controller.getRemarkingList(new Date());
        this.remarkingBoxModel = (DefaultComboBoxModel) remarkingBox.getModel();
        addToRemarkingBox();
    }

    private void addToRemarkingBox() {
        for (Remarking remarking : remarkingList) {
            remarkingBoxModel.addElement(CustomDate.serialize(remarking.getOpening())+"-"+CustomDate.serialize(remarking.getClosing()));
        }
        remarkingBox.setModel(remarkingBoxModel);
    }

    private void showDataTable() {
        model.setColumnIdentifiers(new Object[]{
                "STT", "MSSV", "Họ tên", "Môn học", "PK Điểm GK", "PK Điểm CK", "PK Điểm khác", "PK Điểm tổng", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Kỳ", "Lý do", "Tình trạng"
        });
        int i = 1;
        for (Application transcript : list) {
            model.addRow(new Object[]{
                    i++,
                    transcript.getAttendance().getStudent().getCode(), transcript.getAttendance().getStudent().getName(),
                    transcript.getAttendance().getCalendar().getClasses().getName()+"-"+transcript.getAttendance().getCalendar().getSubject().getCode(),
                    transcript.getMiddleExpect(), transcript.getFinalExpect(), transcript.getOtherExpect(), transcript.getMarkExpect(),
                    transcript.getNewMiddle(), transcript.getNewFinal(), transcript.getNewOther(), transcript.getNewMark(),
                    CustomDate.serialize(transcript.getRemarking().getOpening())+"-"+CustomDate.serialize(transcript.getRemarking().getClosing()),
                    transcript.getReason(),
                    transcript.getStatus()
            });
        }

        rowSorter = new TableRowSorter<>(applicationTable.getModel());
        applicationTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        applicationTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        applicationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        applicationTable.getColumnModel().getColumn(1).setPreferredWidth(35);
        applicationTable.addMouseListener(getDataRow());

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        applicationTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);

    }

    private MouseAdapter getDataRow() {
        return (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && applicationTable.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) applicationTable.getModel();
                    selectedRowIndex = applicationTable.getSelectedRow();
                    selectedRowIndex = applicationTable.convertRowIndexToModel(selectedRowIndex);

                    selectedApplication = list.get(selectedRowIndex);

                    if(model.getValueAt(selectedRowIndex, 4) != null)
                        mdExpectField.setText(model.getValueAt(selectedRowIndex, 4).toString());
                    if(model.getValueAt(selectedRowIndex, 5) != null)
                        fnExpectField.setText(model.getValueAt(selectedRowIndex, 5).toString());
                    if(model.getValueAt(selectedRowIndex, 6) != null)
                        otExpectField.setText(model.getValueAt(selectedRowIndex, 6).toString());
                    if(model.getValueAt(selectedRowIndex, 7) != null)
                        markExpectField.setText(model.getValueAt(selectedRowIndex, 7).toString());
                    if(model.getValueAt(selectedRowIndex, 8) != null)
                        mdMarkField.setText(model.getValueAt(selectedRowIndex, 8).toString());
                    if(model.getValueAt(selectedRowIndex, 9) != null)
                        fnMarkField.setText(model.getValueAt(selectedRowIndex, 9).toString());
                    if(model.getValueAt(selectedRowIndex, 10) != null)
                        otMarkField.setText(model.getValueAt(selectedRowIndex, 10).toString());
                    if(model.getValueAt(selectedRowIndex, 11) != null)
                        markField.setText(model.getValueAt(selectedRowIndex, 11).toString());
                    if(model.getValueAt(selectedRowIndex, 14) != null)
                        statusField.setSelectedItem(model.getValueAt(selectedRowIndex, 14).toString());
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

    private void filter() {
        String text = remarkingBox.getSelectedItem().toString();
        if (text.equals("Tất cả")) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void filterResult() {
        String text = resultBox.getSelectedItem().toString();
        if (text.equals("Tất cả")) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void save() {
        String rs = "Lưu thất bại!";
        String status = statusField.getSelectedItem().toString();
        String middleMark = mdMarkField.getText().trim();
        String finalMark = fnMarkField.getText().trim();
        String otherMark = otMarkField.getText().trim();
        String mark = markField.getText().trim();

        Float newMiddle = null;
        Float newFinal = null;
        Float newOther = null;
        Float newMark = null;

        if (middleMark != null && !middleMark.isEmpty())
            newMiddle = Float.parseFloat(middleMark);
        if (finalMark != null && !finalMark.isEmpty())
            newFinal = Float.parseFloat(finalMark);
        if (otherMark != null && !otherMark.isEmpty())
            newOther = Float.parseFloat(otherMark);
        if (mark != null && !mark.isEmpty())
            newMark = Float.parseFloat(mark);

        boolean checkId = true;
        boolean response = false;

        if (selectedApplication != null) {
            selectedApplication.setNewMiddle(newMiddle);
            selectedApplication.setNewFinal(newFinal);
            selectedApplication.setNewOther(newOther);
            selectedApplication.setNewMark(newMark);
            selectedApplication.setStatus(status);

            response = controller.update(selectedApplication);
        }

        if (checkId == true) {
            if (response == true) {
                if (selectedApplication != null) {
                    model.setValueAt(middleMark, selectedRowIndex, 8);
                    model.setValueAt(finalMark, selectedRowIndex, 9);
                    model.setValueAt(otherMark, selectedRowIndex, 10);
                    model.setValueAt(mark, selectedRowIndex, 11);
                    model.setValueAt(status, selectedRowIndex, 14);
                }

                model.fireTableDataChanged();
                applicationTable.repaint();
                rs = "Lưu thành công!";
            }
        } else {
            rs = "Đơn phúc khảo đã tồn tại!";
        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            List<Application> newList = new ArrayList<>();
            if (selectedApplication != null) {
                for (Application s : list) {
                    if (!(selectedApplication.getRemarking().equals(s.getRemarking()) && selectedApplication.getAttendance().equals(s.getAttendance()))) {
                        newList.add(s);
                    }
                }
            }

            boolean response = controller.delete(selectedApplication);
            if (response == true) {
                model.removeRow(selectedRowIndex);
                model.fireTableDataChanged();
                applicationTable.repaint();
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
        selectedApplication = null;
        selectedRowIndex = null;
        mdMarkField.setText("");
        fnMarkField.setText("");
        otMarkField.setText("");
        markField.setText("");
        mdExpectField.setText("");
        fnExpectField.setText("");
        otExpectField.setText("");
        markExpectField.setText("");
        statusField.setSelectedItem("Chọn");
        applicationTable.getSelectionModel().clearSelection();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("DANH SÁCH PHÚC KHẢO");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        mdMarkLabel = new JLabel("Điểm GK: ");
        fnMarkLabel = new JLabel("Điểm CK: ");
        otMarkLabel = new JLabel("Điểm khác: ");
        markLabel = new JLabel("Điểm tổng: ");
        mdExpectLabel = new JLabel("Phúc khảo điểm GK: ");
        fnExpectLabel = new JLabel("Phúc khảo điểm CK: ");
        otExpectLabel = new JLabel("Phúc khảo điểm khác: ");
        markExpectLabel = new JLabel("Phúc khảo điểm tổng: ");
        statusLabel = new JLabel("Tình trạng: ");

        mdMarkField = new JTextField();
        fnMarkField = new JTextField();
        otMarkField = new JTextField();
        markField = new JTextField();
        mdExpectField = new JTextField();
        fnExpectField = new JTextField();
        otExpectField = new JTextField();
        markExpectField = new JTextField();

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());

        resultBox = new JComboBox(new Object[]{"Tất cả", "Đã cập nhật điểm", "Không cập nhật điểm", "Chưa xem"});
        resultBox.addActionListener(e->filterResult());

        statusField = new JComboBox(new Object[]{"Chưa xem", "Đã cập nhật điểm", "Không cập nhật điểm"});

        remarkingBox = new JComboBox();
        remarkingBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        remarkingBox.addActionListener(e -> filter());

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(search());

        jScrollPane1 = new JScrollPane();

        DefaultTableModel dm = new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "STT", "MSSV", "Họ tên", "Môn học", "PK Điểm GK", "PK Điểm CK", "PK Điểm khác", "PK Điểm tổng", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Kỳ", "Lý do", "Tình trạng"
                }
        ){
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        applicationTable = new JTable( dm ) {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        TableColumnModel cm = applicationTable.getColumnModel();
        ColumnGroup g_name = new ColumnGroup("Bảng điểm phúc khảo");
        g_name.add(cm.getColumn(2));
        g_name.add(cm.getColumn(3));
        g_name.add(cm.getColumn(4));
        g_name.add(cm.getColumn(5));
        ColumnGroup g_lang = new ColumnGroup("Bảng điểm sau phúc khảo");
        g_lang.add(cm.getColumn(6));
        g_lang.add(cm.getColumn(7));
        g_lang.add(cm.getColumn(8));
        g_lang.add(cm.getColumn(9));

        GroupableTableHeader header = (GroupableTableHeader)applicationTable.getTableHeader();
        header.addColumnGroup(g_name);
        header.addColumnGroup(g_lang);

        applicationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        jScrollPane1.setViewportView(applicationTable);

        deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> delete());

        removeButton = new JButton("Xóa tất cả");
        removeButton.addActionListener(e -> clearAll());

        GroupLayout layout = new GroupLayout(panel);
        settingLayout(layout);

        add(panel);
    }

    private void settingLayout(GroupLayout layout) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int xSize = ((int) toolkit.getScreenSize().getWidth());
        int width = (int) (Math.round(xSize * 0.83));

        panel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(statusLabel)
                                                .addComponent(statusField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                )
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(mdExpectLabel)
                                                .addComponent(mdExpectField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fnExpectLabel)
                                                .addComponent(fnExpectField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(otExpectLabel)
                                                .addComponent(otExpectField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(markExpectLabel)
                                                .addComponent(markExpectField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                )
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(mdMarkLabel)
                                                .addComponent(mdMarkField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fnMarkLabel)
                                                .addComponent(fnMarkField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(otMarkLabel)
                                                .addComponent(otMarkField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(markLabel)
                                                .addComponent(markField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                )

                        )
                        .addGap(18, 18, 18)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(saveButton)
                                .addGap(45, 45, 45)
                                .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(18, 18, 18)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(titleLabel)

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(remarkingBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(resultBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addGap(300, 300, 300)
                                                .addComponent(searchField)
                                        )
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, width, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(deleteButton)
                                                .addComponent(removeButton)
                                        )
                                )

                        )
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
                                                        .addComponent(statusLabel)
                                                        .addComponent(statusField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(mdExpectLabel)
                                                        .addComponent(mdExpectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(fnExpectLabel)
                                                        .addComponent(fnExpectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(otExpectLabel)
                                                        .addComponent(otExpectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(markExpectLabel)
                                                        .addComponent(markExpectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(25, 25, 25)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(mdMarkLabel)
                                                        .addComponent(mdMarkField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(fnMarkLabel)
                                                        .addComponent(fnMarkField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(otMarkLabel)
                                                        .addComponent(otMarkField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(markLabel)
                                                        .addComponent(markField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                        )


                                )
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(resetButton)
                                                .addComponent(saveButton))
                                )
                                .addGap(18, 18, 18)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(remarkingBox)
                                                        .addComponent(resultBox)
                                                        .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                                )
                                        )
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(deleteButton)
                                                        .addComponent(removeButton))
                                        )
                                        .addContainerGap(54, Short.MAX_VALUE))
                        )
        );

    }
}
