package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Repository.FoodRepository;
import com.group7.cafemanagementsystem.Response.ImportExcelResponse;
import com.group7.cafemanagementsystem.Response.ImportProductResponse;
import com.group7.cafemanagementsystem.Service.ExcelService;
import com.group7.cafemanagementsystem.helper.ExcelHelper2;
import com.group7.cafemanagementsystem.helper.ImportExcelHelper;
import com.group7.cafemanagementsystem.model.Food;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/excel")
public class AdminExcelController {
    private ExcelService excelService;
    private FoodRepository foodRepository;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String message = "";

        if (file.getSize() > 2 * 1024 * 1024) { // 2MB in bytes
            message = "File size exceeds limit. Please upload a file smaller than 2MB.";
            redirectAttributes.addFlashAttribute("messageError", message);
            return "redirect:/admin/food/list";
        }

        if (ImportExcelHelper.hasExcelFormat(file)) {
            try {
//                excelService.save(file);
                ImportExcelResponse importExcelResponse = ImportExcelHelper.excelToFoods(file.getInputStream());
                foodRepository.saveAll(importExcelResponse.getFoods());

                ByteArrayInputStream errorReport = importExcelResponse.getByteArrayInputStream();
                if (errorReport != null) {
                    // Save the error report to a file on the server
                    String errorFileName = "Error_Products.xlsx";
                    Path errorFilePath = Paths.get(System.getProperty("user.home"), "uploads", errorFileName);
                    Files.createDirectories(errorFilePath.getParent());
                    try (FileOutputStream fos = new FileOutputStream(errorFilePath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = errorReport.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    // Provide the file path to the view layer for download
                    redirectAttributes.addFlashAttribute("errorFilePath", errorFilePath.toString());
                    message = "File " + file.getOriginalFilename() + " has some error rows. Please check the error file.";
                    redirectAttributes.addFlashAttribute("messageError", message);
                } else {
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                    redirectAttributes.addFlashAttribute("messageSuccess", message);
                }
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + " because it's not in format!";
                redirectAttributes.addFlashAttribute("messageError", message);
            }
        } else {
            message = "Please upload an excel file!";
            redirectAttributes.addFlashAttribute("messageError", message);
        }
        return "redirect:/admin/food/list";
    }

    @GetMapping("/downloadErrorReport")
    public ResponseEntity<Resource> downloadErrorReport(@RequestParam("errorFilePath") String errorFilePath) throws IOException {
        Path file = Paths.get(errorFilePath);
        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("File not found " + errorFilePath);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/download/template")
    public ResponseEntity<InputStreamResource> downloadTemplate() {
        ByteArrayInputStream in = ImportExcelHelper.getTemplate();

        String filename = "template.xlsx";
//        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
