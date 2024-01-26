package io.endeavour.stocks.controller;

import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.vo.StockFundamentalsWithnamesVo;
import io.endeavour.stocks.vo.StockPriceHistoryRequestVo;
import io.endeavour.stocks.vo.StocksPriceHistoryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/stocks")
public class StocksController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StocksController.class);

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
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate toDate,
                                                                 @RequestParam(name = "sortField") Optional<String> sortFieldOptional,
                                                                 @RequestParam(name = "sortDirection") Optional<String> sortDirectionOptional){


        if (fromDate.isAfter(toDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"from date cannot be greater than to date");
        }
        System.out.println("From the request , the parameter Ticker symbol is " +tickerSymbol+
                " and teh from date is "+fromDate+
                " and the todate is "+toDate);

        LOGGER.debug("From the request the tickersymbol is : {}, fromdate :{} , todate :{}",tickerSymbol,fromDate,toDate);

        return marketAnalyticsService.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate,sortFieldOptional,sortDirectionOptional);
    }

    @PostMapping(value = "/getMultipleStockPriceHistory")
    public List<StocksPriceHistoryVo> getMultipleStockPriceHistory(@RequestBody StockPriceHistoryRequestVo stockPriceHistoryRequestVo){

        LOGGER.info("valyes received for the HTTP request are :{} , fromdate :{}, todate : {}",
                stockPriceHistoryRequestVo.getTickersList(),
                stockPriceHistoryRequestVo.getFromDate(),
                stockPriceHistoryRequestVo.getToDate());
        return marketAnalyticsService.getMultipleStockPriceHistory(stockPriceHistoryRequestVo.getTickersList(),
                stockPriceHistoryRequestVo.getFromDate(),
                stockPriceHistoryRequestVo.getToDate());
    }


    @GetMapping(value = "/getAllStockFundamentalsJdbc")
    public List<StockFundamentalsWithnamesVo> getAllStockFundamentalsJdbc(){

        LOGGER.debug("In the getAllStockFundamentalsJdbc() method of the class {}",getClass());
        return marketAnalyticsService.getAllStockFundamentals();
    }

    @ExceptionHandler({IllegalArgumentException.class, SQLException.class, NullPointerException.class})
    public ResponseEntity generateExceptionResponce(Exception e){
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
