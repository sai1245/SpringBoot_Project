package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockPriceHistory;
import io.endeavour.stocks.entity.stocks.StockPriceHistoryKey;
import io.endeavour.stocks.vo.TradingHistoryForStocksVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, StockPriceHistoryKey> {

    @Query(name = "StockPriceHistory.stocksTradingHistory",nativeQuery = true)
    public List<TradingHistoryForStocksVO> tradingHistoryForStocksList(String tickerSymbol,
                                                                       LocalDate fromDate,
                                                                       LocalDate toDate);
}
