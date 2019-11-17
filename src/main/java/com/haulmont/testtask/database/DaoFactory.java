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

package com.haulmont.testtask.database;

import com.haulmont.testtask.dao.DaoException;

import java.sql.SQLException;

/**
 * Класс, реализующий шаблон DAO factory
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public class DaoFactory {

    private static DaoFactory instance = null;

    private DaoFactory() {
    }

    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public DoctorDao getDoctorDao() throws DaoException {
        try {
            return new DoctorDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public PatientDao getPatientDao() throws DaoException {
        try {
            return new PatientDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public ReceiptDao getReceiptDao() throws DaoException {
        try {
            return new ReceiptDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public void releaseResources() throws DaoException {
        try {
            DatabaseHelper.closeConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
