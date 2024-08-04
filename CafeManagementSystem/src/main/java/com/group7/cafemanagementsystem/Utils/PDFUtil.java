package com.group7.cafemanagementsystem.Utils;

import com.group7.cafemanagementsystem.model.Bill;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.OrderDetail;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class PDFUtil {
    public static void generatePDF(Bill bill, Document document) throws FileNotFoundException {

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        float threecol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float onetwo[] = {threecol + 125f, threecol * 2};
        float oneColWidth[] = {twocol150};
        float twoColumnWidth[] = {twocol150, twocol};
        float threeColWidth[] = {threecol, threecol, threecol};
        float fullWidth[] = {threecol * 3};
        Paragraph onesp = new Paragraph("\n");

        Table table = new Table(twoColumnWidth);
        table.addCell(new Cell().add(new Paragraph("Bill")).setFontSize(40f).setBold().setBorder(Border.NO_BORDER));
        Table nestedTable = new Table(new float[]{twocol / 2, twocol / 2});
        nestedTable.addCell(getHeaderTextCell("Date checkin: "));
        nestedTable.addCell(getHeaderTextCellValue(bill.getDateCheckIn().format(formatter).toString()));
        nestedTable.addCell(getHeaderTextCell("Date checkout: "));
        nestedTable.addCell(getHeaderTextCellValue(bill.getDateCheckOut().format(formatter).toString()));
        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        Color gray = new DeviceRgb(128, 128, 128);
        Border border = new SolidBorder(gray, 1f / 2f);
        Table divider = new Table(fullWidth);
        divider.setBorder(border);
        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);

        Table twoColTable = new Table(twoColumnWidth);
        twoColTable.addCell(getBillingCell("Customer Information"));
        document.add(twoColTable.setMarginBottom(12f));

        Table oneColTable = new Table(oneColWidth);
        oneColTable.addCell(getCell10Left("Name", true));
        oneColTable.addCell(getCell10Left(bill.getOrderTable().getCustomerName(), false));
        oneColTable.addCell(getCell10Left("Phone", true));
        oneColTable.addCell(getCell10Left(bill.getOrderTable().getPhoneNumber(), false));
        document.add(oneColTable.setMarginBottom(10f));

        Table tabledDivider2 = new Table(fullWidth);
        Border dashBorder = new DashedBorder(gray, 0.5f);
        document.add(tabledDivider2.setBorder(dashBorder));
        Paragraph productPara = new Paragraph("Products");

        document.add(productPara.setBold());
        Table threeColTable = new Table(threeColWidth);
        Color black = new DeviceRgb(0, 0, 0);
        threeColTable.setBackgroundColor(black, 0.7f);

        // Define the white color
        Color white = new DeviceRgb(255, 255, 255);
        threeColTable.addCell(new Cell().add(new Paragraph("Name"))).setBold().setFontColor(white).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
        threeColTable.addCell(new Cell().add(new Paragraph("Quantity"))).setBold().setFontColor(white).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
        threeColTable.addCell(new Cell().add(new Paragraph("Price"))).setBold().setFontColor(white).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f);
        document.add(threeColTable);

        double total = 0;
        int number = 0;
        Table threeColTable2 = new Table(threeColWidth);
        for (OrderDetail orderDetail : bill.getOrderTable().getOrderDetails()) {
            total += orderDetail.getQuantity() * orderDetail.getFood().getPrice();
            number += orderDetail.getQuantity();
            threeColTable2.addCell(new Cell().add(new Paragraph(orderDetail.getFood().getName()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
            threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(orderDetail.getQuantity())))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
            threeColTable2.addCell(new Cell().add(new Paragraph("$" + String.valueOf(orderDetail.getFood().getPrice() * orderDetail.getQuantity())))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f);
        }
        document.add(threeColTable2);

        Table threeColTable4 = new Table(threeColWidth);
        threeColTable4.addCell(new Cell().add(new Paragraph("TOTAL"))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);
        threeColTable4.addCell(new Cell().add(new Paragraph(String.valueOf(number)))).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold();
        threeColTable4.addCell(new Cell().add(new Paragraph("$" + String.valueOf(total)))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f).setBold();
        document.add(threeColTable4);
        document.add(onesp);
        document.add(divider.setBorder(new SolidBorder(gray, 1)).setMarginBottom(15f));

        document.close();
    }

    static Cell getHeaderTextCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
    }

    static Cell getHeaderTextCellValue(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
    }

    static Cell getBillingCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10Left(String textValue, Boolean isBold) {
        Cell myCell = new Cell().add(new Paragraph(textValue)).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? myCell.setBold() : myCell;
    }
}
