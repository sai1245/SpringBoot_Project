package io.endeavour.stocks.service;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.UnitTestBase;
import io.endeavour.stocks.dao.StockFundamentalsWithNamesDao;
import io.endeavour.stocks.remote.StockCalculationsClient;
import io.endeavour.stocks.remote.vo.CRWSOutputVO;
import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//test profile      //https://www.baeldung.com/spring-profiles
class MarketAnalyticsServiceTest extends UnitTestBase {

    @Autowired
    MarketAnalyticsService marketAnalyticsService;
    @MockBean
    StockFundamentalsWithNamesDao stockFundamentalsWithNamesDao;
    @MockBean
    StockCalculationsClient stockCalculationsClient;

    @Test
    public void topNPerformingStocks_HappyPath(){

        List<StockFundamentalsWithNamesVO> dummyStockFundamentalList = List.of(
                createStockFundamental("AAPL", new BigDecimal("123456"), new BigDecimal("2.23")),
                createStockFundamental("MSFT", new BigDecimal("1100"), new BigDecimal("1.98")),
                createStockFundamental("AMD", new BigDecimal("1234"), new BigDecimal("1.36")),
                createStockFundamental("V", new BigDecimal("1450"), new BigDecimal("1.98")),
                createStockFundamental("GOOGL", new BigDecimal("1566"), new BigDecimal("2.75")),
                createStockFundamental("NVDA", new BigDecimal("1345"), new BigDecimal("1.29"))

        );
        /**
         * This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
         * The actual databse call is bypassed in the business code and replaced with the mock call which returns the dymmyList
         */

        Mockito.when(stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO())
                .thenReturn(dummyStockFundamentalList);

        List<CRWSOutputVO> dummyWSOutputList = List.of(
                createWSOutput("AAPL", new BigDecimal("1.90")),
                createWSOutput("MSFT", new BigDecimal("1.76")),
                createWSOutput("AMD", new BigDecimal("1.43")),
                createWSOutput("V", new BigDecimal("1.25")),
                createWSOutput("GOOGL", new BigDecimal("1.73")),
                createWSOutput("NVDA", new BigDecimal("1.01"))
        );

        /**
         * This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
         * The actual databse call is bypassed in the business code and replaced with the mock call which returns the dymmyList
         */

        Mockito.when(stockCalculationsClient.getCumulativeReturns(Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(dummyWSOutputList);


        List<StockFundamentalsWithNamesVO> topNPerformingStocksList = marketAnalyticsService.getTopNPerformingStocks(3,
                LocalDate.now().minusMonths(3),
                LocalDate.now(),
                0L);

        System.out.printf(topNPerformingStocksList.toString());


        //check if top 3 stocks were returned or not
        assertEquals(3,topNPerformingStocksList.size());

        //check if the stocks are sorted by cumulativereturn desc
        assertEquals("MSFT",topNPerformingStocksList.get(1).getTickerSymbol());

        //check if AMD , V and NVDA are excluded or not
        StockFundamentalsWithNamesVO dummyAMDStock= new StockFundamentalsWithNamesVO();
        dummyAMDStock.setTickerSymbol("AMD");
        assertFalse(topNPerformingStocksList.contains(dummyAMDStock));


    }

    @Test
    public void topNPerformingStocks_WebserviceDown(){

        List<StockFundamentalsWithNamesVO> dummyStockFundamentalList = List.of(
                createStockFundamental("AAPL", new BigDecimal("123456"), new BigDecimal("2.23")),
                createStockFundamental("MSFT", new BigDecimal("1100"), new BigDecimal("1.98")),
                createStockFundamental("AMD", new BigDecimal("1234"), new BigDecimal("1.36")),
                createStockFundamental("V", new BigDecimal("1450"), new BigDecimal("1.98")),
                createStockFundamental("GOOGL", new BigDecimal("1566"), new BigDecimal("2.75")),
                createStockFundamental("NVDA", new BigDecimal("1345"), new BigDecimal("1.29"))

        );
        /**
         * This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
         * The actual databse call is bypassed in the business code and replaced with the mock call which returns the dymmyList
         */

        Mockito.when(stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO())
                .thenReturn(dummyStockFundamentalList);


        //This call simulates the webservice being down as it returns an empty list.
        Mockito.when(stockCalculationsClient.getCumulativeReturns(Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(Collections.emptyList());

        assertThrows(StockException.class,()->marketAnalyticsService.getTopNPerformingStocks(5,
                LocalDate.now().minusMonths(6),
                LocalDate.now(),0L));


        Exception exception= assertThrows(StockException.class,()->marketAnalyticsService.getTopNPerformingStocks(5,
                LocalDate.now().minusMonths(6),
                LocalDate.now(),0L));

        String expectedMessage = "Web Service is down";
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage.toUpperCase()));
    }

    @Test
    public void topNPerformingStocks_UnMatchedData(){

        List<StockFundamentalsWithNamesVO> dummyStockFundamentalList = List.of(
                createStockFundamental("AAPL", new BigDecimal("123456"), new BigDecimal("2.23")),
                createStockFundamental("MSFT", new BigDecimal("1100"), new BigDecimal("1.98")),
                createStockFundamental("AMD", new BigDecimal("1234"), new BigDecimal("1.36")),
                createStockFundamental("V", new BigDecimal("1450"), new BigDecimal("1.98")),
                createStockFundamental("GOOGL", new BigDecimal("1566"), new BigDecimal("2.75")),
                createStockFundamental("NVDA", new BigDecimal("1345"), new BigDecimal("1.29"))

        );
        /**
         * This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
         * The actual databse call is bypassed in the business code and replaced with the mock call which returns the dymmyList
         */

        Mockito.when(stockFundamentalsWithNamesDao.getAllStockFundamentalsWithNamesVO())
                .thenReturn(dummyStockFundamentalList);

        List<CRWSOutputVO> dummyWSOutputList = List.of(
                createWSOutput("AAPL", new BigDecimal("1.90")),
                createWSOutput("MSFT", new BigDecimal("1.76")),
                createWSOutput("AMD", new BigDecimal("1.43")),
                createWSOutput("V", new BigDecimal("1.25"))
        );

        /**
         * This code intercepts the method call of the mocked bean, and returns our dummy list as the output list
         * The actual databse call is bypassed in the business code and replaced with the mock call which returns the dymmyList
         */

        Mockito.when(stockCalculationsClient.getCumulativeReturns(Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(dummyWSOutputList);

        List<StockFundamentalsWithNamesVO> topNPerformingStocksList = marketAnalyticsService.getTopNPerformingStocks(5,
                LocalDate.now().minusMonths(3),
                LocalDate.now(),
                0L);

        /**
         * In this case we are sending only 4 cumuklative returns for the total of 6 stocks
         * and in the method we are requesting for top5  stocks
         * it will return only 4 stocks as there is no cumulative return for the remaining 2 so they are null values.
         * in our original method in the marketanalytics service we are removing the nulls from the final output
         * so we get only 4 values.
         */

        assertEquals(4,topNPerformingStocksList.size());


    }



    private StockFundamentalsWithNamesVO createStockFundamental(String tickerSymbol,
                                                                BigDecimal marketCap,
                                                                BigDecimal currentRatio){
        StockFundamentalsWithNamesVO stockFundamentalsWithNamesVO= new StockFundamentalsWithNamesVO();
        stockFundamentalsWithNamesVO.setTickerSymbol(tickerSymbol);
        stockFundamentalsWithNamesVO.setMarketCap(marketCap);
        stockFundamentalsWithNamesVO.setCurrentRatio(currentRatio);

        return stockFundamentalsWithNamesVO;
    }

    private CRWSOutputVO createWSOutput(String tickerSymbol, BigDecimal cumulativeReturn){
        CRWSOutputVO crwsOutputVO = new CRWSOutputVO();
        crwsOutputVO.setTickerSymbol(tickerSymbol);
        crwsOutputVO.setCumulativeReturn(cumulativeReturn);
        return crwsOutputVO;
    }



}