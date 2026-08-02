package com.pollhub.service;
import com.pollhub.dto.*; import com.pollhub.entity.Category; import com.pollhub.exception.*; import com.pollhub.repository.CategoryRepository;
import lombok.RequiredArgsConstructor; import org.springframework.dao.DataIntegrityViolationException; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service @RequiredArgsConstructor public class CategoryService {
 private final CategoryRepository repository;
 @Transactional(readOnly=true) public List<CategoryResponse> all(){return repository.findAll().stream().map(this::response).toList();}
 @Transactional public CategoryResponse create(CategoryRequest r){if(repository.findByNameIgnoreCase(r.name().trim()).isPresent())throw new BusinessRuleException("Category already exists"); Category c=new Category();apply(c,r);return response(repository.save(c));}
 @Transactional public CategoryResponse update(Long id,CategoryRequest r){Category c=get(id);apply(c,r);return response(c);}
 @Transactional public void delete(Long id){try{repository.delete(get(id));repository.flush();}catch(DataIntegrityViolationException e){throw new BusinessRuleException("Category is used by polls");}}
 private Category get(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found"));}
 private void apply(Category c,CategoryRequest r){c.setName(r.name().trim());c.setDescription(r.description()==null?null:r.description().trim());}
 private CategoryResponse response(Category c){return new CategoryResponse(c.getId(),c.getName(),c.getDescription());}
}
