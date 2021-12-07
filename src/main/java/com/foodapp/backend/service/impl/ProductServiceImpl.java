package com.foodapp.backend.service.impl;


import com.foodapp.backend.dto.request.ProductRequest;
import com.foodapp.backend.dto.response.ProductResponse;
import com.foodapp.backend.entity.CategoryEntity;
import com.foodapp.backend.entity.ProductEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.pageable.PageList;
import com.foodapp.backend.repository.CategoryRepository;
import com.foodapp.backend.repository.ProductRepository;
import com.foodapp.backend.repository.custom.ProductRepositoryCustom;
import com.foodapp.backend.service.ProductService;
import com.foodapp.backend.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.foodapp.backend.constants.Constants.Status.ACTIVE;
import static com.foodapp.backend.constants.Constants.Status.DELETED;

@Service
public class ProductServiceImpl implements ProductService {

    private final FileServiceImpl fileService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final EntityManager entityManager;

    @Autowired
    public ProductServiceImpl(FileServiceImpl fileService,
                              ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ProductRepositoryCustom productRepositoryCustom,
                              EntityManager entityManager) {
        this.fileService = fileService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productRepositoryCustom = productRepositoryCustom;
        this.entityManager = entityManager;
    }


    @Override
    @Transactional
    public ProductResponse createProduct(MultipartFile highlights, MultipartFile[] files, ProductRequest productRequest) {
        ProductEntity productEntity = Converter.toModel(productRequest, ProductEntity.class);
        String highLightImg = fileService.singleFile(highlights);
        String images = String.join(";", fileService.listFiles(files));
        productEntity.setImgHighlight(highLightImg);
        productEntity.setImage(images);
        productEntity.setStatus(ACTIVE);
        CategoryEntity categoryEntity = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        productEntity.setCategory(categoryEntity);
        ProductEntity insertData = productRepository.save(productEntity);

        return Converter.toModel(insertData, ProductResponse.class);
    }

    @Override
    public PageList<ProductResponse> findProductByCondition(String code, Pageable pageable) {
        PageList<ProductResponse> pageList = new PageList<>();
        Map<String, Object> params = new HashMap<>();
        if (!"".equals(code)) {
            params.put("code", code);
        }
        List<ProductEntity> productEntities = productRepositoryCustom.findProductByCondition(params, pageable);
        List<ProductResponse> productResponses = Converter.toList(productEntities, ProductResponse.class);
        Long count = productRepositoryCustom.countProductByCondition(code);
        return buildPaginatingResponse(productResponses, pageable.getPageSize(), pageable.getPageNumber(), count);
    }

    @Override
    @Transactional
    public void disableProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        productEntity.setStatus(DELETED);
        productRepository.save(productEntity);
    }

    @Override
    public ProductResponse findById(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        return Converter.toModel(productEntity, ProductResponse.class);
    }

    @Override
    public PageList<ProductResponse> searchProduct(ProductRequest productRequest, Long categoryId, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> productEntityCriteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> productEntityRoot = productEntityCriteriaQuery.from(ProductEntity.class);
        Join<Object, Object> joinCategory = productEntityRoot.join("category", JoinType.INNER);
        String name = productRequest.getProductName();
        List<Predicate> searchCriteria = new ArrayList<>();
        searchCriteria(searchCriteria, categoryId, criteriaBuilder, name, joinCategory, productEntityRoot);
        productEntityCriteriaQuery.select(productEntityRoot).where(searchCriteria.toArray(new Predicate[0]));
        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(productEntityCriteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ProductResponse> lstResponse = Converter.toList(typedQuery.getResultList(), ProductResponse.class);

        long count = countProduct(criteriaBuilder, searchCriteria);

        return buildPaginatingResponse(lstResponse, pageable.getPageSize(), pageable.getPageNumber(), count);
    }

    private PageList<ProductResponse> buildPaginatingResponse(List<ProductResponse> responses, int pageSize, int currentPage, long total) {
        return PageList.<ProductResponse>builder()
                .list(responses)
                .pageSize(pageSize)
                .currentPage(currentPage)
                .success(true)
                .total(total)
                .totalPage((int) Math.ceil(total * 1.0 / pageSize))
                .build();
    }

    private void searchCriteria(List<Predicate> searchCriteria, Long categoryId, CriteriaBuilder criteriaBuilder,
                                String name, Join<Object, Object> joinCategory, Root<ProductEntity> productEntityRoot) {
        searchCriteria.add(criteriaBuilder.like(productEntityRoot.get("status"), "ACTIVE"));
        if (categoryId != null) {
            searchCriteria.add(criteriaBuilder.equal(joinCategory.get("id"), categoryId));
        }
        if (!"".equals(name)) {
            searchCriteria.add(criteriaBuilder.like(productEntityRoot.get("productName"), "%" + name + "%"));
        }
    }

    private long countProduct(CriteriaBuilder criteriaBuilder, List<Predicate> searchCriteria) {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> productEntityCategory = cq.from(ProductEntity.class);
        cq.select(criteriaBuilder.count(productEntityCategory));
        cq.where(searchCriteria.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getSingleResult();
    }

}
