package com.school.app.dto;

import java.time.LocalDateTime;

public class StudentResponse {
    private Long id;
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String className;
    private LocalDateTime createdAt;

    public StudentResponse() {}

    public StudentResponse(Long id, String nationalCode, String firstName,
                           String lastName, String className, LocalDateTime createdAt) {
        this.id = id;
        this.nationalCode = nationalCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "id=" + id +
                ", nationalCode='" + nationalCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", className='" + className + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}