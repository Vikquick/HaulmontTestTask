package com.haulmont.testtask.UI;

import com.haulmont.testtask.DAO.DBClient;
import com.haulmont.testtask.DAO.DBConnection;
import com.haulmont.testtask.Entities.Client;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by Виктор on 11.04.2017.
 */
public class СlientListener {

    public Window clientAddWindow (Grid grid) {


            //Создаем окно добавления клиента
        Window window = new Window("Добавить клиента");
        window.center();
        window.setModal(true);
        window.setWidth("600");
        window.setHeight("600");

            //Создаем слои
        VerticalLayout clientAddLayout = new VerticalLayout();
        clientAddLayout.setMargin(true);
        clientAddLayout.setSizeFull();
        HorizontalLayout fioLayout = new HorizontalLayout();
        HorizontalLayout phoneLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

            //Поле ввода фамилии
        TextField surname = new TextField("Фамилия", "");
        surname.setSizeFull();
        surname.setRequired(true);
        surname.setMaxLength(100);
        surname.setRequiredError("Укажите фамилию");
        surname.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));


            //Поле ввода имени
        TextField name = new TextField("Имя", "");
        name.setSizeFull();
        name.setRequired(true);
        name.setMaxLength(100);
        name.setRequiredError("Укажите имя");
        name.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));


            //Поле ввода отчества
        TextField middleName = new TextField("Отчество", "");
        middleName.setSizeFull();
        middleName.setRequired(true);
        middleName.setMaxLength(100);
        middleName.setRequiredError("Укажите отчество");
        middleName.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));


            //Поле ввода телефона
        TextField phone = new TextField("Номер", "");
        phone.setSizeFull();
        phone.setRequired(true);
        phone.setMaxLength(100);
        phone.setRequiredError("Укажите телефон");
        phone.addValidator(new RegexpValidator("[' '\\-\\+()0-9]{1,100}", true, "Данные некорректны"));


            //Кнопки добавления клиента и отмены
        Button add = new Button("Добавить");
        Button close = new Button("Отмена");

            //Действия кнопок
        close.addClickListener(clickEvent -> window.close());
        add.addClickListener(clickEvent -> {
            try {

                //Валидируем поля ввода
            surname.validate();
            name.validate();
            middleName.validate();
            phone.validate();
                //Добавляем клиента в базу
            DBConnection.startConnection();
            Client client = new Client(surname.getValue(), name.getValue(), middleName.getValue(), phone.getValue());
            DBClient.addClient(client);
                //Выводим таблицу с новым клиентом
            List<Client> clients = DBClient.getClientList();
            int index = clients.size();
            grid.addRow(clients.get(index-1).getId(), clients.get(index - 1).getSurname(), clients.get(index - 1).getName(),
                    clients.get(index - 1).getMiddleName(), clients.get(index - 1).getPhone());
            DBConnection.closeConnection();
            window.close();}
            catch (Validator.InvalidValueException e){Notification.show("Заполните все поля");}

        });


            //Добавляем на слои поля ввода и кнопки
        fioLayout.addComponent(surname);
        fioLayout.addComponent(name);
        fioLayout.addComponent(middleName);
        phoneLayout.addComponent(phone);
        buttonsLayout.addComponent(add);
        buttonsLayout.setComponentAlignment(add, Alignment.BOTTOM_LEFT);
        buttonsLayout.addComponent(close);
        buttonsLayout.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);

            //Добавляем все на главный слой
        clientAddLayout.addComponent(fioLayout);
        clientAddLayout.addComponent(phoneLayout);
        clientAddLayout.addComponent(buttonsLayout);
            //Добавляем главный слой в окно
        window.setContent(clientAddLayout);
            //Возвращаем результат
        return window;
    }


        //Метод удаления клиента
    void deleteClient(Grid grid) {
            //Если строка не пустая, то удаляем
       if (grid.getSelectedRow()==null)
       {    Notification.show("Выберите клиента для удаления");}
       else
           {try
                {       //Запрос к базе на удаление
                    DBConnection.startConnection();
                    int res = DBClient.deleteClient((Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).
                    getItemProperty("ID").getValue());
                    DBConnection.closeConnection();
                            //Если у клиента заказа нет, то удаляем
                        if (res == 0)
                        {grid.getContainerDataSource().removeItem(grid.getSelectedRow());}
                            //Если у клиента есть заказ, то выводим сообщение
                        else if(res == 1){Notification.show("Для этого клиента существует заказ");}
                }
                //Если выбрали кнопку или вообще что то непонятное, нас оповестят :)
           catch (java.lang.NullPointerException e){Notification.show("Выберите клиента для удаления");

       }

    }}

        //Окно изменения клиента
