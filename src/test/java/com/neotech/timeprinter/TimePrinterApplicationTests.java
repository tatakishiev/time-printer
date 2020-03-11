package com.neotech.timeprinter;

import com.neotech.timeprinter.configuration.DataBaseProperties;
import com.neotech.timeprinter.entity.Date;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class TimePrinterApplicationTests {

    @Autowired
    private CommandLineRunner runner;

    @Autowired
    private DataBaseProperties dataBaseProperties;

    @BeforeEach
    void init() {
        clear();
    }

    @Test
    void run_WhenApplicationWorkSomeTime_ShouldBeRowsInTable() throws Exception {
        runner.run();
        TimeUnit.SECONDS.sleep(1);

		Assert.assertFalse(getAll().isEmpty());
    }

	@Test
	void run_WhenApplicationStarted_ShouldBeRowsInRightOrder() throws Exception {
		runner.run();
		TimeUnit.SECONDS.sleep(5);

		List<Date> original = getAll();
		List<Date> sortedByTimestamp = original.stream()
				.sorted(Comparator.comparing(Date::getTimestamp))
				.collect(Collectors.toList());

		Assert.assertEquals(original, sortedByTimestamp);
	}

    private void clear() {
        try (Connection connection = DriverManager.getConnection(dataBaseProperties.getUrl(), dataBaseProperties.getUserName(), dataBaseProperties.getPassword());
             PreparedStatement ps = connection.prepareStatement("DELETE FROM times")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Date> getAll() {
		List<Date> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dataBaseProperties.getUrl(), dataBaseProperties.getUserName(), dataBaseProperties.getPassword());
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM times");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
				list.add(new Date(rs.getLong(1), rs.getTimestamp(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
