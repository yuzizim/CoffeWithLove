package com.group7.cafemanagementsystem.helper;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            int count = 1;
            for (FoodRevenueResponse foodRevenue : foodRevenueResponses) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(count++);
                row.createCell(1).setCellValue(foodRevenue.getName());
                row.createCell(2).setCellValue(foodRevenue.getNumSale());
                row.createCell(3).setCellValue(foodRevenue.getPrice());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
