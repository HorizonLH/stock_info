package cn.horizon.entity;

import lombok.Data;

@Data
public class ResponseData {
    public StockInfo data;

    public Integer errorCode;

    public String errorDescription;

    public String errorUri;

}
