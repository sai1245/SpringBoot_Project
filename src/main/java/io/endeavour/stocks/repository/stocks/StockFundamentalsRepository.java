package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.vo.TopStockBySectorVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockFundamentalsRepository extends JpaRepository<StockFundamentals,String> {

    @Query(name = "StockFundamentals.TopStockBySector",nativeQuery = true)
    public List<TopStockBySectorVO> getAllTopStocksBySector();
}
