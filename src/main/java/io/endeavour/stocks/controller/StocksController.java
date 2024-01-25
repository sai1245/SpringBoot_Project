package io.endeavour.stocks.controller;

import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.vo.StockPriceHistoryRequestVo;
import io.endeavour.stocks.vo.StocksPriceHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/stocks")
public class StocksController {

    @Autowired
    MarketAnalyticsService marketAnalyticsService;


    @GetMapping("/getSamplePriceHistory")
    public StocksPriceHistoryVo getSamplePriceHistory(){
        StocksPriceHistoryVo stocksPriceHistoryVo = new StocksPriceHistoryVo();
        stocksPriceHistoryVo.setTickerSymbol("V");
        stocksPriceHistoryVo.setTradingDate(LocalDate.now());
        stocksPriceHistoryVo.setOpenPrice(new BigDecimal("253.78"));
        stocksPriceHistoryVo.setClosePrice(new BigDecimal("299.67"));
        stocksPriceHistoryVo.setVolume(123456L);

        return stocksPriceHistoryVo;
    }
    @GetMapping(value = "/getSingleStockPriceHistory/{tickerSymbol}")
    public List<StocksPriceHistoryVo> getSingleStockPriceHistory(@PathVariable(name = "tickerSymbol") String tickerSymbol,
                                                                 @RequestParam(name = "fromDate")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                 @RequestParam(name = "toDate")
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate toDate){

        return marketAnalyticsService.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);
    }

    @PostMapping(value = "/getMultipleStockPriceHistory")
    public List<StocksPriceHistoryVo> getMultipleStockPriceHistory(@RequestBody StockPriceHistoryRequestVo stockPriceHistoryRequestVo){
        return marketAnalyticsService.getMultipleStockPriceHistory(stockPriceHistoryRequestVo.getTickersList(),
                stockPriceHistoryRequestVo.getFromDate(),
                stockPriceHistoryRequestVo.getToDate());
    }


}
