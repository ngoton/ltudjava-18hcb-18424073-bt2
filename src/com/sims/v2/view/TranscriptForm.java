package com.sims.v2.view;

import com.sims.v2.controller.TranscriptController;
import com.sims.v2.model.Attendance;
import com.sims.v2.model.Calendar;
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

public class TranscriptForm extends JPanel {
    private static Float basicMark = 5.0f;
    private TranscriptController controller;
    private ClickListener clickListener;
    private List<Attendance> list;
    DefaultTableModel model;
    private List<Calendar> calendarList;
    DefaultComboBoxModel calendarFieldModel;
    DefaultComboBoxModel calendarBoxModel;
    private List<Student> studentList;
    DefaultComboBoxModel studentFieldModel;
    private JPanel panel;
    private JButton addButton;
    private JButton importButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel studentLabel;
    private JLabel calendarLabel;
    private JComboBox calendarBox;
    private JScrollPane jScrollPane1;
    private JTable transcriptTable;
    private JComboBox studentField;
    private JComboBox calendarField;
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
    private JComboBox resultBox;
    private JLabel passName;
    private JLabel passCount;
    private JLabel failName;
    private JLabel failCount;

    private TableRowSorter<TableModel> rowSorter;
    private Attendance selectedTranscript;
    private Integer selectedRowIndex;

    public TranscriptForm() {
        initComponents();
        clickListener = new ClickListener();
        this.controller = new TranscriptController(this);
        this.list = controller.getList();
        loadCalendarList();
        loadStudentList();
        this.model = (DefaultTableModel) transcriptTable.getModel();
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
            studentFieldModel.addElement(student.getCode());
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

    private void showDataTable() {
        model.setColumnIdentifiers(new Object[]{
                "STT", "MSSV", "Họ tên", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Lớp", "KQ"
        });
        int i = 1;
        for (Attendance transcript : list) {
            model.addRow(new Object[]{
                    i++, transcript.getStudent().getCode(), transcript.getStudent().getName(), transcript.getMiddleMark(), transcript.getFinalMark(), transcript.getOtherMark(), transcript.getMark(), transcript.getCalendar().getClasses().getName()+"-"+transcript.getCalendar().getSubject().getCode(), (transcript.getMark()>=basicMark?"Đậu":"Rớt")
            });
        }

        rowSorter = new TableRowSorter<>(transcriptTable.getModel());
        transcriptTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        transcriptTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        transcriptTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        transcriptTable.getColumnModel().getColumn(8).setPreferredWidth(20);
        transcriptTable.addMouseListener(getDataRow());

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        transcriptTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        transcriptTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        transcriptTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        transcriptTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);

        this.statistic();

    }

