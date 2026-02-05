package com.school.app.model;

public class Student {
    private Long id;
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String className;

    public Student() {}

    public Student(Long id, String nationalCode, String firstName, String lastName, String className) {
        this.id = id;
        this.nationalCode = nationalCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNationalCode() { return nationalCode; }
    public void setNationalCode(String nationalCode) { this.nationalCode = nationalCode; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", nationalCode='" + nationalCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
