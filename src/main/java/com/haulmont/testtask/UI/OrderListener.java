package com.haulmont.testtask.UI;

import com.haulmont.testtask.DAO.DBClient;
import com.haulmont.testtask.DAO.DBConnection;
import com.haulmont.testtask.DAO.DBOrder;
import com.haulmont.testtask.Entities.Client;
import com.haulmont.testtask.Entities.Order;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Виктор on 27.04.2017.
 */
public class OrderListener {
    public Window orderAddWindow(Grid grid) {

        //Создаем окно добавления заказа
        Window window = new Window("Добавить заказ");
        window.center();
        window.setModal(true);
        window.setWidth("800");
        window.setHeight("300");

        //Создаем слои
        VerticalLayout clientAddLayout = new VerticalLayout();
        clientAddLayout.setMargin(true);
        clientAddLayout.setSizeFull();
        HorizontalLayout stringLayout = new HorizontalLayout();
        HorizontalLayout dateLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        //Поле ввода описания
        TextField description = new TextField("Описание", "");
        description.setSizeFull();
        description.setRequired(true);
        description.setMaxLength(100);
        description.setRequiredError("Укажите описание!");
        description.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));

        //Поле выбора клиента из существующих
        List<Long> clientsID = new ArrayList<>();
        DBConnection.startConnection();
        List<Client> clients = DBClient.getClientList();
        int index = clients.size();
        for (int i = 0; i < index; i++) {
            clientsID.add(clients.get(i).getId());
        }
        DBConnection.closeConnection();
        ComboBox client = new ComboBox("Клиент", clientsID);
        client.setSizeFull();
        client.setRequired(true);
        client.setRequiredError("Выберите клиента!");
        client.setNullSelectionAllowed(false);

        //Поле ввода даты создания заказа. Заказ может быть принят не позднее текущей даты. Изначально выставляется текущая дата
        DateField dateOfCreation = new DateField("Дата создания");
        dateOfCreation.setSizeFull();
        dateOfCreation.setValue(new Date());
        dateOfCreation.setRequired(true);
        dateOfCreation.setRequiredError("Укажите дату создания!");
        dateOfCreation.addValidator(new DateRangeValidator("Заказ может быть принят не позднее текущей даты",
                null, new java.sql.Date(new Date().getTime()), Resolution.YEAR));

        //Поле даты окончания заказа. Заказ завершается не раньше дня создания заказа
        DateField dateOfCompletionOfWork = new DateField("Дата окончания");
        dateOfCompletionOfWork.setSizeFull();
        dateOfCompletionOfWork.setRequired(true);
        dateOfCompletionOfWork.setRequiredError("Укажите дату окончания!");
        dateOfCompletionOfWork.addValidator(new DateRangeValidator("Заказ может быть завершен не раньше дня его создания",
                dateOfCreation.getValue(), null, Resolution.YEAR));

        //Поле ввода стоимости
        TextField cost = new TextField("Стоимость", "");
        cost.setSizeFull();
        cost.setRequired(true);
        cost.setRequiredError("Укажите стоимость заказа");
        cost.addValidator(new RegexpValidator("[0-9]{1,}(\\.?[0-9]{1,2})", true, "Данные некорректны"));

        //Создаем поле выбора статуса заказа
        List<String> statuses = new ArrayList<>();
        statuses.add("Запланирован");
        statuses.add("Выполнен");
        statuses.add("Принят клиентом");
        ComboBox status = new ComboBox("Выберите статус заказа", statuses);
        status.setSizeFull();
        status.setNullSelectionAllowed(false);
        status.setRequired(true);
        status.setRequiredError("Выберите статус заказа!");

        //Создаем кнопки
        Button add = new Button("Добавить");
        Button close = new Button("Отмена");

        //Назначаем кнопкам действия
        close.addClickListener(clickEvent -> window.close());
        add.addClickListener(clickEvent -> {
            try {
                //Валидируем поля
                description.validate();
                client.validate();
                dateOfCreation.validate();
                dateOfCompletionOfWork.validate();
                cost.validate();
                status.validate();

                //Добавляем заказ в базу
                DBConnection.startConnection();
                Order order = new Order(description.getValue(), Long.parseLong(client.getValue().toString()),
                        dateOfCreation.getValue(), dateOfCompletionOfWork.getValue(), Double.parseDouble(cost.getValue()),
                        status.getValue().toString());
                DBOrder.addOrder(order);

                //Добавляем заказ в таблицу
                List<Order> orders = DBOrder.getOrderList();
                int orderIndex = orders.size();
                grid.addRow(orders.get(orderIndex - 1).getId(), orders.get(orderIndex - 1).getDescription(), orders.get(orderIndex - 1).getClient(),
                        orders.get(orderIndex - 1).getDateOfCreation().toString(), orders.get(orderIndex - 1).getDateOfCompletionOfWork().toString(),
                        orders.get(orderIndex - 1).getCost(), orders.get(orderIndex - 1).getStatus());
                DBConnection.closeConnection();
                window.close();
            } catch (Validator.InvalidValueException e) {
                Notification.show("Заполните все поля");
            }
        });


        //Добавляем все элементы на слои
        stringLayout.addComponent(description);
        stringLayout.addComponent(client);
        stringLayout.addComponent(cost);
        stringLayout.addComponent(status);
        dateLayout.addComponent(dateOfCreation);
        dateLayout.addComponent(dateOfCompletionOfWork);
        buttonsLayout.addComponent(add);
        buttonsLayout.setComponentAlignment(add, Alignment.BOTTOM_LEFT);
        buttonsLayout.addComponent(close);
        buttonsLayout.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);
        //Добавляем слои на главный слой
        clientAddLayout.addComponent(stringLayout);
        clientAddLayout.addComponent(dateLayout);
        clientAddLayout.addComponent(buttonsLayout);
        //добавляем главный слой в окно
        window.setContent(clientAddLayout);
        return window;
    }


    //Метод удаления заказа
    void deleteOrder(Grid grid) {
        //Проверка, не выбрана ли пустая строка
        if (grid.getSelectedRow() == null) {
            Notification.show("Выберите заказ для удаления");
        }
        //Если выбрана не пустая, то удаляем
        else {
            try {
                DBConnection.startConnection();
                DBOrder.deleteOrder((Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).
                        getItemProperty("ID").getValue());
                grid.getContainerDataSource().removeItem(grid.getSelectedRow());
                DBConnection.closeConnection();
            }
            //На всякий случай ловим ошибку пустого значения
            catch (java.lang.NullPointerException e) {
                Notification.show("Выберите заказ для удаления");
            }
        }
    }


    //Метод изменения заказа
    public Window orderChangeWindow(Grid grid) {
        //Создаем окно изменения заказа
        Window window = new Window("Изменить заказ");
        window.center();
        window.setModal(true);
        window.setWidth("800");
        window.setHeight("300");

        //Создаем слои
        VerticalLayout clientUpdateLayout = new VerticalLayout();
        clientUpdateLayout.setMargin(true);
        clientUpdateLayout.setSizeFull();
        HorizontalLayout stringLayout = new HorizontalLayout();
        HorizontalLayout dateLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        //Поле изменения описания
        TextField description = new TextField("", grid.getContainerDataSource().getItem(grid.getSelectedRow())
                .getItemProperty("Описание").getValue().toString());
        description.setSizeFull();
        description.setRequired(true);
        description.setMaxLength(100);
        description.setRequiredError("Укажите описание!");
        description.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));

        //Поле выбора клиента из существующих
        List<Long> clientsID = new ArrayList<>();
        DBConnection.startConnection();
        List<Client> clients = DBClient.getClientList();
        int index = clients.size();
        for (int i = 0; i < index; i++) {
            clientsID.add(clients.get(i).getId());
        }
        DBConnection.closeConnection();
        ComboBox client = new ComboBox("Клиент", clientsID);
        client.setValue(Long.parseLong(grid.getContainerDataSource()
                .getItem(grid.getSelectedRow()).getItemProperty("Клиент").getValue().toString()));
        client.setSizeFull();
        client.setRequired(true);
        client.setRequiredError("Выберите клиента!");
        client.setNullSelectionAllowed(false);

        //Устанавливаем формат вывода даты
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");

        //Поле изменения даты создания заказа
        DateField dateOfCreation = new DateField("Дата создания");
        dateOfCreation.setSizeFull();
        dateOfCreation.setRequired(true);
        dateOfCreation.setRequiredError("Укажите дату создания!");
        dateOfCreation.addValidator(new DateRangeValidator("Заказ может быть принят не позднее текущей даты",
                null, new java.sql.Date(new Date().getTime()), Resolution.YEAR));
        try {
            dateOfCreation.setValue(format.parse(grid.getContainerDataSource().getItem(grid.getSelectedRow())
                    .getItemProperty("Дата создания").getValue().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Поле изменения даты окончания заказа
        DateField dateOfCompletionOfWork = new DateField("Дата окончания");
        dateOfCompletionOfWork.setSizeFull();
        dateOfCompletionOfWork.setRequired(true);
        dateOfCompletionOfWork.setRequiredError("Укажите дату окончания!");
        dateOfCompletionOfWork.addValidator(new DateRangeValidator("Заказ может быть завершен не раньше дня его создания",
                dateOfCreation.getValue(), null, Resolution.YEAR));
        try {
            dateOfCompletionOfWork.setValue(format.parse(grid.getContainerDataSource().getItem(grid.getSelectedRow())
                    .getItemProperty("Дата окончания").getValue().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Поле изменения стоимости заказа
        TextField cost = new TextField("", grid.getContainerDataSource().getItem(grid.getSelectedRow())
                .getItemProperty("Стоимость").getValue().toString());
        cost.setSizeFull();
        cost.setRequired(true);
        cost.setRequiredError("Укажите стоимость заказа");
        cost.addValidator(new RegexpValidator("[0-9]{1,}(\\.?[0-9]{1,2})", true, "Данные некорректны"));

        //Поле выбора статуса заказа
        List<String> statuses = new ArrayList<>();
        statuses.add("Запланирован");
        statuses.add("Выполнен");
        statuses.add("Принят клиентом");
        ComboBox status = new ComboBox("Выберите статус заказа", statuses);
        status.setSizeFull();
        status.setNullSelectionAllowed(false);
        status.setValue(grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Статус").getValue().toString());

        //Создаем кнопки
        Button save = new Button("ОК");
        Button close = new Button("Отменить");

        //Назначаем кнопкам действия
        close.addClickListener(clickEvent -> window.close());
        save.addClickListener(clickEvent ->
                {
                    //Валидируем поля
                    description.validate();
                    client.validate();
                    dateOfCreation.validate();
                    dateOfCompletionOfWork.validate();
                    cost.validate();
                    status.validate();

                    //Добавляем измененный заказ
                    try {
                        DBConnection.startConnection();
                        Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).
                                getItemProperty("ID").getValue();
                        Order order = new Order(description.getValue(), Long.parseLong(client.getValue().toString()),
                                dateOfCreation.getValue(), dateOfCompletionOfWork.getValue(),
                                Double.parseDouble(cost.getValue()), status.getValue().toString(), id);
                        DBOrder.updateOrder(order);
                        DBConnection.closeConnection();
                        grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").setValue(description.getValue());
                        grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Клиент").setValue(client.getValue());
                        grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Дата создания").setValue(new java.sql.Date(dateOfCreation.getValue().getTime()).toString());
                        grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Дата окончания").setValue(new java.sql.Date(dateOfCompletionOfWork.getValue().getTime()).toString());
                        grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Стоимость").setValue(Double.parseDouble(cost.getValue()));
                        grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Статус").setValue(status.getValue());
                        window.close();
                    } catch (java.lang.NullPointerException e) {
                        Notification.show("Заполните все поля");
                    }
                }
        );

        //Добавляем элементы на слои
        stringLayout.addComponent(description);
        stringLayout.addComponent(client);
        stringLayout.addComponent(cost);
        stringLayout.addComponent(status);
        dateLayout.addComponent(dateOfCreation);
        dateLayout.addComponent(dateOfCompletionOfWork);
        buttonsLayout.addComponent(save);
        buttonsLayout.setComponentAlignment(save, Alignment.BOTTOM_LEFT);
        buttonsLayout.addComponent(close);
        buttonsLayout.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);

        //Добавляем слои на главный слой
        clientUpdateLayout.addComponent(stringLayout);
        clientUpdateLayout.addComponent(dateLayout);
        clientUpdateLayout.addComponent(buttonsLayout);

        //Добавляем главный слой в окно
        window.setContent(clientUpdateLayout);
        return window;
    }

    //Создаем  панель фильтра с полями "Клиент", "Статус", "Описание" и кнопкой "Применить"
    public FormLayout orderFilter(Grid grid) {

        //Главный слой фильтра
        FormLayout filterLayout = new FormLayout();

        //Поля ввода информации для фильтрации
        TextField clientID = new TextField("ID Клиента", "");
        clientID.setWidth("200");
        clientID.setInputPrompt("Фильтр");
        TextField status = new TextField("Статус", "");
        status.setWidth("200");
        status.setInputPrompt("Фильтр");
        TextField description = new TextField("Описание", "");
        description.setInputPrompt("Фильтр");
        description.setWidth("200");


        //Кнопка "Применить"
        Button applyFilter = new Button("Применить");


        //Добавляем кнопке действие
        applyFilter.addClickListener(clickEvent -> {

            //Создаем фильтр
            Container.Filterable filterable = (Container.Filterable) grid.getContainerDataSource();
            filterable.removeAllContainerFilters();

            //Добавляем в фильтр информацию
            Container.Filter filterclienID = new SimpleStringFilter("Клиент",
                    clientID.getValue().toString(), true, false);
            filterable.addContainerFilter(filterclienID);
            Container.Filter filterDescription = new SimpleStringFilter("Описание",
                    description.getValue().toString(), true, false);
            filterable.addContainerFilter(filterDescription);
            Container.Filter filterStatus = new SimpleStringFilter("Статус",
                    status.getValue().toString(), true, false);
            filterable.addContainerFilter(filterStatus);
        });

        filterLayout.addComponent(clientID);
        filterLayout.addComponent(description);
        filterLayout.addComponent(status);
        filterLayout.addComponent(applyFilter);
        return filterLayout;
    }

}
