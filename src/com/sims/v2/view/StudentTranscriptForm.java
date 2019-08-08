package com.sims.v2.view;

import com.sims.v2.controller.TranscriptController;
import com.sims.v2.model.Attendance;
import com.sims.v2.model.Calendar;
import com.sims.v2.model.User;
import com.sims.v2.util.ClickListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class StudentTranscriptForm extends JPanel {
    private User user;
    private static Float basicMark = 5.0f;
    private TranscriptController controller;
    private ClickListener clickListener;
    private List<Attendance> list;
    DefaultTableModel model;
    private List<Calendar> calendarList;
    DefaultComboBoxModel calendarBoxModel;
    private JPanel panel;
    private JLabel titleLabel;
    private JComboBox calendarBox;
    private JScrollPane jScrollPane1;
    private JTable transcriptTable;
    private JTextField searchField;

    private TableRowSorter<TableModel> rowSorter;

    public StudentTranscriptForm(User user) {
        this.user = user;
        initComponents();
        clickListener = new ClickListener();
        this.controller = new TranscriptController(this);
        this.list = controller.getListByStudent(user.getUsername());
        loadCalendarList();
        this.model = (DefaultTableModel) transcriptTable.getModel();
        showDataTable();
    }


    private void loadCalendarList(){
        this.calendarBox.setModel(new DefaultComboBoxModel(
                new Object[]{"Tất cả"}
        ));
        this.calendarList = controller.getCalendarListByStudent(user.getUsername());
        this.calendarBoxModel = (DefaultComboBoxModel) calendarBox.getModel();
        addToCalendarBox();
    }

    private void addToCalendarBox() {
        for (Calendar calendar : calendarList) {
            calendarBoxModel.addElement(calendar.getSubject().getCode()+"-"+calendar.getSubject().getName());
        }
        calendarBox.setModel(calendarBoxModel);
    }

    private void showDataTable() {
        model.setColumnIdentifiers(new Object[]{
                "STT", "MSSV", "Họ tên", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Môn học", "KQ"
        });
        int i = 1;
        for (Attendance transcript : list) {
            model.addRow(new Object[]{
                    i++, transcript.getStudent().getCode(), transcript.getStudent().getName(), transcript.getMiddleMark(), transcript.getFinalMark(), transcript.getOtherMark(), transcript.getMark(), transcript.getCalendar().getSubject().getCode()+"-"+transcript.getCalendar().getSubject().getName(), (transcript.getMark()>=basicMark?"Đậu":"Rớt")
            });
        }

        rowSorter = new TableRowSorter<>(transcriptTable.getModel());
        transcriptTable.setRowSorter(rowSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        transcriptTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        transcriptTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        transcriptTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        transcriptTable.getColumnModel().getColumn(8).setPreferredWidth(20);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        transcriptTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        transcriptTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        transcriptTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        transcriptTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);


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

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("BẢNG ĐIỂM");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

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
                        "STT", "MSSV", "Họ tên", "Điểm GK", "Điểm CK", "Điểm khác", "Điểm tổng", "Môn học", "KQ"
                }
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        });

        jScrollPane1.setViewportView(transcriptTable);

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
                                        .addComponent(titleLabel)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(calendarBox, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addGap(600, 600, 600)
                                                .addComponent(searchField)
                                        )
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 1000, GroupLayout.PREFERRED_SIZE)
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
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(calendarBox)
                                                                .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                                        )
                                                )
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(54, Short.MAX_VALUE))))
        );

    }
}
