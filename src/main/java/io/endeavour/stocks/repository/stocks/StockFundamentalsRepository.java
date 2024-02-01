package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.vo.TopStockBySectorVO;
import io.endeavour.stocks.vo.TopThreeStockVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockFundamentalsRepository extends JpaRepository<StockFundamentals,String> {

    @Query(name = "StockFundamentals.TopStockBySector",nativeQuery = true)
    public List<TopStockBySectorVO> getAllTopStocksBySector();


    @Query(name = "StockFundamentals.TopThreeStocks",nativeQuery = true)
    public List<TopThreeStockVO> getTopThreeStocks();


    @Query(nativeQuery = true,value = """
            select
              	*
              from
              	endeavour.stock_fundamentals sf
              where
              	sf.market_cap is not null
             order by market_cap desc
             limit :number
            """)
    public List<StockFundamentals> getTopNStocksNativeSQL(@Param(value = "number") Integer number);

    @Query(value = """
            select
                sf
            from
                StockFundamentals sf
            where
                sf.currentRatio is not null
            order by sf.currentRatio desc
            """)
    public List<StockFundamentals> getNotNullCurrentRatios();

}
