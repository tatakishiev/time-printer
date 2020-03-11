package com.neotech.timeprinter.repository;

import com.neotech.timeprinter.entity.Date;

import java.util.List;

public interface DateRepository {
    void save(Date date);
    List<Date> findAll();
}
