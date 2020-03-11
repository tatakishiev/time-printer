package com.neotech.timeprinter.repository.impl;

import com.neotech.timeprinter.database.ConnectionPool;
import com.neotech.timeprinter.repository.DateRepository;
import com.neotech.timeprinter.entity.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateRepositoryImpl implements DateRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateRepositoryImpl.class);

    private static final String INSERT = "INSERT INTO times(date_time) VALUES(?)";
    private static final String SELECT_ALL = "SELECT id, date_time FROM times";

    private final ConnectionPool connectionPool;

    public DateRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void save(Date date) {
        Connection connection = connectionPool.getConnection();
        while (true) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
                preparedStatement.setTimestamp(1, date.getTimestamp());
                preparedStatement.executeUpdate();
                return;
            } catch (SQLException e) {
                LOGGER.error("Error while execution query. Trying to retry", e);
                connection = connectionPool.getConnection();
            }
        }
    }

    @Override
    public List<Date> findAll() {
        List<Date> dates = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        while (true) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dates.add(new Date(resultSet.getLong(1), resultSet.getTimestamp(2)));
                }
                break;
            } catch (SQLException e) {
                LOGGER.error("Error while execution query. Trying to retry", e);
                dates.clear();
                connection = connectionPool.getConnection();
            }
        }
        return dates;
    }
}
