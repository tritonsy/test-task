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
import com.haulmont.testtask.entity.Receipt;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.Renderer;

import java.util.List;
import java.util.logging.Logger;

/**
 * Класс реализует отображение информации о рецептах
 * в таблице (grid).
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public class ReceiptView extends VerticalLayout implements View {

    public static final String NAME = "receipts";

    private Grid<Receipt> receiptGrid = new Grid<>(Receipt.class);

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private TextField descriptionText;
    private TextField patientText;
    private TextField priorityText;

    private static Logger logger = Logger.getLogger(ReceiptView.class.getName());

    /**
     * Конструктор отображения информации о рецептах
     */
    public ReceiptView() {
        buildReceiptView();
        setupListeners();
    }

    private void buildReceiptView() {
        try {
            Panel filterPanel = new Panel("Фильтр");
            HorizontalLayout filterLayout = new HorizontalLayout();
            filterLayout.setMargin(true);
            filterLayout.setSpacing(true);

            descriptionText = new TextField();
            priorityText = new TextField();
            patientText = new TextField();
            descriptionText.setPlaceholder("описание");
            priorityText.setPlaceholder("приоритет");
            patientText.setPlaceholder("пациент");

            filterLayout.addComponents(descriptionText, priorityText, patientText);
            filterPanel.setContent(filterLayout);

            receiptGrid.removeAllColumns();
            receiptGrid.addColumn(Receipt::getDescription).setCaption("Описание");
            receiptGrid.addColumn(receipt -> receipt.getPatient().getSurname() + " " + receipt.getPatient().getName())
                    .setCaption("Пациент");
            receiptGrid.addColumn(receipt -> receipt.getDoctor().getSurname() + " " + receipt.getDoctor().getName())
                    .setCaption("Доктор");
            receiptGrid.addColumn(Receipt::getCreationDate)
                    .setRenderer((Renderer) new DateRenderer("%1$td.%1$tm.%1$tY"))
                    .setCaption("Дата создания");
            receiptGrid.addColumn(Receipt::getExpireDate)
                    .setRenderer((Renderer) new DateRenderer("%1$td.%1$tm.%1$tY"))
                    .setCaption("Дата истечения срока");
            receiptGrid.addColumn(Receipt::getPriority).setCaption("Приоритет");
            receiptGrid.setSizeFull();

            receiptGrid.setSizeFull();

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
            addComponents(filterPanel, receiptGrid, buttonsLayout);
            setExpandRatio(receiptGrid, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            receiptGrid.addSelectionListener(valueChangeEvent -> {
                if (!receiptGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new ReceiptWindow(receiptGrid, false)));

            editButton.addClickListener(clickEvent -> {
                getUI().addWindow(new ReceiptWindow(receiptGrid, true));
            });

            deleteButton.addClickListener(clickEvent -> {
                if (!receiptGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getReceiptDao().delete(receiptGrid.asSingleSelect().getValue());
                        updateGrid();
                    } catch (DaoException e) {
                        logger.severe(e.getMessage());
                    }
                }
            });

            patientText.addValueChangeListener(this::onFilterChange);

            priorityText.addValueChangeListener(this::onFilterChange);

            descriptionText.addValueChangeListener(this::onFilterChange);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Метод для фильтрации таблицы в зависимости от введенных в поля (пациент, описание, приоритет)
     * информации
     *
     * @param event параметр, проверямый на изменение введенной информации
     */
    private void onFilterChange(HasValue.ValueChangeEvent<String> event) {
        try {
            ListDataProvider<Receipt> dataProvider = (ListDataProvider<Receipt>) receiptGrid.getDataProvider();
            dataProvider.setFilter((item) -> {
                boolean patientFilter = (item.getPatient().getSurname() + " " + item.getPatient()
                        .getName())
                        .toLowerCase()
                        .contains(patientText.getValue().toLowerCase());
                boolean descriptionFilter = item.getDescription()
                        .toLowerCase()
                        .contains(descriptionText.getValue().toLowerCase());
                boolean priorityFilter = item.getPriority()
                        .toLowerCase()
                        .contains(priorityText.getValue().toLowerCase());
                return patientFilter && descriptionFilter && priorityFilter;
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGrid() {
        try {
            List<Receipt> receipts = DaoFactory.getInstance().getReceiptDao().getAll();
            receiptGrid.setItems(receipts);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}