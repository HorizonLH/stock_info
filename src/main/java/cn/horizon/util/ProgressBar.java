package cn.horizon.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @description: 进度条
 * @author: Yang JianXiong
 * @since: 2022/11/26
 */
@Data
public class ProgressBar {

    private ColorEnum colorEnum;

    private int totalVal;

    private static final StringBuilder STRING_BUILDER =  new StringBuilder();

    private static final int TOTAL = 100;
    private static final char BACK_GROUND = '░';
    private static final char FRONT_GROUND = '█';

    public ProgressBar(int totalVal, ColorEnum colorEnum) {
        this.colorEnum = colorEnum;
        this.totalVal = totalVal;
        Stream.generate(() -> BACK_GROUND).limit(totalVal).forEach(STRING_BUILDER::append);
    }

//    public static void main(String[] args) throws InterruptedException {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        Stream.generate(() -> BACK_GROUND).limit(TOTAL).forEach(stringBuilder::append);
//
//        for (int i = 0; i < TOTAL; i++) {
//            stringBuilder.replace(i, i + 1, String.valueOf(FRONT_GROUND));
//            String bar = "\r" + stringBuilder;
//            String percent = " " + (1 + i) + "%";
//
//            // 输出绿色进度条
//            System.out.print(ProgressBar.ColorEnum.GREEN.getValue() + bar + percent);
//
//            // 防止打印太快，方便观察
//            TimeUnit.MILLISECONDS.sleep(i * 10L);
//        }
//    }


    public void printProgress(int currentVal, String appendLog) {
        STRING_BUILDER.replace(currentVal, currentVal + 1, String.valueOf(FRONT_GROUND));
        String bar = "\r" + STRING_BUILDER;
        String percent = " " + currentVal + "%";

        System.out.print(colorEnum.getValue() + bar + percent + "\t" + appendLog);

    }
    public void printProgress(int currentVal) {
        STRING_BUILDER.replace(currentVal, currentVal + 1, String.valueOf(FRONT_GROUND));
        String bar = "\r" + STRING_BUILDER;
        String percent = " " + (1 + currentVal) + "%";

        System.out.print(colorEnum.getValue() + bar + percent);

    }



    /**
     * 颜色枚举
     */
    public enum ColorEnum {

        /**
         * 白色
         */
        WHITE("\33[0m"),

        /**
         * 红色
         */
        RED("\33[1m\33[31m"),

        /**
         * 绿色
         */
        GREEN("\33[1m\33[32m"),

        /**
         * 黄色
         */
        YELLOW("\33[1m\33[33m"),

        /**
         * 蓝色
         */
        BLUE("\33[1m\33[34m"),

        /**
         * 粉色
         */
        PINK("\33[1m\33[35m"),

        /**
         * 青色
         */
        CYAN("\33[1m\33[36m");

        /**
         * 颜色值
         */
        private final String value;

        ColorEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
