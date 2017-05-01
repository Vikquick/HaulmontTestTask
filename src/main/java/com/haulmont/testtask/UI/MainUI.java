package com.haulmont.testtask.UI;

import com.haulmont.testtask.DAO.DBClient;
import com.haulmont.testtask.DAO.DBConnection;
import com.haulmont.testtask.Entities.Client;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.hsqldb.Database;

import java.util.ArrayList;
import java.util.List;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
      Label CarService = new Label("Авто-Сервис");
      HorizontalLayout horizontalLayout = new HorizontalLayout(CarService);
        HorizontalLayout tablesLayout = new HorizontalLayout();
        tablesLayout.addComponent(new TableClient().tableClientLayout());
        tablesLayout.addComponent(new TableOrder().tableOrderLayout());
        tablesLayout.setSizeFull();
        tablesLayout.setMargin(true);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(horizontalLayout);
        mainLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);
        mainLayout.addComponent(tablesLayout);
        setContent(mainLayout);





    }
}