package com.neotech.timeprinter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class Date {
    private Long id;
    private Timestamp timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Date)) return false;
        Date that = (Date) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimestamp());
    }
}
