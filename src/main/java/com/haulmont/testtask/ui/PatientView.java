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
import com.haulmont.testtask.entity.Patient;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Класс реализует отображение информации о пациентах
 * в таблице (grid).
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public class PatientView extends VerticalLayout implements View {

    public static final String NAME = "";

    private Grid<Patient> patientGrid = new Grid<>(Patient.class);

    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    private static Logger logger = Logger.getLogger(PatientView.class.getName());

    /**
     * Конструктор отображения информации о пацинтах
     */
    public PatientView() {
        buildPatientView();
        setupListeners();
    }

    private void buildPatientView() {
        try {
            patientGrid.removeAllColumns();
            patientGrid.addColumn(Patient::getSurname).setCaption("Фамилия");
            patientGrid.addColumn(Patient::getName).setCaption("Имя");
            patientGrid.addColumn(Patient::getPatronymic).setCaption("Отчество");
            patientGrid.addColumn(Patient::getPhoneNumber).setCaption("Телефон");
            patientGrid.setSizeFull();

            HorizontalLayout buttonsLayout = new HorizontalLayout();

            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить", new ThemeResource(AppTheme.BUTTON_ADD));
            editButton = new Button("Изменить", new ThemeResource(AppTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить", new ThemeResource(AppTheme.BUTTON_DELETE));
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(patientGrid, buttonsLayout);
            setExpandRatio(patientGrid, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            patientGrid.addSelectionListener(valueChangeEvent -> {
                if (!patientGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new PatientWindow(patientGrid, false)));

            editButton.addClickListener(clickEvent ->
                    getUI().addWindow(new PatientWindow(patientGrid, true)));

            deleteButton.addClickListener(clickEvent -> {
                if (!patientGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getPatientDao().delete(patientGrid.asSingleSelect().getValue());
                        updateGrid();
                    } catch (DaoException e) {
                        if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                            Notification notification = new Notification("Удаление пациента невозможно, " +
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
            List<Patient> patients = DaoFactory.getInstance().getPatientDao().getAll();
            patientGrid.setItems(patients);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }

}
