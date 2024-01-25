package io.endeavour.stocks.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class StockPriceHistoryRequestVo {


    private List<String> tickersList;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    public List<String> getTickersList() {
        return tickersList;
    }

    public void setTickersList(List<String> tickersList) {
        this.tickersList = tickersList;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "StockPriceHistoryRequestVo{" +
                "tickersList=" + tickersList +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockPriceHistoryRequestVo that = (StockPriceHistoryRequestVo) o;
        return Objects.equals(tickersList, that.tickersList) && Objects.equals(fromDate, that.fromDate) && Objects.equals(toDate, that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickersList, fromDate, toDate);
    }
}
