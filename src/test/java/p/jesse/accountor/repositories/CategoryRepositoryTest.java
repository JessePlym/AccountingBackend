package p.jesse.accountor.repositories;

import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.Category;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {CategoryRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"p.jesse.accountor.entities"})
@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindCategoryByName() {
        String categoryName = "bill";
        Category category = new Category("bill");
        categoryRepository.save(category);

        assertThat(categoryRepository.findByName(categoryName)).isPresent();
    }
}