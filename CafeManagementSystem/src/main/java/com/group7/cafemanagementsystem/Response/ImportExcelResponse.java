package com.group7.cafemanagementsystem.Response;

import com.group7.cafemanagementsystem.model.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportExcelResponse {
    private List<Food> foods;
    private ByteArrayInputStream byteArrayInputStream;
}
