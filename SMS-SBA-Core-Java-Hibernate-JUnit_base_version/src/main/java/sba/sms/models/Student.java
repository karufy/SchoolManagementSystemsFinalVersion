package sba.sms.models;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;


//Hibernate Entity attributes
@Entity
@Table(name = "student")
public class Student {

//Assigns the email as the primary key
//@Column annotation is part of the JPA specification
//(nullable = false) adds a not null constraint to the table definition. Hibernate will not perform any validation on the entity attribute
    @Id
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "password", nullable = false, length = 50)
    private String password;


//Cascade Type @ManyToMany is mapped on the parent sides of the association while the child side (the join table) is hidden
//Shouldn't use CascadeType.ALL because the other CascadeType.*** might end-up doing more than expected
//The @JoinTable has the "foreignKey"/"inverseForeignKey" attributes because FKs are on/owned by the join table
    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "FK_student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"),
            inverseForeignKey = @ForeignKey(name = "FK_courses_id")
    )
    private List<Course> courses;

//No args constructor
    public Student() {};

//All args constructor
    public Student(String email, String name, String password, List<Course> courses) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.courses = courses;
    }

//Required args constructor
    public Student(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;	}

    public String getEmail() { return email; }


//Override equals and hashcode methods (don't use lombok here) because the class is not final and doesn't extend java.lang.object, it can break the contract
//You must override hashCode() in every class that overrides equals(). Failure to do so will result in a violation of the general contract for Object. hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(courses, email, name, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(courses, other.courses) && Objects.equals(email, other.email)
                && Objects.equals(name, other.name) && Objects.equals(password, other.password);
    }


//Getters and Setters
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student [email=" + email + ", name=" + name + ", password=" + password + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> Courses) {
        this.courses = Courses;
    }

}

