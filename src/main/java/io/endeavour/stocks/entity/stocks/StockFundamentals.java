package io.endeavour.stocks.entity.stocks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.endeavour.stocks.vo.TopStockBySectorVO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "stock_fundamentals",schema = "endeavour")
@NamedNativeQuery(name = "StockFundamentals.TopStockBySector",query = """
        with MKTCP_BY_RANK as (
        select
        	sf.sector_id ,
        	sl2.sector_name ,
        	sf.ticker_symbol,
        	sl.ticker_name ,
        	sf.market_cap ,
        	rank() over (partition by sf.sector_id order by sf.market_cap) as MarketCap_Rank
        from
            endeavour.stock_fundamentals sf,
            endeavour.stocks_lookup sl,
            endeavour.sector_lookup sl2
        where
            sf.ticker_symbol =sl.ticker_symbol and
            sf.sector_id =sl2.sector_id  and
        	sf.market_cap is not null
         )
         select
         	mts.sector_id,
         	mts.sector_name,
         	mts.ticker_symbol,
         	mts.ticker_name,
         	mts.market_cap
         from
            MKTCP_BY_RANK mts
         where
          MarketCap_Rank=1
        """,resultSetMapping = "StockFundamentals.TopStockBySectorMapping")

@SqlResultSetMapping(name = "StockFundamentals.TopStockBySectorMapping",
        classes = @ConstructorResult(targetClass = TopStockBySectorVO.class, columns ={
                @ColumnResult(name = "sector_id",type = int.class),
                @ColumnResult(name = "sector_name",type = String.class),
                @ColumnResult(name = "ticker_symbol",type = String.class),
                @ColumnResult(name = "ticker_name",type = String.class),
                @ColumnResult(name = "market_cap",type = BigDecimal.class)
        })
)
public class StockFundamentals {
    @Column(name = "ticker_symbol")
    @Id
    private String tickerSymbol;
    @Column(name = "market_cap")
    private BigDecimal marketCap;
    @Column(name = "current_ratio")
    private BigDecimal currentRatio;

    @OneToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "sector_id")
    private SectorLookup sectorLookup;

    @OneToOne
    @JoinColumn(name = "subsector_id", referencedColumnName = "subsector_id")
    private SubSectorLookup subSectorLookup;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "ticker_symbol")
    private StocksLookup stocksLookup;

//    @OneToOne
//    @JoinColumn(name = "ticker_symbol", referencedColumnName = "ticker_symbol")
//    @MapsId
//    public StocksLookup stocksLookup;

    public int getSectorId(){
        return sectorLookup.getSectorID();
    }

    public String getSectorName(){
        return sectorLookup.getSectorName();
    }

    public int getSubSectorId(){
        return subSectorLookup.getSubSectorID();
    }

    public String getSubSectorName(){
        return subSectorLookup.getSubSectorName();
    }
    public String getTickername(){
        return stocksLookup.getTickerName();
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    @JsonIgnore
    public SectorLookup getSectorLookup() {
        return sectorLookup;
    }

    public void setSectorLookup(SectorLookup sectorLookup) {
        this.sectorLookup = sectorLookup;
    }

    @JsonIgnore
    public SubSectorLookup getSubSectorLookup() {
        return subSectorLookup;
    }

    public void setSubSectorLookup(SubSectorLookup subSectorLookup) {
        this.subSectorLookup = subSectorLookup;
    }


    @JsonIgnore
    public StocksLookup getStocksLookup() {
        return stocksLookup;
    }

    public void setStocksLookup(StocksLookup stocksLookup) {
        this.stocksLookup = stocksLookup;
    }
}