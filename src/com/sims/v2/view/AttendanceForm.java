package com.sims.v2.view;

import com.sims.v2.controller.AttendanceController;
import com.sims.v2.model.Attendance;
import com.sims.v2.model.Calendar;
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

public class AttendanceForm extends JPanel {
    private AttendanceController controller;
    private ClickListener clickListener;
    private List<Attendance> list;
    DefaultTableModel model;
    private List<Calendar> calendarList;
    DefaultComboBoxModel calendarFieldModel;
    DefaultComboBoxModel calendarBoxModel;
    private List<Student> studentList;
    DefaultComboBoxModel studentFieldModel;
    private List<Classes> classList;
    DefaultComboBoxModel classBoxModel;
    private JPanel panel;
    private JButton addButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel studentLabel;
    private JLabel calendarLabel;
    private JComboBox calendarBox;
    private JComboBox classBox;
    private JScrollPane jScrollPane1;
    private JTable attendanceTable;
    private JComboBox studentField;
    private JComboBox calendarField;
    private JTextField searchField;
    private JButton deleteButton;
    private JButton removeButton;

    private TableRowSorter<TableModel> rowSorter;
    private Attendance selectedAttendance;
    private Integer selectedRowIndex;

    public AttendanceForm() {
        initComponents();
        clickListener = new ClickListener();
        this.controller = new AttendanceController(this);
        this.list = controller.getList();
        loadClassList();
        loadCalendarList();
        loadStudentList();
        this.model = (DefaultTableModel) attendanceTable.getModel();
        showDataTable();
    }

    private void loadStudentList(){
        this.studentField.setModel(new DefaultComboBoxModel());
        this.studentList = controller.getStudentList();
        this.studentFieldModel = (DefaultComboBoxModel) studentField.getModel();
        addToStudentBox();
    }

    private void addToStudentBox() {
        for (Student student : studentList) {
            studentFieldModel.addElement(student.getName());
        }
        studentField.setModel(studentFieldModel);
    }

    private void loadCalendarList(){
        this.calendarField.setModel(new DefaultComboBoxModel());
        this.calendarBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        this.calendarList = controller.getCalendarList();
        this.calendarBoxModel = (DefaultComboBoxModel) calendarBox.getModel();
        this.calendarFieldModel = (DefaultComboBoxModel) calendarField.getModel();
        addToCalendarBox();
    }

    private void addToCalendarBox() {
        for (Calendar calendar : calendarList) {
            calendarBoxModel.addElement(calendar.getClasses().getName()+"-"+calendar.getSubject().getCode());
            calendarFieldModel.addElement(calendar.getClasses().getName()+"-"+calendar.getSubject().getCode());
        }
        calendarField.setModel(calendarFieldModel);
        calendarBox.setModel(calendarBoxModel);
    }

    private void loadClassList(){
        this.classBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        this.classList = controller.getClassList();
        this.classBoxModel = (DefaultComboBoxModel) classBox.getModel();
        addToClassBox();
    }

    private void addToClassBox() {
        for (Classes classes : classList) {
            classBoxModel.addElement(classes.getName());
        }
        classBox.setModel(classBoxModel);
    }

    private void showDataTable() {
        model.setColumnIdentifiers(new Object[]{
                "STT", "MSSV", "Họ tên", "Giới tính", "CMND", "Lớp"
        });
        int i = 1;
        for (Attendance attendance : list) {
            model.addRow(new Object[]{
                    i++, attendance.getStudent().getCode(), attendance.getStudent().getName(), attendance.getStudent().getGender(), attendance.getStudent().getIdNumber(), attendance.getCalendar().getClasses().getName()+"-"+attendance.getCalendar().getSubject().getCode(),
            });
        }

        rowSorter = new TableRowSorter<>(attendanceTable.getModel());
        attendanceTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        attendanceTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        attendanceTable.addMouseListener(getDataRow());
    }

