package com.haulmont.testtask.Entities;

import com.vaadin.data.util.filter.Or;

import java.util.Date;


/**
 * Created by Виктор on 30.03.2017.
 */
public class Order {
    private Long id;
    private String description;
    private Long client;
    private Date dateOfCreation;
    private Date dateOfCompletionOfWork;
    private Double cost;
    private String status;

    public Order(String description, Long client, Date dateOfCreation, Date dateOfCompletionOfWork, Double cost, String status) {
        this.description = description;
        this.client = client;
        this.dateOfCreation = dateOfCreation;
        this.dateOfCompletionOfWork = dateOfCompletionOfWork;
        this.cost = cost;
        this.status = status;
    }

    public Order(String description, Long client, Date dateOfCreation, Date dateOfCompletionOfWork, Double cost, String status, Long id) {
        this(description, client, dateOfCreation, dateOfCompletionOfWork, cost, status);
        this.id = id;
    }

    public Order() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClient(Long client) {
        this.client = client;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void setDateOfCompletionOfWork(Date dateOfCompletionOfWork) {
        this.dateOfCompletionOfWork = dateOfCompletionOfWork;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getClient() {
        return this.client;
    }

    public Date getDateOfCreation() {
        return this.dateOfCreation;
    }

    public Date getDateOfCompletionOfWork() {
        return this.dateOfCompletionOfWork;
    }

    public Double getCost() {
        return this.cost;
    }

    public String getStatus() {
        return this.status;
    }
}