    private MouseAdapter getDataRow() {
        return (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && transcriptTable.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) transcriptTable.getModel();
                    selectedRowIndex = transcriptTable.getSelectedRow();
                    selectedRowIndex = transcriptTable.convertRowIndexToModel(selectedRowIndex);

                    selectedTranscript = list.get(selectedRowIndex);

                    studentField.setSelectedItem(model.getValueAt(selectedRowIndex, 1).toString());
                    calendarField.setSelectedItem(model.getValueAt(selectedRowIndex, 7).toString());
                    if(model.getValueAt(selectedRowIndex, 3) != null)
                        mdMarkField.setText(model.getValueAt(selectedRowIndex, 3).toString());
                    if(model.getValueAt(selectedRowIndex, 4) != null)
                        fnMarkField.setText(model.getValueAt(selectedRowIndex, 4).toString());
                    if(model.getValueAt(selectedRowIndex, 5) != null)
                        otMarkField.setText(model.getValueAt(selectedRowIndex, 5).toString());
                    if(model.getValueAt(selectedRowIndex, 6) != null)
                        markField.setText(model.getValueAt(selectedRowIndex, 6).toString());
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
        String calendarSelected = calendarField.getSelectedItem().toString();
        String studentSelected = studentField.getSelectedItem().toString();
        String nameSelected = "";
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
            if (s.getCode().equals(studentSelected)) {
                student = s;
                nameSelected = s.getName();
                break;
            }
        }
        if (selectedTranscript != null) {
            selectedTranscript.setCalendar(calendar);
            selectedTranscript.setStudent(student);
            selectedTranscript.setMiddleMark(newMiddle);
            selectedTranscript.setFinalMark(newFinal);
            selectedTranscript.setOtherMark(newOther);
            selectedTranscript.setMark(newMark);

            for (Attendance s : list) {
                if (selectedTranscript.getStudent().equals(s.getStudent()) && selectedTranscript.getCalendar().equals(s.getCalendar())) {
                    newList.add(selectedTranscript);
                } else {
                    if (studentSelected.equals(s.getStudent().getCode()) && calendarSelected.equals(s.getCalendar().getClasses().getName()+"-"+s.getCalendar().getSubject().getCode())) {
                        checkId = false;
                        break;
                    }
                    newList.add(s);
                }
                if (checkId == true){
                    response = controller.update(selectedTranscript);
                }
            }
        } else {
            boolean checkExists = false;
            for (Attendance s : list) {
                if (studentSelected.equals(s.getStudent().getCode()) && calendarSelected.equals(s.getCalendar().getClasses().getName()+"-"+s.getCalendar().getSubject().getCode())) {
                    if (s.getMiddleMark() == null && s.getFinalMark() == null && s.getOtherMark() == null && s.getMark() == null) {
                        s.setMiddleMark(newMiddle);
                        s.setFinalMark(newFinal);
                        s.setOtherMark(newOther);
                        s.setMark(newMark);
                        response = controller.update(s);
                        checkId = true;
                        checkExists = true;
                    }
                    else {
                        checkId = false;
                    }

                    break;
                }
            }
            if (checkId == true && checkExists == false) {
                Attendance newTranscript = new Attendance();
                newTranscript.setStudent(student);
                newTranscript.setCalendar(calendar);
                newTranscript.setMiddleMark(newMiddle);
                newTranscript.setFinalMark(newFinal);
                newTranscript.setOtherMark(newOther);
                newTranscript.setMark(newMark);
                response = controller.create(newTranscript);
                newList = controller.getList();
            }
        }

