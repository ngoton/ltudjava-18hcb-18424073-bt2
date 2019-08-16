package com.sims.v2.view;

import com.sims.v2.controller.CalendarController;
import com.sims.v2.model.Calendar;
import com.sims.v2.model.Classes;
import com.sims.v2.model.Subject;
import com.sims.v2.util.ClickListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CalendarForm extends JPanel {
    private CalendarController controller;
    private ClickListener clickListener;
    private List<Calendar> list;
    DefaultTableModel model;
    private List<Classes> classList;
    DefaultComboBoxModel classFieldModel;
    DefaultComboBoxModel classBoxModel;
    private List<Subject> subjectList;
    DefaultComboBoxModel subjectFieldModel;
    private JPanel panel;
    private JButton addButton;
    private JButton importButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel subjectLabel;
    private JLabel roomLabel;
    private JLabel classLabel;
    private JComboBox classBox;
    private JScrollPane jScrollPane1;
    private JTable calendarTable;
    private JComboBox subjectField;
    private JTextField roomField;
    private JComboBox classField;
    private JTextField searchField;
    private JButton deleteButton;
    private JButton removeButton;

    private TableRowSorter<TableModel> rowSorter;
    private Calendar selectedCalendar;
    private Integer selectedRowIndex;

    public CalendarForm() {
        initComponents();
        clickListener = new ClickListener();
        this.controller = new CalendarController(this);
        this.list = controller.getList();
        loadSubjectList();
        loadClassList();
        this.model = (DefaultTableModel) calendarTable.getModel();
        showDataTable();
    }

    private void loadSubjectList(){
        this.subjectField.setModel(new DefaultComboBoxModel());
        this.subjectList = controller.getSubjectList();
        this.subjectFieldModel = (DefaultComboBoxModel) subjectField.getModel();
        addToSubjectBox();
    }

    private void addToSubjectBox() {
        for (Subject subject : subjectList) {
            subjectFieldModel.addElement(subject.getName());
        }
        subjectField.setModel(subjectFieldModel);
    }

    private void loadClassList(){
        this.classField.setModel(new DefaultComboBoxModel());
        this.classBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        this.classList = controller.getClassList();
        this.classBoxModel = (DefaultComboBoxModel) classBox.getModel();
        this.classFieldModel = (DefaultComboBoxModel) classField.getModel();
        addToClassBox();
    }

    private void addToClassBox() {
        for (Classes classes : classList) {
            classBoxModel.addElement(classes.getName());
            classFieldModel.addElement(classes.getName());
        }
        classField.setModel(classFieldModel);
        classBox.setModel(classBoxModel);
    }

    private void showDataTable() {
        model.setColumnIdentifiers(new Object[]{
                "STT", "Mã môn", "Tên môn", "Phòng học", "Lớp"
        });
        int i = 1;
        for (Calendar calendar : list) {
            model.addRow(new Object[]{
                    i++, calendar.getSubject().getCode(), calendar.getSubject().getName(), calendar.getRoom(), calendar.getClasses().getName()
            });
        }

        rowSorter = new TableRowSorter<>(calendarTable.getModel());
        calendarTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        calendarTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        calendarTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        calendarTable.addMouseListener(getDataRow());
    }

    private MouseAdapter getDataRow() {
        return (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && calendarTable.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) calendarTable.getModel();
                    selectedRowIndex = calendarTable.getSelectedRow();
                    selectedRowIndex = calendarTable.convertRowIndexToModel(selectedRowIndex);

                    selectedCalendar = list.get(selectedRowIndex);

                    subjectField.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
                    roomField.setText(model.getValueAt(selectedRowIndex, 3).toString());
                    classField.setSelectedItem(model.getValueAt(selectedRowIndex, 4).toString());

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
        String text = classBox.getSelectedItem().toString();
        if (text.equals("Tất cả")) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void save() {
        String rs = "Lưu thất bại!";
        String room = roomField.getText().trim();
        String classSelected = classField.getSelectedItem().toString();
        String subjectSelected = subjectField.getSelectedItem().toString();
        String codeSelected = "";

        if (room.isEmpty()) {
            rs = "Vui lòng nhập vào Phòng học!";
            roomField.requestFocus();
        } else {
            boolean response = false;
            boolean checkId = true;
            List<Calendar> newList = new ArrayList<>();
            Classes classes = new Classes();
            for (Classes c : classList) {
                if (c.getName().equals(classSelected)) {
                    classes = c;
                    break;
                }
            }
            Subject subject = new Subject();
            for (Subject sj : subjectList) {
                if (sj.getName().equals(subjectSelected)) {
                    subject = sj;
                    codeSelected = sj.getCode();
                    break;
                }
            }
            if (selectedCalendar != null) {
                selectedCalendar.setRoom(room);
                selectedCalendar.setClasses(classes);
                selectedCalendar.setSubject(subject);

                for (Calendar s : list) {
                    if (selectedCalendar.getClasses().equals(s.getClasses()) && selectedCalendar.getSubject().equals(s.getSubject())) {
                        newList.add(selectedCalendar);
                    } else {
                        if (classSelected.equals(s.getClasses().getName()) && subjectSelected.equals(s.getSubject().getName())) {
                            checkId = false;
                            break;
                        }
                        newList.add(s);
                    }
                    if (checkId == true){
                        response = controller.update(selectedCalendar);
                    }
                }
            } else {
                for (Calendar s : list) {
                    if (classSelected.equals(s.getClasses().getName()) && subjectSelected.equals(s.getSubject().getName())) {
                        checkId = false;
                        break;
                    }
                }
                if (checkId == true) {
                    Calendar newCalendar = new Calendar();
                    newCalendar.setRoom(room);
                    newCalendar.setSubject(subject);
                    newCalendar.setClasses(classes);
                    response = controller.create(newCalendar);
                    newList = controller.getList();
                }
            }

            if (checkId == true) {
                if (response == true) {
                    if (selectedCalendar != null) {
                        model.setValueAt(codeSelected, selectedRowIndex, 1);
                        model.setValueAt(subjectSelected, selectedRowIndex, 2);
                        model.setValueAt(room, selectedRowIndex, 3);
                        model.setValueAt(classSelected, selectedRowIndex, 4);
                    } else {
                        Integer i = 0;
                        if (model.getRowCount() > 0) {
                            i = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString());
                        }
                        model.addRow(new Object[]{
                                ++i, codeSelected, subjectSelected, room, classSelected
                        });
                    }

                    model.fireTableDataChanged();
                    calendarTable.repaint();
                    rs = "Lưu thành công!";
                    list = newList;
                }
            }
            else {
                rs = "Thời khóa biểu đã tồn tại!";
            }

        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            List<Calendar> newList = new ArrayList<>();
            if (selectedCalendar != null) {
                for (Calendar s : list) {
                    if (!(selectedCalendar.getClasses().equals(s.getClasses()) && selectedCalendar.getSubject().equals(s.getSubject()))) {
                        newList.add(s);
                    }
                }
            }

            boolean response = controller.delete(selectedCalendar);
            if (response == true) {
                model.removeRow(selectedRowIndex);
                model.fireTableDataChanged();
                calendarTable.repaint();
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

    private void importFile() {
        String path = clickListener.importClick();
        if (path != null){
            String rs = "Có lỗi xảy ra!";
            List<Calendar> response = controller.importFile(path);
            if (response.size() > list.size()) {
                list = response;
                model.setRowCount(0);
                showDataTable();
                rs = "Cập nhật thành công!";
            }
            clickListener.showMessage(rs);
            this.loadClassList();
            this.loadSubjectList();
            this.refresh();
        }
    }

    private void refresh() {
        roomField.setText("");
        selectedCalendar = null;
        selectedRowIndex = null;
        calendarTable.getSelectionModel().clearSelection();
        roomField.requestFocus();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("THỜI KHÓA BIỂU");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton("+ Thêm mới");
        addButton.addActionListener(e -> refresh());

        importButton = new JButton("Nhập từ file ...");
        importButton.addActionListener(e -> importFile());

        subjectLabel = new JLabel("Môn học: ");
        roomLabel = new JLabel("Phòng: ");
        classLabel = new JLabel("Lớp: ");

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());

        roomField = new JTextField();

        subjectField = new JComboBox(new DefaultComboBoxModel(

        ));

        classField = new JComboBox(new DefaultComboBoxModel(

        ));

        classBox = new JComboBox();
        classBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        classBox.addActionListener(e -> filter());

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(search());

        jScrollPane1 = new JScrollPane();
        calendarTable = new JTable();
        calendarTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        calendarTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "STT", "Mã môn", "Tên môn", "Phòng học", "Lớp"
                }
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        });

        jScrollPane1.setViewportView(calendarTable);

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
                                                .addComponent(importButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(classLabel)
                                                        .addComponent(subjectLabel)
                                                        .addComponent(roomLabel))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(classField)
                                                        .addComponent(subjectField)
                                                        .addComponent(roomField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
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
                                                .addComponent(classBox, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                .addGap(300, 300, 300)
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
                                                        .addComponent(addButton)
                                                        .addComponent(importButton))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(classLabel)
                                                        .addComponent(classField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(subjectLabel)
                                                        .addComponent(subjectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(roomLabel)
                                                        .addComponent(roomField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(25, 25, 25)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(resetButton)
                                                        .addComponent(saveButton))
                                                .addGap(73, 73, 73))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(classBox)
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
