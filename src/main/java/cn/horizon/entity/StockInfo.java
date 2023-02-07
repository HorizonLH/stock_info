package cn.horizon.entity;

import lombok.Data;

import java.util.List;

@Data
public class StockInfo {

    public String symbol;

    public List<String> column;

    public List<List<Object>> item;


}
