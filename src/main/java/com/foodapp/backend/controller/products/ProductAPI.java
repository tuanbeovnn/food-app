package com.foodapp.backend.controller.products;

import com.foodapp.backend.dto.request.ProductRequest;
import com.foodapp.backend.dto.response.ProductResponse;
import com.foodapp.backend.pageable.PageList;
import com.foodapp.backend.service.ProductService;
import com.foodapp.backend.utils.FormUtils;
import com.foodapp.backend.utils.ResponseEntityBuilder;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Api(tags = {"Product-Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Product-API-Resource", description = "Write description here")
})
@RestController
@RequestMapping(value = "/api")
public class ProductAPI {

    @Autowired
    private Gson gson;

    @Autowired
    private ProductService productService;

    /**
     * @param files
     * @param data
     * @return
     */
    @RequestMapping(value = "/admin/products/create-product", method = RequestMethod.POST)
    public ResponseEntity<?> createProduct(@RequestPart(name = "files", required = false) MultipartFile[] files,
                                           @RequestPart(name = "file", required = false) MultipartFile file,
                                           @RequestParam String data) {
        ProductRequest productRequest = gson.fromJson(data, ProductRequest.class);
        ProductResponse productResponse = productService.createProduct(file, files, productRequest);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(productResponse)
                .setMessage("Create a product successfully")
                .build();
    }

    @RequestMapping(value = "/admin/products/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getListProduct(@RequestParam Map<String, String> model) {
        Pageable pageable = FormUtils.toPageable(model);
        PageList<ProductResponse> productResponsePageList = productService.findProductByCondition(model.get("code"), pageable);
        return ResponseEntityBuilder.getBuilder().setDetails(productResponsePageList).setMessage("Get list products successfully").build();
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/admin/product/disable/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeCourse(@PathVariable("id") long id) {
        productService.disableProduct(id);
        return ResponseEntityBuilder.getBuilder().setMessage("Disable product successfully").build();
    }

    /**
     * @param productId
     * @return
     */
    @RequestMapping(value = "/admin/product/details", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@RequestParam Long productId) {
        ProductResponse productResponse = productService.findById(productId);
        return ResponseEntityBuilder.getBuilder().setDetails(productResponse).setMessage("Find product successfully").build();
    }

    /**
     * z
     *
     * @param model
     * @return
     */

    @RequestMapping(value = "/product/search", method = RequestMethod.GET)
    public ResponseEntity<?> search(@RequestParam Map<String, String> model, Long categoryId) {
        Pageable pageable = FormUtils.toPageable(model);
        ProductRequest filmSearchRequestInput = FormUtils.toModel(ProductRequest.class, model);
        PageList<ProductResponse> lstOutput = productService.searchProduct(filmSearchRequestInput, categoryId, pageable);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(lstOutput)
                .setMessage("GET LIST SUCCESSFULLY")
                .build();
    }

}
