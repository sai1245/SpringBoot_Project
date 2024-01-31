package io.endeavour.stocks.entity.stocks;

import io.endeavour.stocks.vo.TopThreeStockVO;

import javax.persistence.Entity;
import java.util.List;


public class SectorLookupUpdated {

    private int sectorId;
    private String sectorName;

    private List<TopThreeStockVO> topThreeStockVOS;

    public SectorLookupUpdated(int sectorId,String sectorName, List<TopThreeStockVO> topThreeStockVOS) {
        this.sectorId = sectorId;
        this.sectorName=sectorName;
        this.topThreeStockVOS = topThreeStockVOS;
    }

    public String getSectorName() {
        return sectorName;
    }

    public int getSectorId() {
        return sectorId;
    }

    public List<TopThreeStockVO> getTopThreeStockVOS() {
        return topThreeStockVOS;
    }
}
