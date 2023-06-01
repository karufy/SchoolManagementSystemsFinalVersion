package sba.sms.services;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import sba.sms.dao.CourseI;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import java.util.ArrayList;
import java.util.List;
public class StudentService implements StudentI {
    @Override
    public void createStudent(Student s) {
// TODO Auto-generated method stub
//Hibernate SessionFactory is used in creation of Session methods such as save, delete and update, to perform CRUD-based operations on the database
//SessionFactory is a factory class for Session objects. It is available for the whole application while a Session is only available per particular transaction
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction t= session.beginTransaction();
        try (sessionFactory; session) {
            session.persist(s);
// Commit the transaction
            t.commit();
        } catch (Exception e) {
// Handle exceptions
            if (t != null && t.isActive()) {
                t.rollback();
            }
// handle the exception accordingly
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> getAllStudents() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction t= session.beginTransaction();
        List<Student> students = new ArrayList<>();
        try {
            students = session.createQuery("FROM Student", Student.class).list();
            t.commit();
        } catch (Exception e) {
            if (t != null && t.isActive())
                t.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
        return students;
    }


    @Override
    public Student getStudentByEmail(String email) {
        SessionFactory factory= new Configuration().configure().buildSessionFactory();
        Session session= factory.openSession();
        Transaction t= session.beginTransaction();
        Student studentbyEmail = new Student();
        try{
            studentbyEmail = session.createQuery("From Student where email = :email", Student.class)
                    .setParameter("email", email).uniqueResult();
            t.commit();
        }catch (Exception e){
            if (t != null && t.isActive())
                t.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
        return studentbyEmail;
    }


    @Override
    public boolean validateStudent(String email, String password) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session= sessionFactory.openSession();
        Transaction t= session.beginTransaction();
        try {
            Student student = new Student();
            student =session.createQuery("From Student where email = :email and password = :password", Student.class)
                    .setParameter("email", email).setParameter("password", password).uniqueResult();
            if(student != null)
                return true;
            t.commit();
        } catch (Exception e) {
            if (t != null && t.isActive())
                t.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
// Retrieve the Student by email
            Student student = session.createQuery("FROM Student WHERE email = :email", Student.class)
                    .setParameter("email", email).uniqueResult();
// Retrieve the Course by ID
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                // Register the Activity to the Volunteer (preventing duplication)
                if (!student.getCourses().contains(course))
                    student.getCourses().add(course);

            }

            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Course> coursesList = new ArrayList<>();
        try {
// Perform the database query to retrieve the Student Courses using native query
            String nativeQuery = "SELECT c.* FROM Course c " +
                    "INNER JOIN student_courses sc ON c.id = sc.courses_id " +
                    "INNER JOIN Student s ON sc.Fk_student_email = s.email " +
                    "WHERE s.email = :email";
            coursesList = session.createNativeQuery(nativeQuery, Course.class)
                    .setParameter("email", email)
                    .list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
        return coursesList;
    }
}
