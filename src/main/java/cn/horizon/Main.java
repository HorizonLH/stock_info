package cn.horizon;



import cn.horizon.entity.ResponseData;
import cn.horizon.entity.Stock;
import cn.horizon.entity.StockInfo;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpCookie;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/d");
        ArrayList<Map<Object, Object>> stocks = new ArrayList<>();
        ArrayList<Map<Object, Object>> stocks2 = new ArrayList<>();
//        try {
//            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("company.txt"));
//
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getMessage());
//        }

        Scanner scanner = new Scanner(System.in);
//        Scanner scanner = new Scanner(Files.newInputStream(Paths.get("src/main/resources/test.txt")));
        while (!scanner.hasNext("0")) {
            String next = scanner.next();
            System.out.println("公司代码：" + next);
            List<Stock> stockList = getStocks(next);

            Map<Object, Object> excelData = new TreeMap<>();
            Map<Object, Object> excelData2 = new TreeMap<>();
            if (!CollectionUtil.isEmpty(stockList)) {
                System.out.println(stockList);
                excelData.put("公司代码", stockList.get(0).getSymbol());
                stockList.forEach(stock -> excelData.put(sdf.format(stock.getTimestamp()), stock.getClose()));
                stocks.add(excelData);

                excelData2.put("公司代码", stockList.get(0).getSymbol());
                stockList.forEach(stock -> excelData2.put(sdf.format(stock.getTimestamp()), stock.getPe()));
                stocks2.add(excelData2);
            } else {
                System.out.println("无法获取" + next + "的股票信息");
            }
        }
        if (CollectionUtil.isEmpty(stocks) || CollectionUtil.isEmpty(stocks2)) {
            System.out.println("无数据导出");
        } else {
            String outputPath = getOutPutPath();
            System.out.println("文件保存在" + outputPath + "环保行业资本市场分析.xlsx");
            ExcelWriter excelWriter = ExcelUtil.getWriter(outputPath + "/环保行业资本市场分析.xlsx", "股价数据");
            excelWriter.write(stocks);
            excelWriter.autoSizeColumnAll();

            excelWriter.setSheet("市盈率");
            excelWriter.write(stocks2);
            excelWriter.autoSizeColumnAll();

            excelWriter.close();

        }

    }

    private static List<Stock> getStocks(String companyCode) {
        String httpCookies = getCookie();
        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("symbol", "SZ002573");
        paramMap.put("symbol", companyCode);
        paramMap.put("begin", System.currentTimeMillis());
        paramMap.put("period", "day");
        paramMap.put("type", "before");
        paramMap.put("count", "-5");
        paramMap.put("indicator", "kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance");

//        String s = HttpUtil.get("https://stock.xueqiu.com/v5/stock/chart/kline.json", paramMap);
        String s = HttpRequest.get("https://stock.xueqiu.com/v5/stock/chart/kline.json")
                .header(Header.COOKIE, httpCookies)
                .form(paramMap)
                .execute().body();
        ResponseData responseData = JSON.parseObject(s, ResponseData.class);
        System.out.println(responseData);
        if (responseData.getData() == null) {
            StockInfo emptyStock = new StockInfo();
            emptyStock.setSymbol(companyCode);
            emptyStock.setItem(new ArrayList<>());
            responseData.setData(emptyStock);
        }
        if (!CollectionUtil.isEmpty(responseData.getData().getItem())) {

            return responseData.getData().getItem().stream().map(item -> {
                try {
                    return getStockObj(item, responseData.data.getSymbol());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private static String getCookie() {
        //Get request
        HttpResponse httpResponse = HttpRequest.get("https://xueqiu.com").execute();
        List<HttpCookie> cookies = httpResponse.getCookies();
        return cookies.stream().map(String::valueOf).collect(Collectors.joining("; "));
    }

    public static Stock getStockObj(List<Object> data, String symbol) throws IllegalAccessException {
        Stock stock = new Stock();
        Class<? extends Stock> stockClass = stock.getClass();
        Field[] fields = stockClass.getFields();
        fields[0].set(stock, symbol);
        fields[1].set(stock, new Date((Long) data.get(0)));
        for (int i = 2; i < fields.length; i++) {
            fields[i].set(stock, data.get(i - 1));
        }

        return stock;


    }

    public static String getOutPutPath() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.showOpenDialog(jFileChooser);
        return jFileChooser.getSelectedFile().getPath();
    }
}