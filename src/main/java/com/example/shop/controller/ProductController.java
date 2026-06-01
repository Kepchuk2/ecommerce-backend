package com.example.shop.controller;

import com.example.shop.dto.product.*;
import com.example.shop.entity.ProductCategory;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable Long productId) {
        return productService.getByProductId(productId);
    }

    @GetMapping
    public List<ProductListResponse> getProducts(@RequestParam(required = false) ProductCategory category) {
        if (category == null) {
            return productService.getAllProducts();
        }

        return productService.getProductsByCategory(category);
    }

    @GetMapping("/sku/{sku}")
    public ProductVariantResponse getProductVariantBySku(@PathVariable String sku) {
        return productService.getVariantBySku(sku);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProductsByName(@RequestParam String productName) {
        return productService.searchProductByName(productName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request.name(), request.description(), request.category());
    }

    @PostMapping("/{productId}/variants")
    public ProductVariantResponse addVariantToProduct(@PathVariable Long productId, @Valid @RequestBody AddProductVariantRequest request) {
        return productService.addVariantToProduct(productId, request.sku(), request.price(), request.size(), request.color());
    }

    @DeleteMapping("/{productId}/variants/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeVariantFromProduct(@PathVariable Long productId, @PathVariable Long variantId) {
        productService.removeVariantFromProduct(productId, variantId);
    }

    @PostMapping("/{productId}/images")
    public ProductResponse addImageToProduct(@PathVariable Long productId, @Valid @RequestBody AddProductImageRequest request) {
        return productService.addImageToProduct(productId, request.imageUrl(), request.altText(), request.position());
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeImageFromProduct(@PathVariable Long productId, @PathVariable Long imageId) {
        productService.removeImageFromProduct(productId, imageId);
    }

    @PutMapping("/variants/{variantId}/price")
    public ProductVariantResponse changeVariantPrice(@PathVariable Long variantId, @Valid @RequestBody ChangeVariantPriceRequest request) {
        return productService.changeVariantPrice(variantId, request.newPrice());
    }
}
