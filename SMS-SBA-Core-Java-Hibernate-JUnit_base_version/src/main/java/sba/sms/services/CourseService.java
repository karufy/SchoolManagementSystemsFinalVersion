package sba.sms.services;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;


public class CourseService implements CourseI{
    @Override
    public void createCourse(Course course) {
// TODO Auto-generated method stub
//Hibernate SessionFactory is used in creation of Session methods such as save, delete and update, to perform CRUD-based operations on the database
//SessionFactory is a factory class for Session objects. It is available for the whole application while a Session is only available per particular transaction
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();

        try {
            s.persist(course);
            t.commit();

        } catch (Exception e) {
            if (t != null)
                t.rollback(); //
            e.printStackTrace();
        } finally {
            if (s != null)
                s.close();
            if (sf != null)
                sf.close();
        }
    }

    @Override
    public List<Course> getAllCourses() {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        List<Course> coursesList = new ArrayList<>();

        try {
            coursesList = s.createQuery("FROM Course", Course.class).list();
            t.commit();
            // Check for duplicates and add only unique activities to the list
            for (Course course : coursesList) {
                if (!coursesList.contains(course))
                    coursesList.add(course);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s != null)
                s.close();
            if (sf != null)
                sf.close();
        }

        return coursesList;
    }

    @Override
    public Course getCourseById(int courseId) {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        Course act = new Course();
        try {
            // retrieve course by its ID
            act = s.get(Course.class, courseId);

            t.commit();
        } catch (Exception e) {
            if (t != null)
                t.rollback();
            e.printStackTrace();
        } finally {
            // Close session and session factory
            if (s != null)
                s.close();

            if (sf != null)
                sf.close();
        }

        return act;
    }
}
