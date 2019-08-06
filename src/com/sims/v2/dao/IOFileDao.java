package com.sims.v2.dao;

import com.sims.v2.model.User;
import com.sims.v2.util.MD5Encrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOFileDao<T> {
    public List<T> readFile(String path, String regex){
        List<T> list = new ArrayList<>();
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                if (path.equals("data/user.txt")){
                    List<User> userList = new ArrayList<>();
                    User user = new User();
                    user.setId(1);
                    user.setUsername("giaovu");
                    user.setPassword(MD5Encrypt.convertHashToString("giaovu"));
                    user.setRole("ADMIN");
                    user.setStudent(null);
                    userList.add(user);
                    writeFile((List<T>) userList, path, false);
                }
            }
            catch (IOException e){
                System.out.println("Cannot create a file!");
            }
        }
        try(FileInputStream fis = new FileInputStream(file)){
            String thisLine;
            InputStreamReader isr = new InputStreamReader(fis, "UTF8");
            try(BufferedReader br = new BufferedReader(isr)) {
                while((thisLine = br.readLine()) != null){
                    if(!thisLine.isEmpty()){
                        thisLine = thisLine.replace("\uFEFF", "");
                        T arr = (T)thisLine.split(regex);
                        list.add(arr);
                    }
                }
                br.close();
                isr.close();
                fis.close();
            } catch (IOException e) {
                System.out.println("Got an exception!");
            }
        }
        catch (IOException e){
            System.out.println("File not found!");
        }
        return list;
    }
    public boolean writeFile(List<T> data, String path, boolean append){
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            }
            catch (IOException e){
                System.out.println("Cannot create a file!");
            }
        }
        try(FileOutputStream fos = new FileOutputStream(file, append)){
            OutputStreamWriter isw = new OutputStreamWriter(fos, "UTF8");
            try (BufferedWriter bw = new BufferedWriter(isw)) {
                if (data == null){
                    bw.write("");
                }
                else {
                    for (T s : data) {
                        bw.write(s.toString());
                        bw.newLine();
                    }
                }

                bw.close();
                isw.close();
                fos.close();
                return true;
            }catch (IOException e){
                System.out.println("Got an exception!");
            }
        }
        catch (IOException e){
            System.out.println("File not found!");
        }

        return false;
    }

}
