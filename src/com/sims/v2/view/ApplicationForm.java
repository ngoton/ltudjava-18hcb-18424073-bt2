package com.sims.v2.view;

import com.sims.v2.controller.ApplicationController;
import com.sims.v2.model.*;
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

public class ApplicationForm extends JPanel {
    private User user;
    private static Float basicMark = 5.0f;
    private ApplicationController controller;
    private ClickListener clickListener;
    private List<Application> list;
    DefaultTableModel model;
    private List<Remarking> remarkingList;
    DefaultComboBoxModel remarkingFieldModel;
    DefaultComboBoxModel remarkingBoxModel;
    private List<Attendance> attendanceList;
    DefaultComboBoxModel attendanceFieldModel;
    private JPanel panel;
    private JButton addButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel remarkingLabel;
    private JLabel attendanceLabel;
    private JComboBox remarkingBox;
    private JScrollPane jScrollPane1;
    private JTable applicationTable;
    private JComboBox remarkingField;
    private JComboBox attendanceField;
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
    private JTextField reasonField;
    private JLabel reasonLabel;
    private JComboBox resultBox;

    private TableRowSorter<TableModel> rowSorter;
    private Application selectedApplication;
    private Integer selectedRowIndex;

    public ApplicationForm(User user) {
        this.user = user;
        initComponents();
        clickListener = new ClickListener();
        this.controller = new ApplicationController();
        this.list = controller.getListByStudent(user.getUsername());
        loadRemarkingList();
        loadAttendanceList();
        this.model = (DefaultTableModel) applicationTable.getModel();
        showDataTable();
    }

    private void loadRemarkingList(){
        this.remarkingField.setModel(new DefaultComboBoxModel());
        this.remarkingBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        this.remarkingList = controller.getRemarkingList(new Date());
        this.remarkingBoxModel = (DefaultComboBoxModel) remarkingBox.getModel();
        this.remarkingFieldModel = (DefaultComboBoxModel) remarkingField.getModel();
        addToRemarkingBox();
    }

    private void addToRemarkingBox() {
        for (Remarking remarking : remarkingList) {
            remarkingFieldModel.addElement(CustomDate.serialize(remarking.getOpening())+"-"+CustomDate.serialize(remarking.getClosing()));
            remarkingBoxModel.addElement(CustomDate.serialize(remarking.getOpening())+"-"+CustomDate.serialize(remarking.getClosing()));
        }
        remarkingField.setModel(remarkingFieldModel);
        remarkingBox.setModel(remarkingBoxModel);
    }

    private void loadAttendanceList(){
        this.attendanceField.setModel(new DefaultComboBoxModel(
                new Object[]{"Chọn"}
        ));
        this.attendanceList = controller.getTranscriptList(user.getUsername());
        this.attendanceFieldModel = (DefaultComboBoxModel) attendanceField.getModel();
        addToAttendanceBox();
    }

    private void addToAttendanceBox() {
        for (Attendance attendance : attendanceList) {
            attendanceFieldModel.addElement(attendance.getCalendar().getClasses().getName()+"-"+attendance.getCalendar().getSubject().getCode());
        }
        attendanceField.setModel(attendanceFieldModel);
    }

    private void showDataTable() {
//        model.setColumnIdentifiers(new Object[]{
//                "STT", "Môn học", "PK Điểm GK", "PK Điểm CK", "PK Điểm khác", "PK Điểm tổng", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "KQ", "Kỳ", "Lý do"
//        });
        int i = 1;
        for (Application transcript : list) {
            model.addRow(new Object[]{
                    i++, transcript.getAttendance().getCalendar().getClasses().getName()+"-"+transcript.getAttendance().getCalendar().getSubject().getCode(),
                    transcript.getMiddleExpect(), transcript.getFinalExpect(), transcript.getOtherExpect(), transcript.getMarkExpect(),
                    transcript.getNewMiddle(), transcript.getNewFinal(), transcript.getNewOther(), transcript.getNewMark(),
                    transcript.getStatus(),
                    CustomDate.serialize(transcript.getRemarking().getOpening())+"-"+CustomDate.serialize(transcript.getRemarking().getClosing()),
                    transcript.getReason()
            });
        }

        rowSorter = new TableRowSorter<>(applicationTable.getModel());
        applicationTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        applicationTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        applicationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        applicationTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        applicationTable.addMouseListener(getDataRow());

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        applicationTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        applicationTable.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);

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

                    attendanceField.setSelectedItem(model.getValueAt(selectedRowIndex, 1).toString());
                    remarkingField.setSelectedItem(model.getValueAt(selectedRowIndex, 11).toString());

