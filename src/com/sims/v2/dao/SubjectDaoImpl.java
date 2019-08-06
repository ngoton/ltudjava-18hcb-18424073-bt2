package com.sims.v2.dao;

import com.sims.v2.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectDaoImpl extends IOFileDao implements SubjectDao {
    private static String subjectFile = "data/subject.txt";

    public SubjectDaoImpl(){

    }

    @Override
    public List<Subject> getList(){
        List<Subject> list = new ArrayList<>();
        List<String[]> data = readFile(subjectFile, "\\|");
        for (String[] arr : data){
            Subject subject = new Subject();
            subject.setId(Integer.parseInt(arr[0]));
            subject.setCode(arr[1]);
            subject.setName(arr[2]);

            list.add(subject);
        }
        return list;
    }

    @Override
    public Subject getSubjectById(Integer id){
        List<Subject> subject = this.getList();
        Subject cls = null;
        for (Subject c : subject){
            if (id.equals(c.getId())){
                cls = c;
                break;
            }
        }
        return cls;
    }

    @Override
    public Subject getSubjectByName(String name){
        List<Subject> subject = this.getList();
        Subject cls = null;
        for (Subject c : subject){
            if (name.equals(c.getName())){
                cls = c;
                break;
            }
        }
        return cls;
    }

    @Override
    public Subject getSubjectByCode(String code){
        List<Subject> subject = this.getList();
        Subject cls = null;
        for (Subject c : subject){
            if (code.equals(c.getCode())){
                cls = c;
                break;
            }
        }
        return cls;
    }

    @Override
    public boolean addOne(Subject subject){
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(subject);
        return writeFile(subjectList, subjectFile, true);
    }
}
