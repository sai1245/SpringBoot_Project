package io.endeavour.stocks.entity.sectorsubsector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "subsector_lookup",schema = "endeavour")
public class SubSector {

    @Column(name = "subsector_id")
    @Id
    private int subSectorId;

    @Column(name = "subsector_name")
    private String subSectorName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sector_id")
    private int sectorId;

    public int getSubSectorId() {
        return subSectorId;
    }

    public void setSubSectorId(int subSectorId) {
        this.subSectorId = subSectorId;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    @Override
    public String toString() {
        return "SubSector{" +
                "subSectorId=" + subSectorId +
                ", subSectorName='" + subSectorName + '\'' +
                ", sectorId=" + sectorId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSector subSector = (SubSector) o;
        return subSectorId == subSector.subSectorId && sectorId == subSector.sectorId && Objects.equals(subSectorName, subSector.subSectorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subSectorId, subSectorName, sectorId);
    }
}
