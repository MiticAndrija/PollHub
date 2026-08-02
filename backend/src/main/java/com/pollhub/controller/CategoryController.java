package com.pollhub.controller;
import com.pollhub.dto.*; import com.pollhub.service.CategoryService; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/categories") @RequiredArgsConstructor public class CategoryController {
 private final CategoryService service; @GetMapping public List<CategoryResponse> all(){return service.all();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public CategoryResponse update(@PathVariable Long id,@Valid @RequestBody CategoryRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
