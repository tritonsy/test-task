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
import com.haulmont.testtask.entity.Receipt;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Window;

import java.util.List;
import java.util.logging.Logger;

/**
 * Класс реализует окно для отображения
 * статистики докторов
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
class DoctorStatisticWindow extends Window {

    private static Logger logger = Logger.getLogger(DoctorWindow.class.getName());

    /**
     * Конструктор окна для отображения статистики докторов
     */
    DoctorStatisticWindow() {
        buildDoctorStatisticWindow();
    }

    private void buildDoctorStatisticWindow() {
        setCaption("Статистика");

        Grid<Doctor> statGrid = new Grid<Doctor>();
        statGrid.setSizeFull();

        statGrid.removeAllColumns();
        statGrid.addColumn(doctor -> doctor.getSurname() + " " + doctor.getName()).setCaption("Доктор");
        statGrid.addColumn(doctor -> {
            int count = 0;
            try {
                List<Receipt> receipts = DaoFactory.getInstance().getReceiptDao().getAll();
                count = 0;
                for (Receipt receipt : receipts) {
                    if (receipt.getDoctor().equals(doctor)) {
                        count++;
                    }
                }
            } catch (DaoException e) {
                logger.severe(e.getMessage());
            }
            return count;
        }).setCaption("Количество рецептов");
        try {
            statGrid.setItems(DaoFactory.getInstance().getDoctorDao().getAll());
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("480px");
        setHeight("480px");
        setModal(true);
        setResizable(false);
        setContent(statGrid);
        center();
    }
}
