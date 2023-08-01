package p.jesse.accountor.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.repositories.CategoryRepository;

import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ResponseEntity<String> addNewCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DataIntegrityViolationException("Category already exists!");
        }
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }
}
