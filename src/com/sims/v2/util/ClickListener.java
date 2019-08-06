package com.sims.v2.util;

import javax.swing.*;
import java.io.File;

public class ClickListener {
    public void exitClick() {
        String ObjButtons[] = {"Đồng ý","Hủy"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Bạn có chắc chắn muốn thoát chương trình không?","QUẢN LÝ SINH VIÊN",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    public boolean deleteClick() {
        String ObjButtons[] = {"Đồng ý","Hủy"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Bạn có chắc chắn muốn thực hiện không?","THÔNG BÁO",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION)
        {
            return true;
        }
        return false;
    }

    public void showMessage(String message){
        JOptionPane.showMessageDialog(null, message, "THÔNG BÁO",
                JOptionPane.WARNING_MESSAGE);
    }

    public String importClick(){
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String rs;
            if (fileChooser.getSelectedFile() == null) {
                rs = "Vui lòng chọn 1 file!";
            } else {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                File ff = new File(fileName);
                String ffname = ff.getName();
                int aa = ffname.indexOf(".");
                String fftype = ffname.substring(aa + 1);
                if (fftype.equals("csv")) {
                    if (ff.length() > 20000000) {
                        rs = "Kích cỡ file không được vượt quá 20Mb!";
                    } else {
                        path = fileChooser.getSelectedFile().getPath();
                        return path;
                    }
                } else {
                    rs = "Vui lòng chọn file CSV!";
                }
            }
            showMessage(rs);
        }
        return path;
    }
}
