package io.endeavour.stocks.dao;

import io.endeavour.stocks.mapper.PriceHistoryRowMapper;
import io.endeavour.stocks.vo.StocksPriceHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class StockPriceHistoryDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<StocksPriceHistoryVo> getSingleStockPriceHistory(String tickerSymbol, LocalDate fromDate,LocalDate toDate){
        String sqlQuery= """  
                   select
                       sph.ticker_symbol,
                       sph.trading_date,
                       sph.open_price,
                       sph.close_price,
                       sph.volume
                   from
                       endeavour.stocks_price_history sph
                   where
                       sph.ticker_symbol = ?
                       and sph.trading_date between ? and ?
                """;
        Object[] inputParams = new Object[]{tickerSymbol,fromDate,toDate};
        List<StocksPriceHistoryVo> stocksPriceHistoryList = jdbcTemplate.query(sqlQuery, inputParams, new PriceHistoryRowMapper());

        return stocksPriceHistoryList;
    }

    public List<StocksPriceHistoryVo> getMultipleStockPriceHistory(List<String> tickerList, LocalDate fromDate,LocalDate toDate){
        String sqlQuery= """  
                   select
                       sph.ticker_symbol,
                       sph.trading_date,
                       sph.open_price,
                       sph.close_price,
                       sph.volume
                   from
                       endeavour.stocks_price_history sph
                   where
                       sph.ticker_symbol IN (:tickerSymbols)
                       and sph.trading_date between :fromDate and :toDate
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("toDate",toDate);
        mapSqlParameterSource.addValue("tickerSymbols",tickerList);
        mapSqlParameterSource.addValue("fromDate", fromDate);

        List<StocksPriceHistoryVo> stocksPriceHistoryList = namedParameterJdbcTemplate.query(sqlQuery,
                mapSqlParameterSource,
                new PriceHistoryRowMapper());


        return stocksPriceHistoryList;

    }
}
