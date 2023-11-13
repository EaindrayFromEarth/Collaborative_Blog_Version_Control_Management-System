package com.we_write.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we_write.entity.Category;
import com.we_write.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        return categoryRepository.save(category);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public Category updateCategory(Long categoryId, String name, String description) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElse(null);

        if (existingCategory != null) {
            existingCategory.setName(name);
            existingCategory.setDescription(description);
            return categoryRepository.save(existingCategory);
        }

        return null;
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    
}
