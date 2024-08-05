package com.group7.cafemanagementsystem.helper;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.model.OrderTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelHelper2 {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"#", "Customer name", "Phone number", "Order time", "Price", "Created by"};
    static String SHEET = "Revenue_By_Order";

    public static ByteArrayInputStream tutorialsToExcel(List<OrderTable> orderTables) {

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

            // Date CellStyle
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            int count = 1;
            double totalPrice = 0;
            for (OrderTable order : orderTables) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(count++);
                row.createCell(1).setCellValue(order.getCustomerName());
                row.createCell(2).setCellValue(order.getPhoneNumber());

                Cell orderTimeCell = row.createCell(3);
                orderTimeCell.setCellValue(order.getOrderTime().format(formatter));
                orderTimeCell.setCellStyle(dateCellStyle);

                row.createCell(4).setCellValue(order.getTotalPrice());
                row.createCell(5).setCellValue(order.getStaff().getFullName());

                totalPrice += order.getTotalPrice();
            }

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
            cell1.setCellValue("");
            cell1.setCellStyle(totalRowCellStyle);

            Cell cell2 = totalRow.createCell(2);
            cell2.setCellValue("");
            cell2.setCellStyle(totalRowCellStyle);

            Cell cell3 = totalRow.createCell(3);
            cell3.setCellValue("TOTAL");
            cell3.setCellStyle(totalRowCellStyle);

            Cell cell4 = totalRow.createCell(4);
            cell4.setCellValue(totalPrice);
            cell4.setCellStyle(totalRowCellStyle);

            Cell cell5 = totalRow.createCell(5);
            cell5.setCellValue("");
            cell5.setCellStyle(totalRowCellStyle);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
