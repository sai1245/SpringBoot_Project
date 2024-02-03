package io.endeavour.stocks.controller;

import io.endeavour.stocks.entity.stocks.*;
import io.endeavour.stocks.remote.vo.StocksBySubSectorVO;
import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.vo.*;
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

    private static final Logger LOGGER= LoggerFactory.getLogger(StocksController.class);

    @Autowired
    MarketAnalyticsService marketAnalyticsService;
    @GetMapping("/getSamplePriceHistory")
    public StocksPriceHistoryVO getSamplePriceHistory(){
        StocksPriceHistoryVO stocksPriceHistoryVO= new StocksPriceHistoryVO();
        stocksPriceHistoryVO.setTickerSymbol("V");
        stocksPriceHistoryVO.setTradingDate(LocalDate.now());
        stocksPriceHistoryVO.setOpenPrice(new BigDecimal("154.34"));
        stocksPriceHistoryVO.setClosePrice(new BigDecimal("155.93"));
        stocksPriceHistoryVO.setVolume(890634L);

        return stocksPriceHistoryVO;
    }

    @GetMapping(value ="getSingleStockPriceHistory/{tickerSymbol}" )
    public List<StocksPriceHistoryVO> getSingleStockPriceHistory(@PathVariable(name = "tickerSymbol") String tickerSymbol,
                                                                 @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                 @RequestParam(name = "toDate") @DateTimeFormat(pattern ="yyyy-MM-dd" ) LocalDate toDate,
                                                                 @RequestParam(name = "sortField") Optional<String>  sortFieldOptional,
                                                                 @RequestParam(name = "sortDirection") Optional<String>  sortDirectionOptional){
        if (fromDate.isAfter(toDate)){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"fromDate cannot be greater than toDate");
        }
        System.out.println("From the request, the parameter TickerSymbol: "+tickerSymbol+", fromDate : "+fromDate+", toDate :"+toDate);
        LOGGER.debug("From the request, the parameter TickerSymbol : {}, fromDate : {}, toDate : {}",tickerSymbol,fromDate,toDate);
        return marketAnalyticsService.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate,sortFieldOptional,sortDirectionOptional);
    }

    @PostMapping(value = "/getMultipleStockPriceHistory")
    public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(@RequestBody StockPriceHistoryRequestVO stockPriceHistoryRequestVO){
        LOGGER.info("Values received from the Http Request are : tickerSymbol {}, fromDate {}, toDate {}",
                stockPriceHistoryRequestVO.getTickersList(),stockPriceHistoryRequestVO.getFromDate(),
                stockPriceHistoryRequestVO.getToDate());
        return marketAnalyticsService.getMultipleStockPriceHistory(stockPriceHistoryRequestVO.getTickersList(),
                stockPriceHistoryRequestVO.getFromDate(),
                stockPriceHistoryRequestVO.getToDate());

    }

    @GetMapping(value = "/getAllStockFundamentalsJDBC")
    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsJDBC(){
        LOGGER.debug("In the getAllStockFundamentsaJDBC() method of the class{} ", getClass());
        return marketAnalyticsService.getAllStockFundamentals();
    }

    @PostMapping(value = "/getAllSpecificStocks")
    public List<StockFundamentalsWithNamesVO> getAllSpecificStocks(@RequestBody List<String> tickerSymbols){
        LOGGER.debug("got tickerSymbols from JSON into getAllSpecificStocks controller : {}",tickerSymbols);
        return marketAnalyticsService.getAllStockFundamentalsForSpecificTickerSymbols(tickerSymbols);
    }

    @PostMapping(value = "/getAllSpecificStocksUsingSqlQuery")
    public List<StockFundamentalsWithNamesVO> getAllSpecificStocksUsingSqlQuery(@RequestBody Optional<List<String>> tickerSymbols){
        LOGGER.debug("got tickerSymbols from JSON into getAllSpecificStocksUsingSqlQuery controller : {}",tickerSymbols);
        if (tickerSymbols.isPresent()){
            List<String> tickerList = tickerSymbols.get();
            if (tickerList.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TickersList is empty or not sent");
            }else {
                return marketAnalyticsService.getAllStockFundamentalsForGivenTickerSymbolsWithSQLQuery(tickerList);
            }
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No TickersList empty or not sent");
        }

    }

    @GetMapping(value = "/getAllStockFundamentalsJPA")
    public List<StockFundamentals> getAllStockFundamentalsJPA(){
        return  marketAnalyticsService.getAllStockFundamentalsWithJPA();
    }