        if (checkId == true) {
            if (response == true) {
                if (selectedTranscript != null) {
                    model.setValueAt(studentSelected, selectedRowIndex, 1);
                    model.setValueAt(nameSelected, selectedRowIndex, 2);
                    model.setValueAt(middleMark, selectedRowIndex, 3);
                    model.setValueAt(finalMark, selectedRowIndex, 4);
                    model.setValueAt(otherMark, selectedRowIndex, 5);
                    model.setValueAt(mark, selectedRowIndex, 6);
                    model.setValueAt(calendarSelected, selectedRowIndex, 7);
                    model.setValueAt((Float.parseFloat(mark)>=basicMark?"Đậu":"Rớt"), selectedRowIndex, 8);
                } else {
                    Integer i = 0;
                    if (model.getRowCount() > 0) {
                        i = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString());
                    }
                    model.addRow(new Object[]{
                            ++i, studentSelected, nameSelected, middleMark, finalMark, otherMark, mark, calendarSelected, (Float.parseFloat(mark)>=basicMark?"Đậu":"Rớt")
                    });
                }

                model.fireTableDataChanged();
                transcriptTable.repaint();
                rs = "Lưu thành công!";
                list = newList;
            }
        }
        else {
            rs = "Điểm sinh viên đã tồn tại!";
        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            List<Attendance> newList = new ArrayList<>();
            if (selectedTranscript != null) {
                for (Attendance s : list) {
                    if (!(selectedTranscript.getStudent().equals(s.getStudent()) && selectedTranscript.getCalendar().equals(s.getCalendar()))) {
                        newList.add(s);
                    }
                }
                selectedTranscript.setMiddleMark(null);
                selectedTranscript.setFinalMark(null);
                selectedTranscript.setOtherMark(null);
                selectedTranscript.setMark(null);
            }

            boolean response = controller.update(selectedTranscript);
            if (response == true) {
                model.removeRow(selectedRowIndex);
                model.fireTableDataChanged();
                transcriptTable.repaint();
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
            List<Attendance> newList = new ArrayList<>();
            for (Attendance a : list){
                a.setMiddleMark(null);
                a.setFinalMark(null);
                a.setOtherMark(null);
                a.setMark(null);
                newList.add(a);
            }
            boolean response = controller.deleteAll(newList);
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
            List<Attendance> response = controller.importFile(path);
            if (response.size() > list.size()) {
                list = response;
                model.setRowCount(0);
                showDataTable();
                rs = "Cập nhật thành công!";
            }
            clickListener.showMessage(rs);
            this.loadStudentList();
            this.loadCalendarList();
            this.refresh();
        }
    }

    private void refresh() {
        selectedTranscript = null;
        selectedRowIndex = null;
        mdMarkField.setText("");
        fnMarkField.setText("");
        otMarkField.setText("");
        markField.setText("");
        transcriptTable.getSelectionModel().clearSelection();
        this.statistic();
    }

    private void statistic(){
        int pass = 0;
        int fail = 0;
        for (Attendance transcript : list) {
            if (transcript.getMark()>=basicMark){
                pass++;
            }
            else {
                fail++;
            }
        }
        if (pass > 0 || fail > 0){
            float passPer = (float)pass/(pass+fail)*100;
            float failPer = (float)fail/(pass+fail)*100;
            passCount.setText(pass+" ("+Math.round(passPer)+"%)");
            failCount.setText(fail+" ("+Math.round(failPer)+"%)");
        }
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("BẢNG ĐIỂM");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton("+ Thêm mới");
        addButton.addActionListener(e -> refresh());

        importButton = new JButton("Nhập từ file ...");
        importButton.addActionListener(e -> importFile());

        studentLabel = new JLabel("Sinh viên: ");
        calendarLabel = new JLabel("Lớp: ");
        mdMarkLabel = new JLabel("Điểm GK: ");
        fnMarkLabel = new JLabel("Điểm CK: ");
        otMarkLabel = new JLabel("Điểm khác: ");
        markLabel = new JLabel("Điểm tổng: ");

        mdMarkField = new JTextField();
        fnMarkField = new JTextField();
        otMarkField = new JTextField();
        markField = new JTextField();

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());

        resultBox = new JComboBox(new Object[]{"Tất cả", "Đậu", "Rớt"});
        resultBox.addActionListener(e->filterResult());

        studentField = new JComboBox(new DefaultComboBoxModel(

        ));

        calendarField = new JComboBox(new DefaultComboBoxModel(

        ));


        calendarBox = new JComboBox();
        calendarBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        calendarBox.addActionListener(e -> filter());

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(search());

        jScrollPane1 = new JScrollPane();
        transcriptTable = new JTable();
        transcriptTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        transcriptTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "STT", "MSSV", "Họ tên", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Lớp", "KQ"
                }
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        });

        jScrollPane1.setViewportView(transcriptTable);

        deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> delete());

        removeButton = new JButton("Xóa tất cả");
        removeButton.addActionListener(e -> clearAll());

        passName = new JLabel("Đậu: ");
        passCount = new JLabel();
        passCount.setForeground(new Color(52326));
        passCount.setFont(new Font("Arial", 1, 16));
        failName = new JLabel("Rớt: ");
        failCount = new JLabel();
        failCount.setForeground(Color.RED);
        failCount.setFont(new Font("Arial", 1, 16));

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
                                                        .addComponent(calendarLabel)
                                                        .addComponent(studentLabel)
                                                        .addComponent(mdMarkLabel)
                                                        .addComponent(fnMarkLabel)
                                                        .addComponent(otMarkLabel)
                                                        .addComponent(markLabel))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(calendarField)
                                                        .addComponent(studentField)
                                                        .addComponent(mdMarkField)
                                                        .addComponent(fnMarkField)
                                                        .addComponent(otMarkField)
                                                        .addComponent(markField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
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
                                                .addComponent(calendarBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(resultBox, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                .addGap(300, 300, 300)
                                                .addComponent(searchField)
                                        )
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(deleteButton)
                                                .addComponent(removeButton)
                                                .addGap(300, 300, 300)
                                                .addComponent(passName)
                                                .addComponent(passCount)
                                                .addGap(50, 50, 50)
                                                .addComponent(failName)
                                                .addComponent(failCount)
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
                                                        .addComponent(calendarLabel)
                                                        .addComponent(calendarField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(studentLabel)
                                                        .addComponent(studentField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
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
                                                .addGap(25, 25, 25)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(resetButton)
                                                        .addComponent(saveButton))
                                                .addGap(73, 73, 73))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(calendarBox)
                                                                .addComponent(resultBox)
                                                                .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                                        )
                                                )
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(deleteButton)
                                                                .addComponent(removeButton)
                                                                .addComponent(passName)
                                                                .addComponent(passCount)
                                                                .addGap(50, 50, 50)
                                                                .addComponent(failName)
                                                                .addComponent(failCount))
                                                )
                                                .addContainerGap(54, Short.MAX_VALUE))))
        );

    }
}
