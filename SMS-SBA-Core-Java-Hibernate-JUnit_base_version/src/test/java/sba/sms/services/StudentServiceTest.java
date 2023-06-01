package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Lombok @FieldDefaults annotation can add an access modifier ( public , private , or protected ) to each field in the annotated class
//I added (makeFinal) to automatically make the fields private final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal=true)

class StudentServiceTest {
    static StudentService studentService;
    static CourseService courseService;
    @BeforeAll
    static void beforeAll() {
        studentService = new StudentService();
        courseService = new CourseService();
        //CommandLine.addData();
    }
    @Test
    void getAllStudents() {
        List<Student> expected = new ArrayList<>(Arrays.asList(
                new Student("reema@gmail.com", "reema brown", "password"),
                new Student("annette@gmail.com", "annette allen", "password"),
                new Student("anthony@gmail.com", "anthony gallegos", "password"),
                new Student("ariadna@gmail.com", "ariadna ramirez", "password"),
                new Student("bolaji@gmail.com", "bolaji saibu", "password")));
        Assertions.assertEquals(expected.size(), studentService.getAllStudents().size());
    }
    @Test
    public void testGetStudentByEmail() {
        Student student = studentService.getStudentByEmail("anthony@gmail.com");
        Assertions.assertEquals("anthony gallegos", student.getName());
    }
    @Test
    public void testValidateStudent() {
        boolean isValid = studentService.validateStudent("ariadna@gmail.com", "password");
        Assertions.assertTrue(isValid);
    }
    @Test
    public void testGetAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        Assertions.assertEquals(25, courseService.getAllCourses().size());
    }
    @Test
    void testGetCourseById() {
        Course actualCourse = new Course("Java","Phillip Witkin");
        Course retrievedCourse = courseService.getCourseById(1);
        System.out.print("retrieveCourse: " + retrievedCourse);
        Assertions.assertNotNull(retrievedCourse);
        Assertions.assertEquals(actualCourse.getInstructor(), retrievedCourse.getInstructor());
        Assertions.assertEquals(actualCourse.getName(), retrievedCourse.getName());
    }
        
    @Test
    public void testGetStudentCourses() {
        List<Course> courses = studentService.getStudentCourses("reema@gmail.com");
        Assertions.assertEquals(1, courses.size());
    }
    @Test
    public void testRegisterStudentToCourse() {
        studentService.registerStudentToCourse("bolaji@gmail.com", 1);
        Student student = studentService.getStudentByEmail("bolaji@gmail.com");
        Assertions.assertEquals(2, student.getCourses().size());
    }
}