//"1) Write a GET API to get Sector and Subsector details from the database using JPA

    @GetMapping(value = "/getSectorsWithSubSectorsList")
    public List<SectorLookup> getSectorsWithSubSectorsList(){
        return marketAnalyticsService.getAllSectorsWithItsSubSectors();
    }

    @GetMapping(value = "/getAllSubSectorsWithSectors")
    public List<SubSectorLookup> getAllSubSectorsWithSectors(){
        return marketAnalyticsService.getAllSubSectorsWithSectors();
    }


    @GetMapping(value = "/getStockPriceHistoryDetails")
    public ResponseEntity<StockPriceHistory>
    getStockPriceHistoryDetails(@RequestParam(value = "tickerSymbol") String tickerSymbol,
                                @RequestParam(value = "tradingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tradingDate){
        return ResponseEntity.of(marketAnalyticsService.getStockPriceHistory(tickerSymbol,tradingDate));
    }

    @GetMapping(value = "/getTopStockBySector")
    public List<TopStockBySectorVO> getTopStockBySector()
    {
        return marketAnalyticsService.getTopStockBySector();
    }

    @GetMapping(value = "/getTopThreeStocks")
    public List<TopThreeStockVO> getTopThreeStocks(){
        return marketAnalyticsService.getTopThreeStocks();
    }

    @GetMapping(value = "/getThreeStocks")
    public List<SectorLookupUpdated> getThreeStocks(){
        return marketAnalyticsService.getThreeStocks();
    }

    @GetMapping(value = "/getTopNStocksNativeSQL/{number}")
    public List<StockFundamentals> getTopNStocksNativeSQL(@PathVariable(value = "number") Integer number){
        return marketAnalyticsService.getTopNStocksNativeSQL(number);
    }

    @GetMapping(value = "/getTopNStocksNativeJPQL/{number}")
    public List<StockFundamentals> getTopNStocksNativeJPQL(@PathVariable(value = "number") Integer number){
        return marketAnalyticsService.getTopNStocksNativeJPQL(number);
    }


    @GetMapping(value = "/getNotNullCurrentRatios")
    public List<StockFundamentals> getNotNullCurrentRatios(){
        return marketAnalyticsService.getNotNullCurrentRatios();
    }

    @GetMapping(value = "/getTopNStocksCriteriaAPI/{number}")
    public List<StockFundamentals> getTopNStocksCriteriaAPI(@PathVariable(value = "number") Integer number){
        return marketAnalyticsService.getTopNStocksCriteriaAPI(number);
    }

    @GetMapping(value = "/tradingHistoryForStocksList")
    public StockHistoryVO tradingHistoryForStocksList(@RequestParam(value = "tickerSymbol") String tickerSymbol,
                                                      @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                      @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate toDate){
        return marketAnalyticsService.tradingHistoryForStocksList(tickerSymbol, fromDate, toDate);
    }


    @GetMapping(value = "getTopNPerformingStocks/{number}")
    public List<StockFundamentalsWithNamesVO> getTopNPerformingStocks(@PathVariable(value = "number") Integer number,
                                                                      @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                      @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
                                                                      @RequestParam(value = "marketCapLimit") Long marketCapLimit){
       return marketAnalyticsService.getTopNPerformingStocks(number, fromDate, toDate, marketCapLimit);
    }

    @GetMapping(value = "getTopNPerformingStocksBySubSector/{number}")
    public List<StocksBySubSectorVO> getTopNPerformingStocksBySubSector(@PathVariable(value = "number") Integer number,
                                                                        @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                        @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate)
    {
        return marketAnalyticsService.getTopNPerformingStocksBySubSector(number, fromDate, toDate);
    }

    @ExceptionHandler({IllegalArgumentException.class, SQLException.class, NullPointerException.class})
    public ResponseEntity generateExceptionResponse(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