public Window clientChangeWindow (Grid grid)

    {       //Создаем окно
        Window window = new Window("Изменить клиента");
        window.center();
        window.setModal(true);
        window.setWidth("600");
        window.setHeight("600");

        //Создаем слои
    VerticalLayout clientAddLayout = new VerticalLayout();
    clientAddLayout.setMargin(true);
    clientAddLayout.setSizeFull();
    HorizontalLayout fioLayout = new HorizontalLayout();
    HorizontalLayout phoneLayout = new HorizontalLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();


            //Поле ввода фамилии (Исходные данные внутри)
        TextField surname = new TextField("Фамилия", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Фамилия").getValue().toString());
        surname.setSizeFull();
        surname.setRequired(true);
        surname.setMaxLength(100);
        surname.setRequiredError("Укажите фамилию");
        surname.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));

            //Поле ввода имени (Исходные данные внутри)
        TextField name = new TextField("Имя", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Имя").getValue().toString());
        name.setSizeFull();
        name.setRequired(true);
        name.setMaxLength(100);
        name.setRequiredError("Укажите имя");
        name.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));

            //Поле ввода отчества (Исходные данные внутри)
        TextField middleName = new TextField("Отчество", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Отчество").getValue().toString());
        middleName.setSizeFull();
        middleName.setRequired(true);
        middleName.setMaxLength(100);
        middleName.setRequiredError("Укажите отчество");
        middleName.addValidator(new RegexpValidator("[' 'а-яА-Яa-zA-ZЁё]{1,100}", true, "Данные некорректны"));

            //Поле ввода телефона (Исходные данные внутри)
        TextField phone = new TextField("Телефон", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Телефон").getValue().toString());
        phone.setSizeFull();
        phone.setRequired(true);
        phone.setMaxLength(100);
        phone.setRequiredError("Укажите телефон");
        phone.addValidator(new RegexpValidator("[' '\\-\\+()0-9]{1,100}", true, "Данные некорректны"));

        //Создаем кнопки
    Button save = new Button("ОК");
    Button close = new Button("Отменить");

        //Назначаем кнопкам действия
    close.addClickListener(clickEvent -> window.close());
    save.addClickListener(clickEvent ->
            {
                try {

                    //Валидируем поля ввода
                    surname.validate();
                    name.validate();
                    middleName.validate();
                    phone.validate();

                    //Изменяем клиента в базе
                DBConnection.startConnection();
                Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).
                        getItemProperty("ID").getValue();
                Client client = new Client(surname.getValue(), name.getValue(), middleName.getValue(), phone.getValue(), id);
                DBClient.updateClient(client);
                DBConnection.closeConnection();

                    //Обновляем таблицу
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Фамилия").setValue(surname.getValue());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Имя").setValue(name.getValue());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Отчество").setValue(middleName.getValue());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Телефон").setValue(phone.getValue());
                window.close();

            }
            catch (Validator.InvalidValueException e)
            {Notification.show("Заполните все поля");}
            });

            //Добавляем поля ввода и кнопки на слои
        fioLayout.addComponent(surname);
        fioLayout.addComponent(name);
        fioLayout.addComponent(middleName);
        phoneLayout.addComponent(phone);
        buttonsLayout.addComponent(save);
        buttonsLayout.setComponentAlignment(save, Alignment.BOTTOM_LEFT);
        buttonsLayout.addComponent(close);
        buttonsLayout.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);

            //Добавляем все на главный слой
        clientAddLayout.addComponent(fioLayout);
        clientAddLayout.addComponent(phoneLayout);
        clientAddLayout.addComponent(buttonsLayout);
            //Добавляем главный слой в окно
        window.setContent(clientAddLayout);
return window;
}


}