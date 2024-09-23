package pl.dolien.shop.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.auth.PasswordRequest;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @GetMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestParam String email) {
        service.addAdmin(email);
        return ResponseEntity.accepted().build();
    }
}
