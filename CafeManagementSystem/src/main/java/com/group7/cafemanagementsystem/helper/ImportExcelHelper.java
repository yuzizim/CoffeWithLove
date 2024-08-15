package com.group7.cafemanagementsystem.helper;

import com.group7.cafemanagementsystem.Response.ImportExcelResponse;
import com.group7.cafemanagementsystem.Service.FoodCategoryService;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.FoodCategory;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@AllArgsConstructor
public class ImportExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Name", "Price", "Status", "Category", "Description"};
    static String[] HEADERs2 = {"Name", "Price", "Status", "Category", "Description", "Error"};
    static String SHEET = "Foods";
    private static FoodCategoryService foodCategoryService;
    private static FoodService foodService;

    @Autowired
    public ImportExcelHelper(FoodCategoryService foodCategoryService, FoodService foodService) {
        this.foodCategoryService = foodCategoryService;
        this.foodService = foodService;
    }

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static ImportExcelResponse excelToFoods(InputStream is) {
        ImportExcelResponse importExcelResponse = new ImportExcelResponse();
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            Workbook workbookExport = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Sheet sheetExport = workbookExport.createSheet(SHEET);

            // Header CellStyle
            CellStyle headerCellStyle = workbookExport.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font headerFont = workbookExport.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);

//            // Header for the export sheet with "Error" column
//            String[] headersWithError = new String[HEADERs.length + 1];
//            System.arraycopy(HEADERs, 0, headersWithError, 0, HEADERs.length);
//            headersWithError[HEADERs.length] = "Error";

            // Header
            Row headerRow = sheetExport.createRow(0);

            for (int col = 0; col < HEADERs2.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs2[col]);
                cell.setCellStyle(headerCellStyle);
            }

            List<Food> foods = new ArrayList<>();
            List<StringBuilder> errorMessages = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    if (currentRow.getLastCellNum() != HEADERs.length) {
                        throw new IllegalArgumentException("The Excel file has an incorrect number of columns. Expected: " + HEADERs.length + ", Found: " + currentRow.getLastCellNum());
                    }
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Food product = new Food();
                boolean skipRow = false;
                int cellIdx = 0;
                StringBuilder errorMessage = new StringBuilder();
                Row exportRow = sheetExport.createRow(rowNumber + 1);
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    Cell exportCell = exportRow.createCell(cellIdx);

                    // Check for null or blank cells
                    if (currentCell == null || currentCell.getCellType() == CellType.BLANK) {
                        skipRow = true;
                        errorMessage.append("Blank cell; ");
                        break;
                    }

                    switch (cellIdx) {
                        case 0:
                            if (currentCell.getCellType() == CellType.STRING && !foodService.checkExistProduct(currentCell.getStringCellValue())) {
                                product.setName(currentCell.getStringCellValue());
                                exportCell.setCellValue(currentCell.getStringCellValue());
                            } else {
                                skipRow = true;
                                errorMessage.append("Product name is duplicate; ");
                            }
                            break;
                        case 1:
                            if (currentCell.getCellType() == CellType.NUMERIC && (double) currentCell.getNumericCellValue() > 0) {
                                product.setPrice((double) currentCell.getNumericCellValue());
                                exportCell.setCellValue((double) currentCell.getNumericCellValue());
                            } else {
                                skipRow = true;
                                errorMessage.append("Price must be decimal number and larger than 0; ");
                            }
                            break;
                        case 2:
                            if (currentCell.getCellType() == CellType.BOOLEAN) {
                                product.setStatus(currentCell.getBooleanCellValue());
                                exportCell.setCellValue(currentCell.getBooleanCellValue());
                            } else {
                                skipRow = true;
                                errorMessage.append("Status must be true or false; ");
                            }
                            break;
                        case 3:
                            if (currentCell.getCellType() == CellType.NUMERIC) {
                                int categoryId = (int) currentCell.getNumericCellValue();
                                FoodCategory category = foodCategoryService.findById(categoryId);
                                if (category == null) {
                                    skipRow = true;
                                    errorMessage.append("Category is not exist; ");
                                } else {
                                    product.setFoodCategory(category);
                                    exportCell.setCellValue(categoryId);
                                }
                            } else {
                                skipRow = true;
                                errorMessage.append("Category must be number; ");
                            }
                            break;
                        case 4:
                            if (currentCell.getCellType() == CellType.STRING) {
                                product.setDescription(currentCell.getStringCellValue());
                                exportCell.setCellValue(currentCell.getStringCellValue());
                            } else {
                                skipRow = true;
                                errorMessage.append("Invalid description; ");
                            }
                            break;
                        default:
                            break;
                    }

//                    if (skipRow) {
//                        break;
//                    }

                    cellIdx++;
                }

                if (!skipRow) {
                    product.setImages("/static/img/food/none.jpg");
                    foods.add(product);
                }
                // Add error message
                exportRow.createCell(cellIdx).setCellValue(errorMessage.toString());
                if (errorMessage.length() > 0) {
                    errorMessages.add(errorMessage);
                }
                rowNumber++;
            }

            if (rowNumber > 0 && errorMessages.size() > 0) {
                workbookExport.write(out);
                ByteArrayInputStream errorReport = new ByteArrayInputStream(out.toByteArray());
                importExcelResponse.setByteArrayInputStream(errorReport);
            }

            workbook.close();

            importExcelResponse.setFoods(foods);
            return importExcelResponse;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream getTemplate() {

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
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