                    if(model.getValueAt(selectedRowIndex, 2) != null)
                        mdExpectField.setText(model.getValueAt(selectedRowIndex, 2).toString());
                    if(model.getValueAt(selectedRowIndex, 3) != null)
                        fnExpectField.setText(model.getValueAt(selectedRowIndex, 3).toString());
                    if(model.getValueAt(selectedRowIndex, 4) != null)
                        otExpectField.setText(model.getValueAt(selectedRowIndex, 4).toString());
                    if(model.getValueAt(selectedRowIndex, 5) != null)
                        markExpectField.setText(model.getValueAt(selectedRowIndex, 5).toString());
                    reasonField.setText(model.getValueAt(selectedRowIndex, 12).toString());
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


    private void getTranscriptDetail() {
        mdMarkField.setText("");
        fnMarkField.setText("");
        otMarkField.setText("");
        markField.setText("");

        String attendanceSelected = attendanceField.getSelectedItem().toString();
        if (!attendanceSelected.equals("Chọn")) {
            Attendance transcript = new Attendance();
            for (Attendance s : attendanceList) {
                String cl = s.getCalendar().getClasses().getName() + "-" + s.getCalendar().getSubject().getCode();
                if (cl.equals(attendanceSelected)) {
                    transcript = s;
                    break;
                }
            }

            if (transcript != null) {
                if (transcript.getMiddleMark() != null)
                    mdMarkField.setText(transcript.getMiddleMark().toString());
                if (transcript.getFinalMark() != null)
                    fnMarkField.setText(transcript.getFinalMark().toString());
                if (transcript.getOtherMark() != null)
                    otMarkField.setText(transcript.getOtherMark().toString());
                if (transcript.getMark() != null)
                    markField.setText(transcript.getMark().toString());
            }
        }
    }

    private void save() {
        String rs = "Lưu thất bại!";
        String remarkingSelected = remarkingField.getSelectedItem().toString();
        String attendanceSelected = attendanceField.getSelectedItem().toString();
        String middleMark = mdExpectField.getText().trim();
        String finalMark = fnExpectField.getText().trim();
        String otherMark = otExpectField.getText().trim();
        String mark = markExpectField.getText().trim();
        String reason = reasonField.getText().trim();

        if (remarkingSelected == null || remarkingSelected.isEmpty()){
            rs = "Không có lịch phúc khảo nào!";
        }
        else if(attendanceSelected == null || attendanceSelected.isEmpty()){
            rs = "Không có môn nào được chọn!";
        }
        else if (reason.isEmpty()){
            rs = "Vui lòng nhập lý do!";
        }
        else {
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

            List<Application> newList = new ArrayList<>();
            Remarking remarking = new Remarking();
            for (Remarking c : remarkingList) {
                String cl = CustomDate.serialize(c.getOpening()) + "-" + CustomDate.serialize(c.getClosing());
                if (cl.equals(remarkingSelected)) {
                    remarking = c;
                    break;
                }
            }
            Attendance attendance = new Attendance();
            for (Attendance s : attendanceList) {
                String cl = s.getCalendar().getClasses().getName() + "-" + s.getCalendar().getSubject().getCode();
                if (cl.equals(attendanceSelected)) {
                    attendance = s;
                    break;
                }
            }
            if (selectedApplication != null) {
                selectedApplication.setAttendance(attendance);
                selectedApplication.setRemarking(remarking);
                selectedApplication.setMiddleExpect(newMiddle);
                selectedApplication.setFinalExpect(newFinal);
                selectedApplication.setOtherExpect(newOther);
                selectedApplication.setMarkExpect(newMark);
                selectedApplication.setReason(reason);

                for (Application s : list) {
                    if(selectedApplication.getStatus() == null) {
                        if (selectedApplication.getRemarking().equals(s.getRemarking()) && selectedApplication.getAttendance().equals(s.getAttendance())) {
                            newList.add(selectedApplication);
                        } else {
                            if (remarkingSelected.equals(CustomDate.serialize(s.getRemarking().getOpening()) + "-" + CustomDate.serialize(s.getRemarking().getClosing())) && attendanceSelected.equals(s.getAttendance().getCalendar().getClasses().getName() + "-" + s.getAttendance().getCalendar().getSubject().getCode())) {
                                checkId = false;
                                break;
                            }
                            newList.add(s);
                        }
                        if (checkId == true) {
                            response = controller.update(selectedApplication);
                        }
                    }
                    else {
                        checkId = false;
                        break;
                    }
                }
            } else {
                for (Application s : list) {
                    if (remarkingSelected.equals(CustomDate.serialize(s.getRemarking().getOpening()) + "-" + CustomDate.serialize(s.getRemarking().getClosing())) && attendanceSelected.equals(s.getAttendance().getCalendar().getClasses().getName() + "-" + s.getAttendance().getCalendar().getSubject().getCode())) {
                        checkId = false;
                        break;
                    }
                }
                if (checkId == true) {
                    Application newApplication = new Application();
                    newApplication.setRemarking(remarking);
                    newApplication.setAttendance(attendance);
                    newApplication.setMiddleExpect(newMiddle);
                    newApplication.setFinalExpect(newFinal);
                    newApplication.setOtherExpect(newOther);
                    newApplication.setMarkExpect(newMark);
                    newApplication.setReason(reason);
                    response = controller.create(newApplication);
                    newList = controller.getList();
                }
            }

            if (checkId == true) {
                if (response == true) {
                    if (selectedApplication != null) {
                        model.setValueAt(attendanceSelected, selectedRowIndex, 1);
                        model.setValueAt(remarkingSelected, selectedRowIndex, 11);
                        model.setValueAt(middleMark, selectedRowIndex, 2);
                        model.setValueAt(finalMark, selectedRowIndex, 3);
                        model.setValueAt(otherMark, selectedRowIndex, 4);
                        model.setValueAt(mark, selectedRowIndex, 5);
                        model.setValueAt(reason, selectedRowIndex, 12);
                    } else {
                        Integer i = 0;
                        if (model.getRowCount() > 0) {
                            i = Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString());
                        }
                        model.addRow(new Object[]{
                                ++i, attendanceSelected,
                                middleMark, finalMark, otherMark, mark,
                                null, null, null, null,
                                null,
                                remarkingSelected,
                                reason
                        });
                    }

                    model.fireTableDataChanged();
                    applicationTable.repaint();
                    rs = "Lưu thành công!";
                    list = newList;
                }
            } else {
                rs = "Đơn phúc khảo đã tồn tại!";
            }
        }

