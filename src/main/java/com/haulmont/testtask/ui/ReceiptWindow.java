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
import com.haulmont.testtask.database.ReceiptDao;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Receipt;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Класс реализует окно для добавления и редактирования
 * рецептов.
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
class ReceiptWindow extends Window {

    private Grid<Receipt> grid;
    private boolean edit;
    private Button okButton;
    private Button cancelButton;
    private TextArea descriptionText;
    private ComboBox<Patient> patientComboBox;
    private ComboBox<Doctor> doctorComboBox;
    private DateField creationDateField;
    private DateField expireDateField;
    private ComboBox<String> priorityComboBox;

    private Binder<Receipt> binder = new Binder<>(Receipt.class);

    private Receipt receipt;

    private static Logger logger = Logger.getLogger(PatientWindow.class.getName());

    /**
     * Конструктор окна изменения/добавления рецептов
     *
     * @param grid таблица, с которой необходимо работать
     * @param edit флаг, определяющий какое действие нужно совершить
     *             (edit = true - изменить, edit = false - добавить)
     */
    ReceiptWindow(Grid<Receipt> grid, boolean edit) {
        this.grid = grid;
        this.edit = edit;
        buildReceiptWindow();
        fillPatientsComboBox();
        fillDoctorsComboBox();
        fillPriorityComboBox();
        setupListeners();
    }

    private void buildReceiptWindow() {
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("450px");
        setHeight("500px");
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

        descriptionText = new TextArea("Описание");
        descriptionText.setMaxLength(140);
        descriptionText.setWidth("100%");
        binder.forField(descriptionText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите описание.")
                .asRequired()
                .bind(Receipt::getDescription, Receipt::setDescription);

        patientComboBox = new ComboBox<Patient>("Пациент");
        patientComboBox.setTextInputAllowed(false);
        patientComboBox.setPlaceholder("Выберите пациента");
        patientComboBox.setWidth("100%");
        binder.forField(patientComboBox)
                .withValidator(Objects::nonNull, "Пожалуйста, выберите пациента.")
                .asRequired()
                .bind(Receipt::getPatient, Receipt::setPatient);

        doctorComboBox = new ComboBox<Doctor>("Доктор");
        doctorComboBox.setTextInputAllowed(false);
        doctorComboBox.setPlaceholder("Выберите доктора");
        doctorComboBox.setWidth("100%");
        binder.forField(doctorComboBox)
                .withValidator(Objects::nonNull, "Пожалуйста, выберите доктора.")
                .asRequired()
                .bind(Receipt::getDoctor, Receipt::setDoctor);

        creationDateField = new DateField("Дата создания");
        creationDateField.setDateFormat("dd.MM.yyyy");
        creationDateField.setPlaceholder("дд.мм.гггг");
        creationDateField.setTextFieldEnabled(false);
        creationDateField.setWidth("100%");
        //Дата конвертируется из LocalDate в Date, для корректной записи в БД
        binder.forField(creationDateField)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .withValidator(Objects::nonNull, "Пожалуйста, выберите дату создания.")
                .asRequired()
                .bind(Receipt::getCreationDate, Receipt::setCreationDate);

        expireDateField = new DateField("Дата окончания срока");
        expireDateField.setDateFormat("dd.MM.yyyy");
        expireDateField.setPlaceholder("дд.мм.гггг");
        expireDateField.setTextFieldEnabled(false);
        expireDateField.setWidth("100%");
        //Дата конвертируется из LocalDate в Date, для корректной записи в БД
        binder.forField(expireDateField)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .withValidator(Objects::nonNull, "Пожалуйста, выберите дату окончания срока.")
                .asRequired()
                .bind(Receipt::getExpireDate, Receipt::setExpireDate);

        priorityComboBox = new ComboBox<String>("Приоритет");
        priorityComboBox.setTextInputAllowed(false);
        priorityComboBox.setPageLength(5);
        priorityComboBox.setPlaceholder("Выберите приоритет");
        priorityComboBox.setWidth("100%");
        binder.forField(priorityComboBox)
                .withValidator(Objects::nonNull, "Пожалуйста, выберите приоритет.")
                .asRequired()
                .bind(Receipt::getPriority, Receipt::setPriority);

        formLayout.addComponents(patientComboBox, doctorComboBox, creationDateField, expireDateField, priorityComboBox, descriptionText);

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

    private void fillPatientsComboBox() {
        try {
            List<Patient> patients = DaoFactory.getInstance().getPatientDao().getAll();
            patientComboBox.setItems(patients);
            patientComboBox.setItemCaptionGenerator(patient -> patient.getSurname() + " " + patient.getName());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillDoctorsComboBox() {
        try {
            List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
            doctorComboBox.setItems(doctors);
            doctorComboBox.setItemCaptionGenerator(doctor -> doctor.getSurname() + " " + doctor.getName());
        } catch (Exception e) {
            logger.severe(e.getMessage());

        }
    }

    private void fillPriorityComboBox() {
        try {
            List<String> priority = new ArrayList<>();
            priority.add("Нормальный");
            priority.add("Срочный");
            priority.add("Немедленный");
            priorityComboBox.setItems(priority);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        if (edit) {
            setCaption("Редактирование рецепта");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    receipt = grid.asSingleSelect().getValue();
                    binder.setBean(receipt);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление рецепта");
            descriptionText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                if ((Date.from(creationDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .compareTo(Date.from(expireDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) > 0) {
                    Notification notification = new Notification("Дата создания не может быть " +
                            "позже даты окончания срока годности.");
                    notification.setDelayMsec(2000);
                    notification.show(Page.getCurrent());
                } else {
                    try {
                        Receipt receiptToWrite = new Receipt();
                        receiptToWrite.setDescription(descriptionText.getValue());
                        receiptToWrite.setPatient(patientComboBox.getValue());
                        receiptToWrite.setDoctor(doctorComboBox.getValue());
                        receiptToWrite.setCreationDate(Date.from(creationDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        receiptToWrite.setExpireDate(Date.from(expireDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        receiptToWrite.setPriority(priorityComboBox.getValue());
                        ReceiptDao receiptDao = DaoFactory.getInstance().getReceiptDao();
                        if (edit) {
                            try {
                                receiptDao.update(receipt);
                            } catch (Exception e) {
                                logger.severe(e.getMessage());
                            }
                        } else {
                            receiptDao.persist(receiptToWrite);
                        }
                        List<Receipt> receipts = DaoFactory.getInstance().getReceiptDao().getAll();
                        grid.setItems(receipts);
                    } catch (DaoException e) {
                        logger.severe(e.getMessage());
                    }
                    close();
                }
            }
        });
        cancelButton.addClickListener(clickEvent -> close());
    }
}

