package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.entity.stocks.*;
import io.endeavour.stocks.repository.stocks.*;
import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.vo.StocksPriceHistoryVO;
import io.endeavour.stocks.vo.TopStockBySectorVO;
import io.endeavour.stocks.vo.TopThreeStockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarketAnalyticsService {

    private final static Logger LOGGER= LoggerFactory.getLogger(MarketAnalyticsService.class);
    StockPriceHistoryDao stockPriceHistoryDao;

    @Autowired
    StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao;

    @Autowired
    StockFundamentalsRepository stockFundamentalsRepository;

    @Autowired
    SectorLookupRepository sectorLookupRepository;

    SubSectorLookupRepository subSectorLookupRepository;

    @Autowired
    StockPriceHistoryRepository stockPriceHistoryRepository;

    @Autowired
    StockFundamentalsRepo stockFundamentalsRepo;

    @Autowired
    public MarketAnalyticsService(StockPriceHistoryDao stockPriceHistoryDao,
                                  SubSectorLookupRepository subSectorLookupRepository) {
        this.stockPriceHistoryDao = stockPriceHistoryDao;
        this.subSectorLookupRepository=subSectorLookupRepository;
    }

    public List<StocksPriceHistoryVO> getSingleStockPriceHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate,
                                                                 Optional<String> sortFieldOptional, Optional<String> sortDirectionOptional){
        List<StocksPriceHistoryVO> stockPriceHistoryList = stockPriceHistoryDao.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);
        String fieldToSortBy = sortFieldOptional.orElse("TradingDate");
        String directionToSortBy = sortDirectionOptional.orElse("asc");

        Comparator sortComparator= switch (fieldToSortBy.toUpperCase()){
            case ("OPENPRICE")-> Comparator.comparing(StocksPriceHistoryVO::getOpenPrice);
            case ("CLOSEPRICE")->Comparator.comparing(StocksPriceHistoryVO::getClosePrice);
            case ("VOLUME")->Comparator.comparing(StocksPriceHistoryVO::getVolume);
            case ("TRADINGDATE")->Comparator.comparing(StocksPriceHistoryVO::getTradingDate);
            default -> {
                LOGGER.error("Value entered for sortField is incorrect. Value entered is {} : ",fieldToSortBy);
                throw new IllegalArgumentException("Value Entered for Sort field is Incorrect. Value entered is: " + fieldToSortBy);

            }

        };
        if (directionToSortBy.equalsIgnoreCase("desc")){
            sortComparator=sortComparator.reversed();
        }
        stockPriceHistoryList.sort(sortComparator);

        return stockPriceHistoryList;
    }

    public List<StocksPriceHistoryVO> getMultipleStockPriceHistory(List<String> tickerList, LocalDate fromDate, LocalDate toDate){
        return stockPriceHistoryDao.getMultipleStockPriceHistory(tickerList,fromDate,toDate);
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentals(){
        LOGGER.debug("Entered the method getAllStockFundamentals() of the class {}",getClass());
        List<StockFundamentalsWithNamesVO> stockFundamentalsList = stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();
        return  stockFundamentalsList;
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsForSpecificTickerSymbols(List<String> tickerSymbolList){
        List<StockFundamentalsWithNamesVO> allStockFundamentalsWithNamesVOList = stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();
        List<StockFundamentalsWithNamesVO> listOfRequiredStocks = allStockFundamentalsWithNamesVOList.stream()
                .filter(tickerList -> tickerSymbolList.contains(tickerList.getTickerSymbol()))
                .collect(Collectors.toList());
        return listOfRequiredStocks;
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsForGivenTickerSymbolsWithSQLQuery(List<String> tickerSymbolList ){

        return stockFundamentalsWithNamesDao.gellStockFundamentalDetailsWithNames(tickerSymbolList);
    }

    public List<StockFundamentals> getAllStockFundamentalsWithJPA(){
        return stockFundamentalsRepository.findAll();
    }

    public  List<SectorLookup> getAllSectorsWithItsSubSectors(){
        return sectorLookupRepository.findAll();
    }


    public List<SubSectorLookup> getAllSubSectorsWithSectors(){
        return subSectorLookupRepository.findAll();
    }


    public Optional<StockPriceHistory> getStockPriceHistory(String tickerSymbol, LocalDate tradingDate){
        StockPriceHistoryKey primaryKeyObj= new StockPriceHistoryKey();
        primaryKeyObj.setTickerSymbol(tickerSymbol);
        primaryKeyObj.setTradingDate(tradingDate);
        return stockPriceHistoryRepository.findById(primaryKeyObj);
    }

    public List<TopStockBySectorVO> getTopStockBySector()
    {
        return stockFundamentalsRepository.getAllTopStocksBySector();
    }

   public List<TopThreeStockVO> getTopThreeStocks(){
        return stockFundamentalsRepo.getTopThreeStocks();
   }

   public List<SectorLookupUpdated> getThreeStocks(){
       List<TopThreeStockVO> topThreeStockVOS=stockFundamentalsRepo.getTopThreeStocks();
       List<SectorLookup> stocksListWithId=sectorLookupRepository.findAll();

       Map<Integer,SectorLookupUpdated> StocksWithSectorMap=new HashMap<>();

       for (SectorLookup sectorLookup:stocksListWithId) {
           List<TopThreeStockVO> collectedStocksList = topThreeStockVOS.stream()
                   .filter(stock -> sectorLookup.getSectorID().equals(stock.getSectorId()))
                   .collect(Collectors.toList());

           SectorLookupUpdated sector=new SectorLookupUpdated(sectorLookup.getSectorID(),sectorLookup.getSectorName(),collectedStocksList);
           StocksWithSectorMap.put(sectorLookup.getSectorID(),sector);

       }

       List<SectorLookupUpdated> finalResult=new ArrayList<>(StocksWithSectorMap.values());     //.values() ArrayList<>((Collection) StocksWithSectorMap)


       return finalResult;
   }

}
