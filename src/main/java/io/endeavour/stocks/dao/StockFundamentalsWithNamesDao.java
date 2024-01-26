package io.endeavour.stocks.dao;

import io.endeavour.stocks.vo.StockFundamentalsWithnamesVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockFundamentalsWithNamesDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockFundamentalsWithNamesDao.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<StockFundamentalsWithnamesVo> getallStocksWithNames() {

        LOGGER.debug("Currently in the method getallStocksWithNames() in the class {}",getClass());
        String sqlQuery = """
                    select
                        sf.ticker_symbol,
                        sl.ticker_name,
                        sf.sector_id,
                        sl1.sector_name,
                        sf.subsector_id,
                        sl2.subsector_name,
                        sf.market_cap,
                        sf.current_ratio\s
                    from
                        endeavour.stock_fundamentals sf,
                        endeavour.stocks_lookup sl,
                        endeavour.sector_lookup sl1,
                        endeavour.subsector_lookup sl2
                    where
                        sf.ticker_symbol = sl.ticker_symbol
                        and sf.sector_id = sl1.sector_id
                        and sf.subsector_id = sl2.subsector_id\s
                """;
        List<StockFundamentalsWithnamesVo> stockFundamentalsWithnamesVoList = namedParameterJdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            StockFundamentalsWithnamesVo stockFundamentalsWithnamesVo = new StockFundamentalsWithnamesVo();
            stockFundamentalsWithnamesVo.setTickerSymbol(rs.getString("ticker_symbol"));
            stockFundamentalsWithnamesVo.setTickerName(rs.getString("ticker_name"));
            stockFundamentalsWithnamesVo.setSectorId(rs.getInt("sector_id"));
            stockFundamentalsWithnamesVo.setSectorName(rs.getString("sector_name"));
            stockFundamentalsWithnamesVo.setSubSectorId(rs.getInt("subsector_id"));
            stockFundamentalsWithnamesVo.setSubSectorName(rs.getString("subsector_name"));
            stockFundamentalsWithnamesVo.setCurrentRatio(rs.getBigDecimal("current_ratio"));
            stockFundamentalsWithnamesVo.setMarketCap(rs.getBigDecimal("market_cap"));

            return stockFundamentalsWithnamesVo;
        });


        return stockFundamentalsWithnamesVoList;
    }
}
