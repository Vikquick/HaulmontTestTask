package com.haulmont.testtask.UI;

import com.haulmont.testtask.DAO.DBConnection;
import com.haulmont.testtask.DAO.DBOrder;
import com.haulmont.testtask.Entities.Order;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Виктор on 31.03.2017.
 */
public class TableOrder {
    public VerticalLayout tableOrderLayout() {
        //Название таблицы
        Label desc = new Label("Заказы");
        desc.setStyleName(ValoTheme.LABEL_HUGE);

        //Заполнение таблицы
        Grid grid = new Grid();
        grid.addColumn("ID", Long.class);
        grid.addColumn("Описание", String.class);
        grid.addColumn("Клиент", Long.class);
        grid.addColumn("Дата создания", String.class);
        grid.addColumn("Дата окончания", String.class);
        grid.addColumn("Стоимость", Double.class);
        grid.addColumn("Статус", String.class);
        DBConnection.startConnection();
        ArrayList<Order> clients = DBOrder.getOrderList();
        int index = clients.size();
        for (int i = 0; i < index; i++) {
            grid.addRow(clients.get(i).getId(), clients.get(i).getDescription(), clients.get(i).getClient(),
                    clients.get(i).getDateOfCreation().toString(), clients.get(i).getDateOfCompletionOfWork().toString(),
                    clients.get(i).getCost(), clients.get(i).getStatus());
        }
        DBConnection.closeConnection();
        grid.setSizeFull();


        //Создаем слой с кнопками
        HorizontalLayout buttons = new HorizontalLayout();
        //Создаем кнопки
        Button addOrder = new Button("Добавить");
        Button updateOrder = new Button("Изменить");
        Button deleteOrder = new Button("Удалить");
        //Добавляем кнопки на их слой
        buttons.addComponent(addOrder);
        buttons.addComponent(updateOrder);
        buttons.addComponent(deleteOrder);


        //Создаем слой фильтра
        FormLayout filter = new FormLayout();
        //Добавляем фильтр на его слой
        filter.addComponent(new OrderListener().orderFilter(grid));


        //Создаем главный слой
        VerticalLayout tableOrder = new VerticalLayout();
        //Добавляем на него компоненты
        tableOrder.addComponent(desc);
        tableOrder.addComponent(grid);
        tableOrder.addComponent(buttons);
        tableOrder.addComponent(filter);
        tableOrder.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        //Добавляем действия на кнопки
        addOrder.addClickListener(clickEvent -> buttons.getUI().getUI().addWindow(new OrderListener().orderAddWindow(grid)));
        deleteOrder.addClickListener(clickEvent -> new OrderListener().deleteOrder(grid));
        updateOrder.addClickListener(clickEvent -> {
            if (grid.getSelectedRow() != null)
                buttons.getUI().getUI().addWindow(new OrderListener().orderChangeWindow(grid));
            else {
                Notification.show("Выберите клиента");
            }
        });
        return tableOrder;
    }
}
