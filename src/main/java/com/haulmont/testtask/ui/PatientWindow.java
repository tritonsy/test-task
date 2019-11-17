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
import com.haulmont.testtask.database.PatientDao;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Класс реализует окно для добавления и редактирования
 * пациентов.
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
class PatientWindow extends Window {

    private Grid<Patient> grid;
    private boolean edit;
    private Button okButton;
    private Button cancelButton;
    private TextField nameText;
    private TextField surnameText;
    private TextField patronymicText;
    private TextField phoneNumberText;

    private Patient patient;

    private static Logger logger = Logger.getLogger(PatientWindow.class.getName());

    private Binder<Patient> binder = new Binder<>(Patient.class);

    /**
     * Конструктор окна изменения/добавления пациентов
     *
     * @param grid таблица, с которой необходимо работать
     * @param edit флаг, определяющий какое действие нужно совершить
     *             (edit = true - изменить, edit = false - добавить)
     */
    PatientWindow(Grid<Patient> grid, boolean edit) {
        this.grid = grid;
        this.edit = edit;
        buildPatientWindow();
        setupListeners();
    }

    private void buildPatientWindow() {
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
                .bind(Patient::getSurname, Patient::setSurname);

        nameText = new TextField("Имя");
        nameText.setMaxLength(32);
        nameText.setWidth("100%");
        nameText.setRequiredIndicatorVisible(true);
        binder.forField(nameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите имя.")
                .asRequired()
                .bind(Patient::getName, Patient::setSurname);

        patronymicText = new TextField("Отчество");
        patronymicText.setMaxLength(32);
        patronymicText.setWidth("100%");
        patronymicText.setRequiredIndicatorVisible(true);
        binder.forField(patronymicText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите отчество.")
                .asRequired()
                .bind(Patient::getPatronymic, Patient::setPatronymic);

        phoneNumberText = new TextField("Телефон");
        phoneNumberText.setMaxLength(32);
        phoneNumberText.setWidth("100%");
        phoneNumberText.setRequiredIndicatorVisible(true);
        //Валидация номера телефона при помощи регулярного выражения
        binder.forField(phoneNumberText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите номер телефона.")
                .withValidator(new RegexpValidator("Введите корректный номер телефона.", "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"))
                .asRequired()
                .bind(Patient::getPhoneNumber, Patient::setPhoneNumber);

        formLayout.addComponents(surnameText, nameText, patronymicText, phoneNumberText);

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
            setCaption("Редактирование пациента");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    patient = grid.asSingleSelect().getValue();
                    binder.setBean(patient);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление пациента");
            surnameText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    Patient patientToWrite = new Patient();
                    patientToWrite.setSurname(surnameText.getValue());
                    patientToWrite.setName(nameText.getValue());
                    patientToWrite.setPatronymic(patronymicText.getValue());
                    patientToWrite.setPhoneNumber(phoneNumberText.getValue());
                    PatientDao patientDao = DaoFactory.getInstance().getPatientDao();
                    if (edit) {
                        patientDao.update(patient);
                    } else {
                        patientDao.persist(patientToWrite);
                    }
                    List<Patient> patients = DaoFactory.getInstance().getPatientDao().getAll();
                    grid.setItems(patients);
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
