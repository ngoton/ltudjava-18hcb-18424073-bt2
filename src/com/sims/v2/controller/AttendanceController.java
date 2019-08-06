package com.sims.v2.controller;

import com.sims.v2.model.*;
import com.sims.v2.service.*;
import com.sims.v2.view.AttendanceForm;

import java.util.List;

public class AttendanceController {
    private AttendanceForm attendanceForm;
    private AttendanceService attendanceService;
    private ClassesService classesService;
    private CalendarService calendarService;
    private StudentService studentService;

    public AttendanceController(AttendanceForm attendanceForm){
        this.attendanceForm = attendanceForm;
        this.attendanceService = new AttendanceServiceImpl();
        this.classesService = new ClassesServiceImpl();
        this.calendarService = new CalendarServiceImpl();
        this.studentService = new StudentServiceImpl();
    }

    public List<Attendance> getList(){
        return attendanceService.getList();
    }

    public List<Classes> getClassList(){
        return classesService.getList();
    }

    public List<Calendar> getCalendarList(){
        return calendarService.getList();
    }

    public List<Student> getStudentList(){
        return studentService.getList();
    }

    public boolean save(List<Attendance> attendances){
        return attendanceService.save(attendances);
    }

    public boolean deleteAll(){
        return attendanceService.deleteAll();
    }

}
