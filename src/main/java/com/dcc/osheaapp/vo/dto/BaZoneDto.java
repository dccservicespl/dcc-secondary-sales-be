package com.dcc.osheaapp.vo.dto;

public class BaZoneDto {
    private Long total;
    private Long east;
    private Long west;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getEast() {
        return east;
    }

    public void setEast(Long east) {
        this.east = east;
    }

    public Long getWest() {
        return west;
    }

    public void setWest(Long west) {
        this.west = west;
    }

    public Long getNorth() {
        return north;
    }

    public void setNorth(Long north) {
        this.north = north;
    }

    public Long getSouth() {
        return south;
    }

    public void setSouth(Long south) {
        this.south = south;
    }

    private Long north;
    private Long south;

    public BaZoneDto(Long total, Long east, Long west, Long north, Long south) {
        this.total = total;
        this.east = east;
        this.west = west;
        this.north = north;
        this.south = south;
    }
}
