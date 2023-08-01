package p.jesse.accountor.service;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CategoryService.class})
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void shouldReturnAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<Category> actualCategoryList = categoryService.getAllCategories();

        assertThat(actualCategoryList).isEmpty();
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldAddNewCategory() {
        Category newCategory = new Category("purchase");
        when(categoryRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> actualResult = categoryService.addNewCategory(newCategory);

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(capturedCategory.getName()).isEqualTo(newCategory.getName());
    }

    @Test
    void shouldThrowAndReturn400IfCategoryAlreadyExists() {
        Category newCategory = new Category("purchase");
        when(categoryRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(newCategory));

        assertThrows(DataIntegrityViolationException.class,
                () -> categoryService.addNewCategory(newCategory),
                "Category already exists!");
    }

    @Test
    void shouldDeleteCategoryWithGivenId() {
        Long id = 1L;
        when(categoryRepository.existsById(Mockito.any())).thenReturn(true);

        categoryService.deleteCategory(id);
        verify(categoryRepository).deleteById(Mockito.any());
    }

    @Test
    void shouldDoNothingIdCategoryIdNotExists() {
        Long id = 1L;
        when(categoryRepository.existsById(Mockito.any())).thenReturn(false);
        categoryService.deleteCategory(id);
        verify(categoryRepository, times(0)).deleteById(Mockito.any());
    }
}