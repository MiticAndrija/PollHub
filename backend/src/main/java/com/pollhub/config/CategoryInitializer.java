package com.pollhub.config;
import com.pollhub.entity.Category; import com.pollhub.repository.CategoryRepository; import lombok.RequiredArgsConstructor; import org.springframework.boot.ApplicationRunner; import org.springframework.context.annotation.*; import java.util.List;
@Configuration @RequiredArgsConstructor public class CategoryInitializer {
 private final CategoryRepository repository;
 @Bean ApplicationRunner initializeCategories(){return args->List.of("Tehnologija","Sport","Zabava","Obrazovanje","Ostalo").forEach(name->{if(repository.findByNameIgnoreCase(name).isEmpty()){Category c=new Category();c.setName(name);repository.save(c);}});}
}
