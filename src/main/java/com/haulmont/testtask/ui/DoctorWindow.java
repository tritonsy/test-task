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
import com.haulmont.testtask.database.DoctorDao;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Класс реализует окно для добавления и редактирования
 * докторов.
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
class DoctorWindow extends Window {

    private Grid<Doctor> grid;
    private boolean edit;
    private Button okButton;
    private Button cancelButton;
    private TextField nameText;
    private TextField surnameText;
    private TextField patronymicText;
    private TextField specializationText;

    private Doctor doctor;

    private static Logger logger = Logger.getLogger(DoctorWindow.class.getName());

    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    /**
     * Конструктор окна изменения/добавления докторов
     *
     * @param grid таблица, с которой необходимо работать
     * @param edit флаг, определяющий какое действие нужно совершить
     *             (edit = true - изменить, edit = false - добавить)
     */
    DoctorWindow(Grid<Doctor> grid, boolean edit) {
        this.grid = grid;
        this.edit = edit;
        buildDoctorWindow();
        setupListeners();
    }

    private void buildDoctorWindow() {
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("450px");
        setHeight("360px");
        setModal(true);
        setResizable(false);
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);

        surnameText = new TextField("Фамилия");
        surnameText.setMaxLength(32);
        surnameText.setWidth("100%");
        surnameText.setRequiredIndicatorVisible(true);
        binder.forField(surnameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите фамилию.")
                .asRequired()
                .bind(Doctor::getSurname, Doctor::setSurname);

        nameText = new TextField("Имя");
        nameText.setMaxLength(32);
        nameText.setWidth("100%");
        nameText.setRequiredIndicatorVisible(true);
        binder.forField(nameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите имя.")
                .asRequired()
                .bind(Doctor::getName, Doctor::setName);

        patronymicText = new TextField("Отчество");
        patronymicText.setMaxLength(32);
        patronymicText.setWidth("100%");
        patronymicText.setRequiredIndicatorVisible(true);
        binder.forField(patronymicText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите отчество.")
                .asRequired()
                .bind(Doctor::getPatronymic, Doctor::setPatronymic);

        specializationText = new TextField("Специализация");
        specializationText.setMaxLength(32);
        specializationText.setWidth("100%");
        specializationText.setRequiredIndicatorVisible(true);
        binder.forField(specializationText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите специализацию.")
                .asRequired()
                .bind(Doctor::getSpecialization, Doctor::setSpecialization);

        formLayout.addComponents(surnameText, nameText, patronymicText, specializationText);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        okButton = new Button("ОК");
        okButton.setWidth("125px");
        cancelButton = new Button("Отменить");
        cancelButton.setWidth("125px");

        buttonsLayout.addComponents(okButton, cancelButton);

        mainLayout.addComponents(formLayout, buttonsLayout);
        mainLayout.setExpandRatio(formLayout, 1f);
        mainLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        setContent(mainLayout);
    }

    private void setupListeners() {
        if (edit) {
            setCaption("Редактирование доктора");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    doctor = grid.asSingleSelect().getValue();
                    binder.setBean(doctor);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление доктора");
            surnameText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    Doctor doctorToWrite = new Doctor();
                    doctorToWrite.setName(nameText.getValue());
                    doctorToWrite.setSurname(surnameText.getValue());
                    doctorToWrite.setPatronymic(patronymicText.getValue());
                    doctorToWrite.setSpecialization(specializationText.getValue());
                    DoctorDao doctorDao = DaoFactory.getInstance().getDoctorDao();
                    if (edit) {
                        doctorDao.update(doctor);
                    } else {
                        doctorDao.persist(doctorToWrite);
                    }
                    List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
                    grid.setItems(doctors);
                } catch (DaoException e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });

        cancelButton.addClickListener(clickEvent ->
                close());
    }
}