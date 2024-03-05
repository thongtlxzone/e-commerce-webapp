package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.ProductEntity;
import com.project.shopapp.models.ProductImageEntity;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.services.ProductService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final LocalizationUtils localizationUtils;

    private String storedFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename()!=null){
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + " " + filename; // Thêm UUID vào trước tên file để tránh trùng tên file
        Path uploadDir = Paths.get("uploads"); // Đường dẫn tới thư mục mà bạn muốn lưu file.
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);//Đường dẫn đầy đủ đến file
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING); //Sao chép file vào thư mục đích
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @PostMapping("")
    public ResponseEntity<?> createProducts(@Valid @RequestBody ProductDTO  productDTO,
//                                         @ModelAttribute("files") MultipartFile files,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            ProductEntity newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") long ProductId,
            @ModelAttribute("files") List<MultipartFile> files)
    {
        try {
            ProductEntity existingProduct = productService.getProductById(ProductId);
            files = files == null ? new ArrayList<MultipartFile>():files;
            if (files.size() > ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            List<ProductImageEntity> productImages = new ArrayList<>();
            for (MultipartFile file : files){
                if(file != null){
                    if(file.getSize()==0){
                        continue;
                    }
                    //Kiểm tra kích thước và định dạng
                    if(file.getSize() > 10*1024*1024){ // size > 10mb
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is to large! max size = 10mb");
                    }
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image/")){
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
                    }
                    //Lưu file và cập nhật thumbnail trong DTO
                    String filename = storedFile(file);
                    //Lưu vào đối tượng product trong DB
                    ProductImageEntity productImage = productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .imgUrl(filename)
                                    .build());
                    productImages.add(productImage);
                }
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
//                Sort.by("createdAt").descending()
                Sort.by("id").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        //Lấy tổng số trang
        int totalPage = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPage)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") long productId
    ){
        try {
            ProductEntity existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_DELETE_PRODUCT_SUCCESSFULLY)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
        @PathVariable long id,
        @RequestBody ProductDTO productDTO
    ){
        try {
            ProductEntity updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @PostMapping("/generateFakeProducts")
    private ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i<1000000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 9000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(2,5))
                    .thumbnail("")
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake product created successfully");
    }
}
