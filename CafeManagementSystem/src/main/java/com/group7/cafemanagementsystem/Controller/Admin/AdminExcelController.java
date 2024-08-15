package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Response.ImportProductResponse;
import com.group7.cafemanagementsystem.Service.ExcelService;
import com.group7.cafemanagementsystem.helper.ImportExcelHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/excel")
public class AdminExcelController {
    private ExcelService excelService;

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
                excelService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                redirectAttributes.addFlashAttribute("messageSuccess", message);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                redirectAttributes.addFlashAttribute("messageError", message);
            }
        } else {
            message = "Please upload an excel file!";
            redirectAttributes.addFlashAttribute("messageError", message);
        }
        return "redirect:/admin/food/list";
    }
}
