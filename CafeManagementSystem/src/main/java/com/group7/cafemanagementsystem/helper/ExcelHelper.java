package com.group7.cafemanagementsystem.helper;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Id", "Product name", "Number sale", "Price"};
    static String SHEET = "Revenue_Product_By_Day";

    public static ByteArrayInputStream tutorialsToExcel(List<FoodRevenueResponse> foodRevenueResponses) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header CellStyle
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            int count = 1;
            int totalNumber = 0;
            double totalPrice = 0;
            for (FoodRevenueResponse foodRevenue : foodRevenueResponses) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(count++);
                row.createCell(1).setCellValue(foodRevenue.getName());
                row.createCell(2).setCellValue(foodRevenue.getNumSale());
                row.createCell(3).setCellValue("$" + foodRevenue.getPrice());

                totalNumber += foodRevenue.getNumSale();
                totalPrice += foodRevenue.getPrice();
            }

//            Row row = sheet.createRow(rowIdx++);
//            row.createCell(0).setCellValue("");
//            row.createCell(1).setCellValue("TOTAL");
//            row.createCell(2).setCellValue(totalNumber);
//            row.createCell(3).setCellValue(totalPrice);

            // Total row CellStyle
            CellStyle totalRowCellStyle = workbook.createCellStyle();
            Font totalRowFont = workbook.createFont();
            totalRowFont.setBold(true);
            totalRowCellStyle.setFont(totalRowFont);

            // Total row
            Row totalRow = sheet.createRow(rowIdx++);
            Cell cell0 = totalRow.createCell(0);
            cell0.setCellValue("");
            cell0.setCellStyle(totalRowCellStyle);

            Cell cell1 = totalRow.createCell(1);
            cell1.setCellValue("TOTAL");
            cell1.setCellStyle(totalRowCellStyle);

            Cell cell2 = totalRow.createCell(2);
            cell2.setCellValue(totalNumber);
            cell2.setCellStyle(totalRowCellStyle);

            Cell cell3 = totalRow.createCell(3);
            cell3.setCellValue("$" + totalPrice);
            cell3.setCellStyle(totalRowCellStyle);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
