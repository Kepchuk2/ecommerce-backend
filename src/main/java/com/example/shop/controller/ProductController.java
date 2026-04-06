package com.example.shop.controller;

import com.example.shop.dto.product.*;
import com.example.shop.entity.Product;
import com.example.shop.entity.ProductCategory;
import com.example.shop.entity.ProductVariant;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable Long productId) {
        Product product = productService.getByProductId(productId);
        return ProductMapper.toProductResponse(product);
    }

    @GetMapping("/category/{category}")
    public List<ProductResponse> getProductsByCategory(@PathVariable ProductCategory category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ProductMapper.toProductResponseList(products);
    }

    @GetMapping("/sku/{sku}")
    public ProductVariantResponse getProductVariantBySku(@PathVariable String sku) {
        ProductVariant variant = productService.getVariantBySku(sku);
        return ProductMapper.toProductVariantResponse(variant);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProductsByName(@RequestParam String productName) {
        List<Product> products = productService.searchProductByName(productName);
        return ProductMapper.toProductResponseList(products);
    }

    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request.getName(), request.getDescription(), request.getCategory());
        return ProductMapper.toProductResponse(product);
    }

    @PostMapping("/{productId}/variants")
    public ProductVariantResponse addVariantToProduct(@Valid @PathVariable Long productId, @RequestBody AddProductVariantRequest request) {
        ProductVariant variant = productService.addVariantToProduct(productId, request.getSku(), request.getPrice(), request.getSize(), request.getColor());
        return ProductMapper.toProductVariantResponse(variant);
    }

    @DeleteMapping("/{productId}/variants/{variantId}")
    public ProductVariantResponse removeVariantFromProduct(@PathVariable Long productId, @PathVariable Long variantId) {
        ProductVariant variant = productService.removeVariantFromProduct(productId, variantId);
        return ProductMapper.toProductVariantResponse(variant);
    }

    @PostMapping("/{productId}/images")
    public ProductResponse addImageToProduct(@Valid @PathVariable Long productId, @RequestBody AddProductImageRequest request) {
        Product product =productService.addImageToProduct(productId, request.getImageUrl(), request.getAltText(), request.getPosition());
        return ProductMapper.toProductResponse(product);
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ProductResponse removeImageFromProduct(@PathVariable Long productId, @PathVariable Long imageId) {
        Product product =  productService.removeImageFromProduct(productId, imageId);
        return ProductMapper.toProductResponse(product);
    }

    @PutMapping("/variants/{variantId}/price")
    public ProductVariantResponse changeVariantPrice(@Valid @PathVariable Long variantId, @RequestBody ChangeVariantPriceRequest request) {
        ProductVariant variant = productService.changeVariantPrice(variantId, request.getNewPrice());
        return ProductMapper.toProductVariantResponse(variant);
    }
}
