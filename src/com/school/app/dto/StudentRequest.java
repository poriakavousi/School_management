package com.school.app.dto;

public class StudentRequest {
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String className;

    public StudentRequest() {}

    public StudentRequest(String nationalCode, String firstName, String lastName, String className) {
        this.nationalCode = nationalCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
    }

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
        return "StudentRequest{" +
                "nationalCode='" + nationalCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}