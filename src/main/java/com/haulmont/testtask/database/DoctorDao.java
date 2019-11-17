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
import com.haulmont.testtask.dao.GenericDao;
import com.haulmont.testtask.entity.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с таблицей Доктор из БД.
 * Выполнено по шаблону DAO.
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public class DoctorDao implements GenericDao<Doctor> {

    private Connection connection = null;

    protected DoctorDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Doctor persist(Doctor object) throws DaoException {
        Doctor doctor = new Doctor();
        String sql = "insert into T_DOCTOR (NAME, SURNAME, PATRONYMIC, SPECIALIZATION) values (?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, object.getName());
            st.setString(2, object.getSurname());
            st.setString(3, object.getPatronymic());
            st.setString(4, object.getSpecialization());
            if (st.executeUpdate() == 1) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    rs.next();
                    doctor.setId(rs.getLong(1));
                    doctor.setName(object.getName());
                    doctor.setSurname(object.getSurname());
                    doctor.setPatronymic(object.getPatronymic());
                    doctor.setSpecialization(object.getSpecialization());
                } catch (SQLException e) {
                    throw e;
                }
            } else {
                throw new SQLException("Creating Doctor failed, no row inserted.");
            }
        } catch (SQLException e) {
            doctor = null;
            throw new DaoException(e);
        }
        return doctor;
    }

    @Override
    public void update(Doctor object) throws DaoException {
        String sql = "update T_DOCTOR set NAME = ?, SURNAME = ?, PATRONYMIC = ?, SPECIALIZATION = ? where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, object.getName());
            st.setString(2, object.getSurname());
            st.setString(3, object.getPatronymic());
            st.setString(4, object.getSpecialization());
            st.setLong(5, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Doctor object) throws DaoException {
        String sql = "delete from T_DOCTOR where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Doctor getByPrimaryKey(Long key) throws DaoException {
        Doctor doctor = null;
        String sql = "select NAME, SURNAME, PATRONYMIC, SPECIALIZATION from T_DOCTOR where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, key);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                doctor = new Doctor();
                doctor.setId(key);
                doctor.setName(rs.getString("NAME"));
                doctor.setSurname(rs.getString("SURNAME"));
                doctor.setPatronymic(rs.getString("PATRONYMIC"));
                doctor.setSpecialization(rs.getString("SPECIALIZATION"));
            }
        } catch (SQLException e) {
            doctor = null;
            throw new DaoException(e);
        }
        return doctor;
    }

    @Override
    public List<Doctor> getAll() throws DaoException {
        List<Doctor> list = new ArrayList<Doctor>();
        String sql = "select ID, NAME, SURNAME, PATRONYMIC, SPECIALIZATION from T_DOCTOR;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("ID"));
                doctor.setName(rs.getString("NAME"));
                doctor.setSurname(rs.getString("SURNAME"));
                doctor.setPatronymic(rs.getString("PATRONYMIC"));
                doctor.setSpecialization(rs.getString("SPECIALIZATION"));
                list.add(doctor);
            }
        } catch (SQLException e) {
            list = null;
            throw new DaoException(e);
        }
        return list;
    }
}
