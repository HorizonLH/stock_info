package cn.horizon.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class Stock {
    public String symbol;
    public Date timestamp;
    public Integer volume;
    public Object open;
    public Object high;
    public Object low;
    public Object close;
    public Object chg;
    public Object percent;
    public Object turnoverrate;
    public Object amount;
    public Object volume_post;
    public Object amount_post;
    public Object pe;
    public Object pb;
    public Object ps;
    public Object pcf;
    public Object market_capital;
    public Object balance;
    public Object hold_volume_cn;
    public Object hold_ratio_cn;
    public Object net_volume_cn;
    public Object hold_volume_hk;
    public Object hold_ratio_hk;
    public Object net_volume_hk;



}