        clickListener.showMessage(rs);
        this.refresh();
    }

    private void delete() {
        String rs = "Có lỗi xảy ra!";
        if (clickListener.deleteClick()) {
            if (selectedApplication.getStatus() == null) {
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
            }
            else {
                rs = "Không thể xóa!";
            }
            clickListener.showMessage(rs);
            this.refresh();
        }
    }

    private void clearAll() {
        if (clickListener.deleteClick()) {
            String rs = "Có lỗi xảy ra!";
            boolean response = controller.deleteAllByStudent(user.getUsername());
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
        reasonField.setText("");
        attendanceField.setSelectedItem("Chọn");
        applicationTable.getSelectionModel().clearSelection();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("ĐƠN PHÚC KHẢO");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton = new JButton("+ Thêm mới");
        addButton.addActionListener(e -> refresh());

        remarkingLabel = new JLabel("Kỳ phúc khảo: ");
        attendanceLabel = new JLabel("Môn học: ");
        mdMarkLabel = new JLabel("Điểm GK: ");
        fnMarkLabel = new JLabel("Điểm CK: ");
        otMarkLabel = new JLabel("Điểm khác: ");
        markLabel = new JLabel("Điểm tổng: ");
        mdExpectLabel = new JLabel("Phúc khảo điểm GK: ");
        fnExpectLabel = new JLabel("Phúc khảo điểm CK: ");
        otExpectLabel = new JLabel("Phúc khảo điểm khác: ");
        markExpectLabel = new JLabel("Phúc khảo điểm tổng: ");
        reasonLabel = new JLabel("Lý do: ");

        mdMarkField = new JTextField();
        fnMarkField = new JTextField();
        otMarkField = new JTextField();
        markField = new JTextField();
        mdExpectField = new JTextField();
        fnExpectField = new JTextField();
        otExpectField = new JTextField();
        markExpectField = new JTextField();
        reasonField = new JTextField();

        saveButton = new JButton("Lưu lại");
        saveButton.addActionListener(e -> save());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e -> refresh());

        resultBox = new JComboBox(new Object[]{"Tất cả", "Đã cập nhật điểm", "Không cập nhật điểm", "Chưa xem"});
        resultBox.addActionListener(e->filterResult());

        remarkingField = new JComboBox(new DefaultComboBoxModel(

        ));

        attendanceField = new JComboBox(new DefaultComboBoxModel(

        ));
        attendanceField.addActionListener(e -> getTranscriptDetail());


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
                        "STT", "Môn học", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "KQ", "Kỳ", "Lý do"
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
                        .addComponent(addButton)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(remarkingLabel)
                                                .addComponent(remarkingField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(attendanceLabel)
                                                .addComponent(attendanceField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(reasonLabel)
                                                .addComponent(reasonField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(addButton)
                                .addGap(18, 18, 18)

                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(remarkingLabel)
                                                        .addComponent(remarkingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(attendanceLabel)
                                                        .addComponent(attendanceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(reasonLabel)
                                                        .addComponent(reasonField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
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
