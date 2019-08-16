package com.sims.v2.view;

import com.sims.v2.controller.StudentController;
import com.sims.v2.model.Classes;
import com.sims.v2.model.Student;
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

public class StudentForm extends JPanel {
    private StudentController controller;
    private ClickListener clickListener;
    private List<Student> list;
    DefaultTableModel model;
    private List<Classes> classList;
    DefaultComboBoxModel classFieldModel;
    DefaultComboBoxModel classBoxModel;
    private JPanel panel;
    private JButton addButton;
    private JButton importButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel codeLabel;
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel idLabel;
    private JLabel classLabel;
    private JComboBox classBox;
    private JScrollPane jScrollPane1;
    private JTable studentTable;
    private JTextField nameField;
    private JTextField idField;
    private JTextField codeField;
    private JComboBox classField;
    private JTextField searchField;
    private ButtonGroup genderGroup;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private JButton deleteButton;
    private JButton removeButton;

    private TableRowSorter<TableModel> rowSorter;
    private Student selectedStudent;
    private Integer selectedRowIndex;

    public StudentForm() {
        initComponents();
        clickListener = new ClickListener();
        this.controller = new StudentController(this);
        this.list = controller.getList();
        loadClassList();
        this.model = (DefaultTableModel) studentTable.getModel();
        showDataTable();
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
                "STT", "MSSV", "Họ tên", "Giới tính", "CMND", "Lớp"
        });
        int i = 1;
        for (Student student : list) {
            model.addRow(new Object[]{
                    i++, student.getCode(), student.getName(), student.getGender(), student.getIdNumber(), student.getStudentClass().getName()
            });
        }

        rowSorter = new TableRowSorter<>(studentTable.getModel());
        studentTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        studentTable.addMouseListener(getDataRow());
    }

    private MouseAdapter getDataRow() {
        return (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && studentTable.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
                    selectedRowIndex = studentTable.getSelectedRow();
                    selectedRowIndex = studentTable.convertRowIndexToModel(selectedRowIndex);

                    selectedStudent = list.get(selectedRowIndex);

                    codeField.setText(model.getValueAt(selectedRowIndex, 1).toString());
                    nameField.setText(model.getValueAt(selectedRowIndex, 2).toString());
                    idField.setText(model.getValueAt(selectedRowIndex, 4).toString());
                    classField.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());

                    if (model.getValueAt(selectedRowIndex, 3).toString().equals("Nam")) {
                        maleButton.setSelected(true);
                    } else {
                        femaleButton.setSelected(true);
                    }

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
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String idNumber = idField.getText().trim();
        String classSelected = classField.getSelectedItem().toString();
        String gender = "Nam";
        if (femaleButton.isSelected()) {
            gender = "Nữ";
        }

        if (code.isEmpty()) {
            rs = "Vui lòng nhập vào MSSV!";
            codeField.requestFocus();
        } else if (name.isEmpty()) {
            rs = "Vui lòng nhập vào Họ tên!";
            nameField.requestFocus();
        } else {
            boolean response = false;
            boolean checkId = true;
            List<Student> newList = new ArrayList<>();
            Classes classes = new Classes();
            for (Classes c : classList) {
                if (c.getName().equals(classSelected)) {
                    classes = c;
                    break;
                }
            }
            if (selectedStudent != null) {
                selectedStudent.setCode(code);
                selectedStudent.setName(name);
                selectedStudent.setGender(gender);
                selectedStudent.setIdNumber(idNumber);
                selectedStudent.setStudentClass(classes);

                for (Student s : list) {
                    if (selectedStudent.getId().equals(s.getId())) {
                        newList.add(selectedStudent);
                    } else {
                        if (code.equals(s.getCode())) {
                            checkId = false;
                            break;
                        }
                        newList.add(s);
                    }
                }
                if (checkId == true){
                    response = controller.update(selectedStudent);
                }
            } else {
                for (Student s : list) {
                    if (code.equals(s.getCode())) {
                        checkId = false;
                        break;
                    }
                }
                if (checkId == true) {
                    Student newStudent = new Student();
                    newStudent.setCode(code);
                    newStudent.setName(name);
                    newStudent.setGender(gender);
                    newStudent.setIdNumber(idNumber);
                    newStudent.setStudentClass(classes);
                    response = controller.create(newStudent);
                    newList = controller.getList();
                }

            }

            if (checkId == true) {
                if (response == true) {
                    if (selectedStudent != null) {
                        model.setValueAt(code, selectedRowIndex, 1);
                        model.setValueAt(name, selectedRowIndex, 2);
                        model.setValueAt(gender, selectedRowIndex, 3);
                        model.setValueAt(idNumber, selectedRowIndex, 4);
                        model.setValueAt(classSelected, selectedRowIndex, 5);
                    } else {
                        Integer i = 0;
                        if (model.getRowCount() > 0) {
                            i = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString());
                        }
                        model.addRow(new Object[]{
                                ++i, code, name, gender, idNumber, classSelected
                        });
                    }

                    model.fireTableDataChanged();
                    studentTable.repaint();
                    rs = "Lưu thành công!";
                    list = newList;
                }
            } else {
                rs = "MSSV đã tồn tại!";
                codeField.requestFocus();
            }

        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            List<Student> newList = new ArrayList<>();
            if (selectedStudent != null) {
                for (Student s : list) {
                    if (!selectedStudent.getId().equals(s.getId())) {
                        newList.add(s);
                    }
                }

                boolean response = controller.delete(selectedStudent);
                if (response == true) {
                    model.removeRow(selectedRowIndex);
                    model.fireTableDataChanged();
                    studentTable.repaint();
                    rs = "Xóa thành công!";
                    list = newList;
                }
                clickListener.showMessage(rs);
                this.refresh();
            }
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
            List<Student> response = controller.importFile(path);
            if (response.size() > list.size()) {
                list = response;
                model.setRowCount(0);
                showDataTable();
                rs = "Cập nhật thành công!";
            }
            clickListener.showMessage(rs);
            this.loadClassList();
            this.refresh();
        }
    }

    private void refresh() {
        codeField.setText("");
        nameField.setText("");
        idField.setText("");
        selectedStudent = null;
        selectedRowIndex = null;
        studentTable.getSelectionModel().clearSelection();
        codeField.requestFocus();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("THÔNG TIN SINH VIÊN");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton("+ Thêm mới");
        addButton.addActionListener(e -> refresh());

        importButton = new JButton("Nhập từ file ...");
        importButton.addActionListener(e -> importFile());

        codeLabel = new JLabel("MSSV: ");
        nameLabel = new JLabel("Họ tên: ");
        genderLabel = new JLabel("Giới tính: ");
        idLabel = new JLabel("CMND: ");
        classLabel = new JLabel("Lớp: ");

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());

        codeField = new JTextField();
        nameField = new JTextField();
        genderGroup = new ButtonGroup();
        maleButton = new JRadioButton("Nam");
        maleButton.setSelected(true);
        femaleButton = new JRadioButton("Nữ");
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        idField = new JTextField();

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
        studentTable = new JTable();
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        studentTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "STT", "MSSV", "Họ tên", "Giới tính", "CMND", "Lớp"
                }
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        });

        jScrollPane1.setViewportView(studentTable);

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
                                                        .addComponent(codeLabel)
                                                        .addComponent(nameLabel)
                                                        .addComponent(genderLabel)
                                                        .addComponent(idLabel)
                                                        .addComponent(classLabel))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(codeField)
                                                        .addComponent(nameField)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(maleButton)
                                                                .addComponent(femaleButton)
                                                        )
                                                        .addComponent(idField)
                                                        .addComponent(classField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
                                                .addGap(27, 27, 27))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
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
                                                        .addComponent(codeLabel)
                                                        .addComponent(codeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(nameLabel)
                                                        .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(genderLabel)
                                                        .addComponent(maleButton)
                                                        .addComponent(femaleButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(idLabel)
                                                        .addComponent(idField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(classLabel)
                                                        .addComponent(classField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
