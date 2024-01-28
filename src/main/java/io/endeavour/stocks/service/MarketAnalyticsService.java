package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.repository.stocks.StockFundamentalRepository;
import io.endeavour.stocks.vo.StockFundamentalsWithnamesVo;
import io.endeavour.stocks.vo.StocksPriceHistoryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketAnalyticsService {

   StockFundamentalRepository stockFundamentalRepository;
    StockPriceHistoryDao stockPriceHistoryDao;

    StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao;



    private static final Logger LOGGER = LoggerFactory.getLogger(MarketAnalyticsService.class);

    @Autowired
    public MarketAnalyticsService(StockPriceHistoryDao stockPriceHistoryDao,
                                  StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao,
                                  StockFundamentalRepository stockFundamentalRepository) {
        this.stockPriceHistoryDao = stockPriceHistoryDao;
        this.stockFundamentalsWithNamesDao=stockFundamentalsWithNamesDao;
        this.stockFundamentalRepository=stockFundamentalRepository;
    }

    public List<StocksPriceHistoryVo> getSingleStockPriceHistory(String tickerSymbol,
                                                                 LocalDate fromDate,
                                                                 LocalDate toDate,
                                                                 Optional<String> sortFieldOptional,
                                                                 Optional<String> sortDirectionOptional){


        List<StocksPriceHistoryVo> singleStockPriceHistory = stockPriceHistoryDao.
                getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);
        String fieldToSortBy = sortFieldOptional.orElse("TradingDate");
        String directionToSortBy = sortDirectionOptional.orElse("asc");

        Comparator sortComparator = switch (fieldToSortBy.toUpperCase()){
            case ("OPENPRICE")-> Comparator.comparing(StocksPriceHistoryVo::getOpenPrice);
            case ("CLOSEPRICE")-> Comparator.comparing(StocksPriceHistoryVo::getClosePrice);
            case ("VOLUME")-> Comparator.comparing(StocksPriceHistoryVo::getVolume);
            case("TRADINGDATE")-> Comparator.comparing(StocksPriceHistoryVo::getTradingDate);
            default -> {

                LOGGER.error("Value Enetered for the sortFieldBy is incorrect.Value entered is :{}",fieldToSortBy);
                throw new IllegalArgumentException("Value Enetered for the sortFieldBy is incorrect." +
                        " Value entered is : "+fieldToSortBy);
            }
        };


        if(directionToSortBy.equalsIgnoreCase("DESC")){
            sortComparator=sortComparator.reversed();
        }

//        Collections.sort(singleStockPriceHistory,sortComparator);

        singleStockPriceHistory.sort(sortComparator);

        return singleStockPriceHistory;

    }

    public List<StocksPriceHistoryVo> getMultipleStockPriceHistory(List<String> tickerSymbols,
                                                                   LocalDate fromDate,
                                                                   LocalDate toDate){
        return stockPriceHistoryDao.getMultipleStockPriceHistory(tickerSymbols, fromDate, toDate);
    }

    public List<StockFundamentalsWithnamesVo> getAllStockFundamentals(){

        LOGGER.debug("Entered teh method getAllStockFundamentals() of the class {}",getClass());
        List<StockFundamentalsWithnamesVo> stockfundamentalsList= stockFundamentalsWithNamesDao.getallStocksWithNames();


        return stockfundamentalsList;
    }
    public List<StockFundamentalsWithnamesVo> getAllStockFundamentals1(List<String> tickerSymbolsList){

        List<StockFundamentalsWithnamesVo> stockfundamentalsList1= stockFundamentalsWithNamesDao.getallStocksWithNames();

        List<StockFundamentalsWithnamesVo> finalGivenTickerDetails = stockfundamentalsList1.stream()
                .filter((tickerList -> tickerSymbolsList.contains(tickerList.getTickerSymbol())))
                .collect(Collectors.toList());
        System.out.println(finalGivenTickerDetails);
        return finalGivenTickerDetails;
    }

    public List<StockFundamentalsWithnamesVo> getGivenTickerDetails(List<String> tickerSymbols){
        return stockFundamentalsWithNamesDao.getGivenStocksWithNames(tickerSymbols);
    }

    public List<StockFundamentals> getAllStockFundamentalsJpa(){
        return stockFundamentalRepository.findAll();
    }


}
