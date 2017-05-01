package com.haulmont.testtask.Entities;

/**
 * Created by Виктор on 30.03.2017.
 */
public class Client {

    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private String phone;

    public Client() {
    }

    public Client(String surname, String name, String middleName, String phone) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.phone = phone;
    }

    public Client(String surname, String name, String middleName, String phone, Long id) {
        this(surname, name, middleName, phone);
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getPhone() {
        return this.phone;
    }


}


