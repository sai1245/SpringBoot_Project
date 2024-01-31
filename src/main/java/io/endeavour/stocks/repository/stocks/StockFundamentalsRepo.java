package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.vo.TopThreeStockVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockFundamentalsRepo extends JpaRepository<StockFundamentals,String> {

    @Query(name = "StockFundamentals.TopThreeStocks",nativeQuery = true)
    public List<TopThreeStockVO> getTopThreeStocks();
}
