package com.sims.v2.dao;

import com.sims.v2.model.*;

import java.util.ArrayList;
import java.util.List;

public class CalendarDaoImpl extends IOFileDao implements CalendarDao {
    private static String calendarFile = "data/calendar.txt";
    private ClassesDao classesDao;
    private SubjectDao subjectDao;

    public CalendarDaoImpl(){
        this.classesDao = new ClassesDaoImpl();
        this.subjectDao = new SubjectDaoImpl();
    }

    @Override
    public List<Calendar> getList(){
        List<Calendar> list = new ArrayList<>();
        List<String[]> data = readFile(calendarFile, "\\|");
        for (String[] arr : data){
            Calendar calendar = new Calendar();
            calendar.setId(Integer.parseInt(arr[0]));
            calendar.setRoom(arr[3]);

            Classes classes = classesDao.getClassById(Integer.parseInt(arr[1]));
            if (classes != null){
                calendar.setClasses(classes);
            }

            Subject subject = subjectDao.getSubjectById(Integer.parseInt(arr[2]));
            if (subject != null){
                calendar.setSubject(subject);
            }

            list.add(calendar);
        }

        return list;
    }

    @Override
    public List<Calendar> getListByStudent(String code){
        AttendanceDao attendanceDao = new AttendanceDaoImpl();
        List<Attendance> attendanceList = attendanceDao.getList();
        List<Calendar> list = new ArrayList<>();
        List<String[]> data = readFile(calendarFile, "\\|");
        for (String[] arr : data){
            Calendar calendar = new Calendar();
            calendar.setId(Integer.parseInt(arr[0]));
            calendar.setRoom(arr[3]);

            Classes classes = classesDao.getClassById(Integer.parseInt(arr[1]));
            if (classes != null){
                calendar.setClasses(classes);
            }

            Subject subject = subjectDao.getSubjectById(Integer.parseInt(arr[2]));
            if (subject != null){
                calendar.setSubject(subject);
            }

            boolean check = false;
            for (Attendance attendance : attendanceList){
                if (attendance.getCalendar().getId().equals(calendar.getId()) && attendance.getStudent().getCode().equals(code)){
                    check = true;
                    break;
                }
            }
            if (check == true) {
                list.add(calendar);
            }
        }

        return list;
    }

    @Override
    public Calendar getCalendarById(Integer id){
        List<Calendar> calendars = this.getList();
        Calendar calendar = null;
        for (Calendar s : calendars){
            if (id.equals(s.getId())){
                calendar = s;
                break;
            }
        }
        return calendar;
    }

    @Override
    public Calendar getCalendarByName(String name){
        List<Calendar> calendars = this.getList();
        Calendar calendar = null;
        for (Calendar s : calendars){
            if (name.equals(s.getClasses().getName()+"-"+s.getSubject().getCode())){
                calendar = s;
                break;
            }
        }
        return calendar;
    }

    @Override
    public boolean save(List<Calendar> calendars){
        StudentDao studentDao = new StudentDaoImpl();
        AttendanceDao attendanceDao = new AttendanceDaoImpl();
        TranscriptDao transcriptDao = new TranscriptDaoImpl();
        List<Student> studentList = studentDao.getList();
        List<Attendance> attendances = attendanceDao.getList();
        List<Attendance> attendanceList = new ArrayList<>();
        List<Transcript> transcriptList = new ArrayList<>();
        Integer lastId = 0;
        if (attendances.size() > 0) {
            lastId = attendances.get(attendances.size() - 1).getId();
        }
        for (Calendar c : calendars){
            List<Attendance> list = attendanceDao.getAttendanceByCalendar(c);
            List<Transcript> listTrans = transcriptDao.getTranscriptByCalendar(c);
            if (list == null || list.size() == 0){
                for (Student s : studentList) {
                    if (s.getStudentClass().getId().equals(c.getClasses().getId())) {
                        Attendance attendance = new Attendance();
                        attendance.setId(++lastId);
                        attendance.setCalendar(c);
                        attendance.setStudent(s);
                        attendanceDao.addOne(attendance);
                        attendanceList.add(attendance);
                    }
                }
            }
            else {
                for (Attendance attendance : list) {
                    attendanceList.add(attendance);
                }
                for (Transcript transcript : listTrans) {
                    transcriptList.add(transcript);
                }
            }

        }

        attendanceDao.updateAll(attendanceList);
        transcriptDao.save(transcriptList);

        return writeFile(calendars, calendarFile, false);
    }

