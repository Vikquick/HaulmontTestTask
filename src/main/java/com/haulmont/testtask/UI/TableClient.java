package com.haulmont.testtask.UI;

import com.haulmont.testtask.DAO.DBClient;
import com.haulmont.testtask.DAO.DBConnection;
import com.haulmont.testtask.Entities.Client;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by Виктор on 31.03.2017.
 */
public class TableClient {



    public VerticalLayout tableClientLayout(){

            //Название таблицы
        Label desc = new Label("Клиенты");
        desc.setStyleName(ValoTheme.LABEL_HUGE);

            //Заполнение таблицы
        Grid grid = new Grid();
        grid.addColumn("ID", Long.class);
        grid.addColumn("Фамилия", String.class);
        grid.addColumn("Имя", String.class);
        grid.addColumn("Отчество", String.class);
        grid.addColumn("Телефон", String.class);
        DBConnection.startConnection();
        List<Client> clients = DBClient.getClientList();
        int index = clients.size();
        for (int i = 0; i < index; i++) {
            grid.addRow(clients.get(i).getId(), clients.get(i).getSurname(), clients.get(i).getName(),
                    clients.get(i).getMiddleName(), clients.get(i).getPhone());
        }
        grid.setSizeFull();
        DBConnection.closeConnection();


            //Слой с кнопками
        HorizontalLayout buttons = new HorizontalLayout();
            //Создаем кнопки
        Button addClient = new Button("Добавить");
        Button updateClient = new Button("Изменить");
        Button deleteClient = new Button("Удалить");
            //Добавляем кнопки на их слой
        buttons.addComponent(addClient);
        buttons.addComponent(updateClient);
        buttons.addComponent(deleteClient);




            //Создаем главный слой таблицы
        VerticalLayout tableClient = new VerticalLayout();
            //Добавляем на него все компоненты
        tableClient.addComponent(desc);
        tableClient.addComponent(grid);
        tableClient.addComponent(buttons);
        tableClient.setComponentAlignment(buttons,Alignment.BOTTOM_LEFT);
        tableClient.setSizeFull();

                //Устанавливаем действия на кнопки
        addClient.addClickListener(clickEvent -> buttons.getUI().getUI().addWindow(new СlientListener().clientAddWindow(grid)));
        deleteClient.addClickListener(clickEvent -> new СlientListener().deleteClient(grid));
        updateClient.addClickListener(clickEvent -> {
            if (grid.getSelectedRow() != null)
                buttons.getUI().getUI().addWindow(new СlientListener().clientChangeWindow(grid));
            else {Notification.show("Выберите клиента");}
        });
        return tableClient;
    }
}
