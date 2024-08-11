package com.group7.cafemanagementsystem.helper;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@AllArgsConstructor
public class ImportExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Id", "Name", "Price", "Status", "Category", "Description"};
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

    public static List<Food> excelToFoods(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Food> foods = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Food product = new Food();
                boolean skipRow = false;
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    // Check for null or blank cells
                    if (currentCell == null || currentCell.getCellType() == CellType.BLANK) {
                        skipRow = true;
                        break;
                    }

                    switch (cellIdx) {
                        case 0:
                            if (currentCell.getCellType() != CellType.NUMERIC) {
                                skipRow = true;
                            }
                            break;
                        case 1:
                            if (currentCell.getCellType() == CellType.STRING) {
                                String productName = currentCell.getStringCellValue();
                                if (foodService.checkExistProduct(productName)) {
                                    skipRow = true;
                                } else {
                                    product.setName(productName);
                                }
                            } else {
                                skipRow = true;
                            }
                            break;
                        case 2:
                            if (currentCell.getCellType() == CellType.NUMERIC) {
                                product.setPrice((double) currentCell.getNumericCellValue());
                            } else {
                                skipRow = true;
                            }
                            break;
                        case 3:
                            if (currentCell.getCellType() == CellType.BOOLEAN) {
                                product.setStatus(currentCell.getBooleanCellValue());
                            } else {
                                skipRow = true;
                            }
                            break;
                        case 4:
                            if (currentCell.getCellType() == CellType.NUMERIC) {
                                int categoryId = (int) currentCell.getNumericCellValue();
                                FoodCategory category = foodCategoryService.findById(categoryId);
                                if (category == null) {
                                    skipRow = true;
                                } else {
                                    product.setFoodCategory(category);
                                }
                            } else {
                                skipRow = true;
                            }
                            break;
                        case 5:
                            if (currentCell.getCellType() == CellType.STRING) {
                                product.setDescription(currentCell.getStringCellValue());
                            } else {
                                skipRow = true;
                            }
                            break;
                        default:
                            break;
                    }

                    if (skipRow) {
                        break;
                    }

                    cellIdx++;
                }

                if (!skipRow) {
                    product.setImages("/static/img/food/none.jpg");
                    foods.add(product);
                }
            }

            workbook.close();

            return foods;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
