package com.benio.demoproject.bluetooth;

import com.benio.demoproject.bluetooth.printer.PrintDataMaker;
import com.benio.demoproject.bluetooth.printer.PrinterWriter;
import com.benio.demoproject.bluetooth.printer.PrinterWriter58mm;
import com.benio.demoproject.bluetooth.printer.PrinterWriter80mm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 测试数据生成器
 * Created by Alex on 2016/11/10.
 */

public class TestPrintDataMaker implements PrintDataMaker {
    private int width;
    private int height;

    public TestPrintDataMaker(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();
        try {
            PrinterWriter printer;
            printer = type == PrinterWriter58mm.TYPE_58 ? new PrinterWriter58mm(height, width) : new PrinterWriter80mm(height, width);
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
            printer.setAlignLeft();
            printer.printLine();
            printer.printLineFeed();

            printer.printLineFeed();
            printer.setAlignCenter();
            printer.setEmphasizedOn();
            printer.setFontSize(1);
            printer.print("我的餐厅");
            printer.printLineFeed();
            printer.setFontSize(0);
            printer.setEmphasizedOff();
            printer.printLineFeed();

            printer.print("最时尚的明星餐厅");
            printer.printLineFeed();
            printer.print("客服电话：400-8008800");
            printer.printLineFeed();

            printer.setAlignLeft();
            printer.printLineFeed();

            printer.print("订单号：88888888888888888");
            printer.printLineFeed();

            printer.print("预计送达：" +
                    new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                            .format(new Date(System.currentTimeMillis())));
            printer.printLineFeed();

            printer.setEmphasizedOn();
            printer.print("#8（已付款）");
            printer.printLineFeed();
            printer.print("××区××路×××大厦××楼×××室");
            printer.printLineFeed();
            printer.setEmphasizedOff();
            printer.print("13843211234");
            printer.print("（张某某）");
            printer.printLineFeed();
            printer.print("备注：多加点辣椒，多加点香菜，多加点酸萝卜，多送点一次性手套");
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();

            printer.printInOneLine("星级美食（豪华套餐）×1", "￥88.88", 0);
            printer.printLineFeed();
            printer.printInOneLine("星级美食（限量套餐）×1", "￥888.88", 0);
            printer.printLineFeed();
            printer.printInOneLine("餐具×1", "￥0.00", 0);
            printer.printLineFeed();
            printer.printInOneLine("配送费", "免费", 0);
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();

            printer.setAlignRight();
            printer.print("合计：977.76");
            printer.printLineFeed();
            printer.printLineFeed();

            printer.setAlignCenter();

            data.add(printer.getDataAndReset());
            printer.printLineFeed();
            printer.printLineFeed();

            printer.feedPaperCutPartial();

            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}