    @Override
    public boolean deleteAll(){
        AttendanceDao attendanceDao = new AttendanceDaoImpl();
        attendanceDao.deleteAll();
        TranscriptDao transcriptDao = new TranscriptDaoImpl();
        transcriptDao.deleteAll();
        return writeFile(null, calendarFile, false);
    }

    @Override
    public List<Calendar> importFile(String path){
        StudentDao studentDao = new StudentDaoImpl();
        AttendanceDao attendanceDao = new AttendanceDaoImpl();
        List<Calendar> list = getList();
        List<Calendar> newList = new ArrayList<>();
        Subject subject = null;
        Classes classes = null;
        List<String[]> data = readFile(path, ",");
        Integer lastCalendar = 0;
        if (list.size() > 0) {
            lastCalendar = list.get(list.size() - 1).getId();
        }

        List<Attendance> attendanceList = attendanceDao.getList();
        Integer lastAttendance = 0;
        if (attendanceList.size() > 0) {
            lastAttendance = attendanceList.get(attendanceList.size() - 1).getId();
        }
        List<Student> studentList = studentDao.getList();

        int i = 0;
        for (String[] arr : data){
            if (i == 0){
                String className = arr[0].trim();
                if (!className.isEmpty()){
                    classes = classesDao.getClassByName(className);
                    if (classes == null){
                        List<Classes> classesList = classesDao.getList();
                        Integer lastId = 0;
                        if (classesList.size() > 0) {
                            lastId = classesList.get(classesList.size() - 1).getId();
                        }
                        Classes c = new Classes();
                        c.setId(++lastId);
                        c.setName(className);
                        if(classesDao.addOne(c)){
                            classes = c;
                        }
                        else {
                            return list;
                        }
                    }
                }
            }
            else if (i > 1){
                String subjectCode = arr[1].trim();
                if (!subjectCode.isEmpty()){
                    subject = subjectDao.getSubjectByCode(subjectCode);
                    if (subject == null){
                        List<Subject> subjectList = subjectDao.getList();
                        Integer lastId = 0;
                        if (subjectList.size() > 0) {
                            lastId = subjectList.get(subjectList.size() - 1).getId();
                        }
                        Subject c = new Subject();
                        c.setId(++lastId);
                        c.setCode(subjectCode);
                        c.setName(arr[2].trim());
                        if(subjectDao.addOne(c)){
                            subject = c;
                        }
                        else {
                            return list;
                        }
                    }
                }

                boolean checkCode = true;
                for (Calendar cl : list){
                    if (classes.getId().equals(cl.getClasses().getId()) && subject.getId().equals(cl.getSubject().getId())){
                        checkCode = false;
                        break;
                    }
                }

                if (checkCode == true) {
                    Calendar calendar = new Calendar();
                    calendar.setId(++lastCalendar);
                    calendar.setRoom(arr[3].trim());
                    calendar.setClasses(classes);
                    calendar.setSubject(subject);
                    newList.add(calendar);
                    list.add(calendar);

                    for (Student s : studentList) {
                        if (s.getStudentClass().getId().equals(classes.getId())) {
                            Attendance attendance = new Attendance();
                            attendance.setId(++lastAttendance);
                            attendance.setStudent(s);
                            attendance.setCalendar(calendar);
                            //attendanceDao.addOne(attendance);
                            attendanceList.add(attendance);
                        }
                    }
                }
            }
            i++;
        }

        attendanceDao.save(attendanceList);

        writeFile(newList, calendarFile, true);

        return list;
    }
}
