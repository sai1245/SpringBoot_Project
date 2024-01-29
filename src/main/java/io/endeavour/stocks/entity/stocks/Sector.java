package io.endeavour.stocks.entity.stocks;

import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sector_lookup", schema = "endeavour")
public class Sector {
//    @Column(name = "sector_id")
//    @Id

    @OneToMany(mappedBy = "sector_lookup",fetch = FetchType.EAGER)
    private int sectorId;
    @Column(name = "sector_name")
    private String sectorName;

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return sectorId == sector.sectorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorId);
    }

    @Override
    public String toString() {
        return "Sector{" +
                "sectorId=" + sectorId +
                ", sectorName='" + sectorName + '\'' +
                '}';
    }
}
