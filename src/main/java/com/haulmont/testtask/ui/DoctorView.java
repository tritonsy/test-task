/*
 * The MIT License
 * Copyright © 2019 Kobzarev Alex
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Класс реализует отображение информации о докторах
 * в таблице (grid).
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public class DoctorView extends VerticalLayout implements View {

    public static final String NAME = "doctors";

    private Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button statisticButton;

    private static Logger logger = Logger.getLogger(DoctorView.class.getName());

    /**
     * Конструктор отображения информации о докторах
     */
    public DoctorView() {
        buildDoctorView();
        setupListeners();
    }

    private void buildDoctorView() {
        try {
            doctorGrid.removeAllColumns();
            doctorGrid.addColumn(Doctor::getSurname).setCaption("Фамилия");
            doctorGrid.addColumn(Doctor::getName).setCaption("Имя");
            doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество");
            doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специальность");
            doctorGrid.setSizeFull();
            doctorGrid.getEditor().setEnabled(false);

            HorizontalLayout buttonsLayout = new HorizontalLayout();

            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить", new ThemeResource(AppTheme.BUTTON_ADD));
            editButton = new Button("Изменить", new ThemeResource(AppTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить", new ThemeResource(AppTheme.BUTTON_DELETE));
            statisticButton = new Button("Показать статистику", new ThemeResource(AppTheme.BUTTON_STATISTIC));
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton, statisticButton);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(doctorGrid, buttonsLayout);
            setExpandRatio(doctorGrid, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            doctorGrid.addSelectionListener(valueChangeEvent -> {
                if (!doctorGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new DoctorWindow(doctorGrid, false)));

            editButton.addClickListener(clickEvent -> {
                getUI().addWindow(new DoctorWindow(doctorGrid, true));
            });

            statisticButton.addClickListener(clickEvent -> {
                getUI().addWindow(new DoctorStatisticWindow());
            });

            deleteButton.addClickListener(clickEvent -> {
                if (!doctorGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getDoctorDao().delete(doctorGrid.asSingleSelect().getValue());
                        updateGrid();
                    } catch (DaoException e) {
                        if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                            Notification notification = new Notification("Удаление доктора невозможно, " +
                                    "так как у него есть активные рецепты");
                            notification.setDelayMsec(2000);
                            notification.show(Page.getCurrent());
                        } else {
                            logger.severe(e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGrid() {
        try {
            List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
            doctorGrid.setItems(doctors);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}