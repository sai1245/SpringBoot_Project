package io.endeavour.stocks.entity.stocks;

import io.endeavour.stocks.vo.TradingHistoryForStocksVO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "stocks_price_history",schema = "endeavour")
@IdClass(value = StockPriceHistoryKey.class)
@NamedNativeQuery(name = "StockPriceHistory.stocksTradingHistory", query = """
                    select
                        sf.market_cap,
                        sf.current_ratio,
                        sf.ticker_symbol,
                        sl.ticker_name,
                        sph.trading_date,
                        sph.close_price,
                        sph.volume
                    from
                        endeavour.stock_fundamentals sf,
                        endeavour.stocks_price_history sph,
                        endeavour.stocks_lookup sl
                    where
                        sf.ticker_symbol=sph.ticker_symbol and
                        sl.ticker_symbol=sph.ticker_symbol and
                        sph.ticker_symbol = (:tickerSymbol)
                        and sph.trading_date between :fromDate and :toDate
                    """,resultSetMapping = "StockPriceHistory.stocksTradingHistoryMapping")
@SqlResultSetMapping(name = "StockPriceHistory.stocksTradingHistoryMapping",
        classes = @ConstructorResult(targetClass = TradingHistoryForStocksVO.class, columns = {
                @ColumnResult(name = "market_cap",type = BigDecimal.class),
                @ColumnResult(name = "current_ratio",type = BigDecimal.class),
                @ColumnResult(name = "ticker_symbol",type = String.class),
                @ColumnResult(name = "ticker_name",type = String.class),
                @ColumnResult(name = "trading_date",type = LocalDate.class),
                @ColumnResult(name = "close_price",type = BigDecimal.class),
                @ColumnResult(name = "volume",type = Long.class)
        })
)
public class StockPriceHistory {

    @Column(name = "ticker_symbol")
    @Id
    private String tickerSymbol;
    @Column(name = "trading_date")
    @Id
    private LocalDate tradingDate;
    @Column(name = "open_price")
    private BigDecimal openPrice;
    @Column(name = "close_price")
    private BigDecimal closePrice;
    @Column(name = "high_price")
    private BigDecimal highPrice;
    @Column(name = "volume")
    private Long volume;

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockPriceHistory that = (StockPriceHistory) o;
        return Objects.equals(tickerSymbol, that.tickerSymbol) && Objects.equals(tradingDate, that.tradingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol, tradingDate);
    }
}
