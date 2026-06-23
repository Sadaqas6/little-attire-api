package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;


    public CategoriesController(CategoryService categoryService,ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Category> getAllCategories()
    {
        return categoryService.getAllCategories();
    }


    @GetMapping("/{categoryId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Category> getCategoryById(@PathVariable int categoryId)
    {
        return categoryService.getCategoryById(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {

        return productService.listByCategoryId(categoryId);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        Category saved = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category updateCategory(@PathVariable int categoryId, @RequestBody Category category)
    {
        if(categoryService.getCategoryById(categoryId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return categoryService.update(categoryId, category);
    }

    @DeleteMapping("{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId)
    {

      if (categoryService.getCategoryById(categoryId).isEmpty())
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

      categoryService.delete(categoryId);
      return ResponseEntity.noContent().build();
    }

}
