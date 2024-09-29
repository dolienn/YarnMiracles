package pl.dolien.shop.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.dashboard.DashboardData;
import pl.dolien.shop.dashboard.DashboardService;
import pl.dolien.shop.image.ImageService;
import pl.dolien.shop.product.*;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserEditDTO;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;
    private final DashboardService dashboardService;
    private final ImageService imageService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductService productService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestParam String email) {
        adminService.addAdmin(email);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/get-dashboard-data")
    public ResponseEntity<DashboardData> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(
            @RequestPart("product") @Valid ProductRequest productRequest,
            @RequestPart("file") @Valid @NotNull(message = "File is mandatory") MultipartFile file
    ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String absoluteUploadDir = new File(uploadDir).getAbsolutePath();
        File directory = new File(absoluteUploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String imageUrl;
        try {
            imageUrl = imageService.uploadImage(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not save file: " + e.getMessage());
        }

        Product product = productMapper.toProduct(productRequest, imageUrl);
        var savedProduct = productRepository.save(product);

        String sku = productService.generateSku(savedProduct.getName(), savedProduct.getId());
        savedProduct.setSku(sku);
        productRepository.save(savedProduct);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/edit-user")
    public ResponseEntity<?> editUser(@RequestBody @Valid UserEditDTO user) {
        adminService.editUser(user);
        return ResponseEntity.accepted().build();
    }
}