    private MouseAdapter getDataRow() {
        return (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && attendanceTable.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) attendanceTable.getModel();
                    selectedRowIndex = attendanceTable.getSelectedRow();
                    selectedRowIndex = attendanceTable.convertRowIndexToModel(selectedRowIndex);

                    selectedAttendance = list.get(selectedRowIndex);

                    studentField.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
                    calendarField.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());

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
        String text = calendarBox.getSelectedItem().toString();
        if (text.equals("Tất cả")) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
    private void filterClass() {
        String text = classBox.getSelectedItem().toString();
        if (text.equals("Tất cả")) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void save() {
        String rs = "Lưu thất bại!";
        String calendarSelected = calendarField.getSelectedItem().toString();
        String studentSelected = studentField.getSelectedItem().toString();
        String codeSelected = "";
        String nameSelected = "";
        String genderSelected = "";
        String idSelected = "";
        boolean response = false;
        boolean checkId = true;

        List<Attendance> newList = new ArrayList<>();
        Calendar calendar = new Calendar();
        for (Calendar c : calendarList) {
            String cl = c.getClasses().getName()+"-"+c.getSubject().getCode();
            if (cl.equals(calendarSelected)) {
                calendar = c;
                break;
            }
        }
        Student student = new Student();
        for (Student s : studentList) {
            if (s.getName().equals(studentSelected)) {
                student = s;
                codeSelected = s.getCode();
                nameSelected = s.getName();
                genderSelected = s.getGender();
                idSelected = s.getIdNumber();
                break;
            }
        }
        if (selectedAttendance != null) {
            selectedAttendance.setCalendar(calendar);
            selectedAttendance.setStudent(student);

            for (Attendance s : list) {
                if (selectedAttendance.getStudent().equals(s.getStudent()) && selectedAttendance.getCalendar().equals(s.getCalendar())) {
                    newList.add(selectedAttendance);
                } else {
                    if (studentSelected.equals(s.getStudent().getName()) && calendarSelected.equals(s.getCalendar().getClasses().getName()+"-"+s.getCalendar().getSubject().getCode())) {
                        checkId = false;
                        break;
                    }
                    newList.add(s);
                }
                if (checkId == true){
                    response = controller.update(selectedAttendance);
                }
            }
        } else {
            for (Attendance s : list) {
                if (studentSelected.equals(s.getStudent().getName()) && calendarSelected.equals(s.getCalendar().getClasses().getName()+"-"+s.getCalendar().getSubject().getCode())) {
                    checkId = false;
                    break;
                }
            }
            if (checkId == true) {
                Attendance newAttendance = new Attendance();
                newAttendance.setStudent(student);
                newAttendance.setCalendar(calendar);
                response = controller.create(newAttendance);
                newList = controller.getList();
            }
        }

        if (checkId == true) {
            if (response == true) {
                if (selectedAttendance != null) {
                    model.setValueAt(codeSelected, selectedRowIndex, 1);
                    model.setValueAt(nameSelected, selectedRowIndex, 2);
                    model.setValueAt(genderSelected, selectedRowIndex, 3);
                    model.setValueAt(idSelected, selectedRowIndex, 4);
                    model.setValueAt(calendarSelected, selectedRowIndex, 5);
                } else {
                    Integer i = 0;
                    if (model.getRowCount() > 0) {
                        i = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString());
                    }
                    model.addRow(new Object[]{
                            ++i, codeSelected, nameSelected, genderSelected, idSelected, calendarSelected
                    });
                }

                model.fireTableDataChanged();
                attendanceTable.repaint();
                rs = "Lưu thành công!";
                list = newList;
            }
        }
        else {
            rs = "Sinh viên đã tồn tại!";
        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            List<Attendance> newList = new ArrayList<>();
            if (selectedAttendance != null) {
                for (Attendance s : list) {
                    if (!(selectedAttendance.getStudent().equals(s.getStudent()) && selectedAttendance.getCalendar().equals(s.getCalendar()))) {
                        newList.add(s);
                    }
                }
            }

            boolean response = controller.delete(selectedAttendance);
            if (response == true) {
                model.removeRow(selectedRowIndex);
                model.fireTableDataChanged();
                attendanceTable.repaint();
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
        selectedAttendance = null;
        selectedRowIndex = null;
        attendanceTable.getSelectionModel().clearSelection();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("DANH SÁCH LỚP");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton("+ Thêm mới");
        addButton.addActionListener(e -> refresh());


        studentLabel = new JLabel("Sinh viên: ");
        calendarLabel = new JLabel("Lớp: ");

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());


        studentField = new JComboBox(new DefaultComboBoxModel(

        ));

        calendarField = new JComboBox(new DefaultComboBoxModel(

        ));

        classBox = new JComboBox();
        classBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        classBox.addActionListener(e -> filterClass());

        calendarBox = new JComboBox();
        calendarBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        calendarBox.addActionListener(e -> filter());

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(search());

        jScrollPane1 = new JScrollPane();
        attendanceTable = new JTable();
        attendanceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        attendanceTable.setModel(new DefaultTableModel(
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

        jScrollPane1.setViewportView(attendanceTable);

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
                                                        .addComponent(calendarLabel)
                                                        .addComponent(studentLabel))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(calendarField)
                                                        .addComponent(studentField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
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
                                                .addComponent(calendarBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addGap(200, 200, 200)
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
                                                        .addComponent(calendarLabel)
                                                        .addComponent(calendarField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(studentLabel)
                                                        .addComponent(studentField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(25, 25, 25)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(resetButton)
                                                        .addComponent(saveButton))
                                                .addGap(73, 73, 73))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(classBox)
                                                                .addComponent(calendarBox)
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
