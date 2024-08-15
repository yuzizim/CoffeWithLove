package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.FoodRepository;
import com.group7.cafemanagementsystem.Response.ImportExcelResponse;
import com.group7.cafemanagementsystem.helper.ImportExcelHelper;
import com.group7.cafemanagementsystem.model.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private FoodRepository foodRepository;

    public void save(MultipartFile file) {
        try {
            ImportExcelResponse foods = ImportExcelHelper.excelToFoods(file.getInputStream());
            foodRepository.saveAll(foods.getFoods());
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
