package io.endeavour.stocks.remote.vo;

import io.endeavour.stocks.vo.StockFundamentalsWithNamesVO;

import java.util.List;

public class StocksBySubSectorVO {
    private String sectorName;
    private String subSectorName;
    private List<StocksWithNameAndCumulativeReturnsVO> topStocks;

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public List<StocksWithNameAndCumulativeReturnsVO> getTopStocks() {
        return topStocks;
    }

    public void setTopStocks(List<StocksWithNameAndCumulativeReturnsVO> topStocks) {
        this.topStocks = topStocks;
    }

    @Override
    public String toString() {
        return "SubSectorVo{" +
                "sectorName='" + sectorName + '\'' +
                ", subSectorName='" + subSectorName + '\'' +
                ", topStocks=" + topStocks +
                '}';
    }
}
