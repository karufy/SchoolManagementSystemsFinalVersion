package sba.sms.models;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;


//Hibernate Entity attributes
@Entity
@Table(name = "Course")
public class Course {


//GeneratedValue: used to specify the primary key generation strategy.
//Instructs the database to generate a value for (id) field automatically.
//If the strategy is not specified by default AUTO will be used
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


//@Column annotation is part of the JPA specification
//(nullable = false) adds a not null constraint to the table definition. Hibernate will not perform any validation on the entity attribute.
//(unique = true) is redundant. I just want to specify that it corresponds to a single column not a composite key column
    @Column(name = "id", unique = true, length = 50, nullable = false)
    private int id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "instructor", nullable = false, length = 50)
    private String instructor;


//Cascade Type @ManyToMany is mapped on the parent sides of the association while the child side (the join table) is hidden
// Shouldn't use CascadeType.ALL because the other CascadeType.*** might end-up doing more than expected
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
// Redundant because it has been used in the child side.
//    @JoinTable(name = "student_courses",
//            joinColumns = @JoinColumn(name = "courses_id"),
//            inverseJoinColumns = @JoinColumn(name = "student_email"))
        private List<Student> students;


//No args constructor
    public Course() {}

//All args constructor
    public Course(int id, String name, String instructor, List<Student> students) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.students = students;
    }

//Required args constructor. (Name and instructor only)
    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }


// Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    public List<Student> getStudents() {
        return students;
    }
    public void setStudents(List<Student> students) {
        this.students = students;
    }


// toString (exclude collections i.e students to avoid infinite loops)
// You can also use Lombok @toString(exclude={}) to annotate the classes
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }

//override equals and hashcode methods (don't use lombok here) because the class is not final and doesn't extend java.lang.object, it can break the contract
//You must override hashCode() in every class that overrides equals(). Failure to do so will result in a violation of the general contract for Object. hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getId() == course.getId() && Objects.equals(getName(), course.getName()) && Objects.equals(getInstructor(), course.getInstructor()) && Objects.equals(getStudents(), course.getStudents());
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
