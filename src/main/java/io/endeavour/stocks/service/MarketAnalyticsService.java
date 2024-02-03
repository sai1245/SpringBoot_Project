package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.dao.StockPriceHistoryDao;
import io.endeavour.stocks.entity.stocks.*;
import io.endeavour.stocks.remote.StockCalculationsClient;
import io.endeavour.stocks.remote.vo.CRWSInputVO;
import io.endeavour.stocks.remote.vo.CRWSOutputVO;
import io.endeavour.stocks.repository.stocks.*;
import io.endeavour.stocks.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    StockCalculationsClient stockCalculationsClient;


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
        return stockFundamentalsRepository.getTopThreeStocks();
   }

   public List<SectorLookupUpdated> getThreeStocks(){
       List<TopThreeStockVO> topThreeStockVOS=stockFundamentalsRepository.getTopThreeStocks();
       List<SectorLookup> stocksListWithId=sectorLookupRepository.findAll();

       Map<Integer,SectorLookupUpdated> StocksWithSectorMap=new HashMap<>();

       for (SectorLookup sectorLookup:stocksListWithId)
       {
           List<TopThreeStockVO> collectedStocksList = topThreeStockVOS.stream()
                   .filter(stock -> sectorLookup.getSectorID().equals(stock.getSectorId()))
                   .collect(Collectors.toList());

           SectorLookupUpdated sector=new SectorLookupUpdated(sectorLookup.getSectorID(),sectorLookup.getSectorName(),collectedStocksList);
           StocksWithSectorMap.put(sectorLookup.getSectorID(),sector);

       }

       List<SectorLookupUpdated> finalResult=new ArrayList<>(StocksWithSectorMap.values());     //.values() ArrayList<>((Collection) StocksWithSectorMap)
       finalResult.sort(Comparator.comparing(SectorLookupUpdated::getSectorId));


       return finalResult;
   }

   public List<StockFundamentals> getTopNStocksNativeSQL(Integer number){
        return stockFundamentalsRepository.getTopNStocksNativeSQL(number);
   }

    public List<StockFundamentals> getTopNStocksNativeJPQL(Integer number){
        return stockFundamentalsWithNamesDao.getTopNStocksJPQL(number);
    }

    public List<StockFundamentals> getNotNullCurrentRatios(){
        return stockFundamentalsRepository.getNotNullCurrentRatios();
    }

    public List<StockFundamentals> getTopNStocksCriteriaAPI(Integer number){
        return stockFundamentalsWithNamesDao.getTopNStocksCriteriaAPI(number);
    }

    public StockHistoryVO tradingHistoryForStocksList(String tickerSymbol,
                                                      LocalDate fromDate,
                                                      LocalDate toDate){


        List<TradingHistoryForStocksVO>  tradingHistoryForStocksList=stockPriceHistoryRepository.tradingHistoryForStocksList(tickerSymbol, fromDate, toDate);


        Map<BigDecimal, Map<BigDecimal,List<TradingHistoryForStocksVO>>> firstMap=tradingHistoryForStocksList.stream()
                .collect(Collectors.groupingBy(TradingHistoryForStocksVO::getMarketCap,Collectors.groupingBy(TradingHistoryForStocksVO::getCurrentRatio)));


        BigDecimal marketCaps = firstMap.keySet().iterator().next();

        Map<BigDecimal, List<TradingHistoryForStocksVO>> secondMap = firstMap.get(marketCaps);


        BigDecimal currentRatios= secondMap.keySet().iterator().next();

        StockHistoryVO stockHistoryVO=new StockHistoryVO();

        stockHistoryVO.setMarketCap(marketCaps);
        stockHistoryVO.setCurrentRatio(currentRatios);

        List<TradingHistoryVO> tradingHistoryVOList=new ArrayList<>();

        for (TradingHistoryForStocksVO list : secondMap.get(currentRatios)) {
            TradingHistoryVO tradingHistoryVO = new TradingHistoryVO();
            tradingHistoryVO.setTickerSymbol(list.getTickerSymbol());
            tradingHistoryVO.setClosePrice(list.getClosePrice());
            tradingHistoryVO.setVolume(list.getVolume());
            tradingHistoryVO.setTradingDate(list.getTradingDate());
            tradingHistoryVO.setTickerName(list.getTickerName());

            tradingHistoryVOList.add(tradingHistoryVO);
        }

        stockHistoryVO.setTradinghistory(tradingHistoryVOList);



        return stockHistoryVO;
    }


    public List<StockFundamentalsWithNamesVO> getTopNPerformingStocks(Integer number, LocalDate fromDate, LocalDate toDate, Long marketCapLimit){

//        List<StockFundamentals> allStocksList = stockFundamentalsRepository.findAll();

        List<StockFundamentalsWithNamesVO> allStocksList= stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO();

        List<String> allTickerList = allStocksList.stream()
                .map(StockFundamentalsWithNamesVO::getTickerSymbol)
                .collect(Collectors.toList());
        LOGGER.info("Number of stocks that are being sent as input to the cumulative return web service is : {}",allStocksList.size());
        CRWSInputVO crwsInputVO = new CRWSInputVO();
        crwsInputVO.setTickers(allTickerList);


        List<CRWSOutputVO> cumulativeReturnsList = stockCalculationsClient.getCumulativeReturns(fromDate, toDate, crwsInputVO);

        LOGGER.info("Number of stocls returns form the cumulative returns webservice is :{}",cumulativeReturnsList.size());


        Map<String, BigDecimal> cumulativeReturnsByTickerSymbolMap = cumulativeReturnsList.stream()
                .collect(Collectors.toMap(
                        CRWSOutputVO::getTickerSymbol,
                        CRWSOutputVO::getCumulativeReturn
                ));


        allStocksList.forEach(stockFundamentals -> stockFundamentals
                .setCumulativeReturn(cumulativeReturnsByTickerSymbolMap
                        .get(stockFundamentals.getTickerSymbol())));

        List<StockFundamentalsWithNamesVO> collect = allStocksList.stream()
                .filter(stockFundamentals -> stockFundamentals.getCumulativeReturn() != null)
                .filter(stockFundamentals -> stockFundamentals.getMarketCap().compareTo(new BigDecimal(marketCapLimit)) > 0)
                .sorted(Comparator.comparing(StockFundamentalsWithNamesVO::getCumulativeReturn).reversed())
                .limit(number)
                .collect(Collectors.toList());


        return collect;
    }

}
