package io.endeavour.stocks.mapper;

import io.endeavour.stocks.vo.StocksPriceHistoryVo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceHistoryRowMapper implements RowMapper<StocksPriceHistoryVo> {
    @Override
    public StocksPriceHistoryVo mapRow(ResultSet rs, int rowNum) throws SQLException {

        StocksPriceHistoryVo stocksPriceHistoryVo = new StocksPriceHistoryVo();

        stocksPriceHistoryVo.setTickerSymbol(rs.getString("ticker_symbol"));
        stocksPriceHistoryVo.setTradingDate(rs.getDate("trading_date").toLocalDate());
        stocksPriceHistoryVo.setOpenPrice(rs.getBigDecimal("open_price"));
        stocksPriceHistoryVo.setClosePrice(rs.getBigDecimal("close_price"));
        stocksPriceHistoryVo.setVolume(rs.getLong("volume"));


        return stocksPriceHistoryVo;
    }
}
