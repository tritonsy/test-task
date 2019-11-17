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

/**
 * Класс содержит константы наименований стилей
 * из apptheme.scss и пути к иконкам для кнопок
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public final class AppTheme {

    static final String THEME_NAME = "apptheme";

    static final String HEADER_LAYOUT = "header-layout";
    static final String VIEW_LAYOUT = "view-layout";
    public static final String MODAL_WINDOW = "modal-window";
    static final String BORDERLESS = "borderless";

    public static final String BUTTON_ADD = "img/button_add.png";
    public static final String BUTTON_STATISTIC = "img/button_statistic.png";
    public static final String BUTTON_EDIT = "img/button_edit.png";
    public static final String BUTTON_DELETE = "img/button_delete.png";

    private AppTheme() {
    }
}
