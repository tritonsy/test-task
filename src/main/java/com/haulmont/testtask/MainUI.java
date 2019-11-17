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

package com.haulmont.testtask;

import com.haulmont.testtask.ui.DoctorView;
import com.haulmont.testtask.ui.PatientView;
import com.haulmont.testtask.ui.ReceiptView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

/**
 * Класс представляет из себя входную точку в приложение.
 * Здесь, посредством навигатора, описана логика переходов по страницам.
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
@Theme(AppTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        String title = "Информация поликлиники";
        getPage().setTitle(title);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("48px");
        headerLayout.setWidth("100%");
        headerLayout.setMargin(false);
        headerLayout.setSpacing(true);

        Button patientButton = new Button("Пациенты", clickEvent -> getNavigator().navigateTo(PatientView.NAME));
        patientButton.setHeight("100%");
        patientButton.addStyleName(AppTheme.BORDERLESS);

        Button doctorButton = new Button("Доктора", clickEvent -> getNavigator().navigateTo(DoctorView.NAME));
        doctorButton.setHeight("100%");
        doctorButton.addStyleName(AppTheme.BORDERLESS);

        Button receiptButton = new Button("Рецепты", clickEvent -> getNavigator().navigateTo(ReceiptView.NAME));
        receiptButton.setHeight("100%");
        receiptButton.addStyleName(AppTheme.BORDERLESS);

        Label header = new Label(title);
        header.setWidth(null);

        headerLayout.addComponents(patientButton, doctorButton, receiptButton, header);
        headerLayout.setComponentAlignment(header, Alignment.MIDDLE_RIGHT);
        headerLayout.setExpandRatio(header, 1f);

        VerticalLayout viewsLayout = new VerticalLayout();
        viewsLayout.setSizeFull();
        viewsLayout.setMargin(false);
        viewsLayout.setSpacing(true);

        mainLayout.addComponents(headerLayout, viewsLayout);
        mainLayout.setExpandRatio(viewsLayout, 1f);

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(viewsLayout);
        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addView(DoctorView.NAME, new DoctorView());
        navigator.addView(PatientView.NAME, new PatientView());
        navigator.addView(ReceiptView.NAME, new ReceiptView());

        headerLayout.setStyleName(AppTheme.HEADER_LAYOUT);
        mainLayout.setStyleName(AppTheme.VIEW_LAYOUT);
        viewsLayout.setStyleName(AppTheme.VIEW_LAYOUT);

        setContent(mainLayout);
    }
}
