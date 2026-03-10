package com.dcc.osheaapp.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.service.ProductService;
import com.dcc.osheaapp.vo.ProductCategoryVo;
import com.dcc.osheaapp.vo.ProductInputVo;
import com.dcc.osheaapp.vo.ProductVo;
import com.dcc.osheaapp.vo.dto.ProductVideoLinksDto;
import com.dcc.osheaapp.vo.dto.UpdateProductDto;
import com.dcc.osheaapp.vo.dto.UpdateUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
    originPatterns = {"*"},
    allowedHeaders = "*",
    maxAge = 4800,
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RestController
@RequestMapping("/product")
@Tag(description = "API related to Product", name = "Product")
public class ProductController {

  private static final Logger LOGGER = LogManager.getLogger(ProductController.class);
  private final ProductService productService;
  @Autowired AmazonS3 client;

  @Value("${s3.bucket}")
  String bucketName;

  //    @Operation(summary = "Creates a new User",
  //            description = "Creates a new User")
  //    @RequestMapping(value = "/signup", method = RequestMethod.POST)
  //    public ResponseEntity<ApiResponse> signup(@RequestBody SignUpVo signUpVo) {
  //        return this.userService.signUp(signUpVo);
  //    }
  @Value("${file.selfieUploadFolder}")
  private String selfieUploadFolder;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "getproduct", description = "getproduct")
  @RequestMapping(value = "/getproduct", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> getproduct(HttpServletRequest request) {
    LOGGER.info("ProductController :: getproduct() called......");
    return this.productService.fetchCategory(request);
  }

  @Operation(summary = "Fetch product entry by id", description = "Fetch product entry by id")
  @PreAuthorize("isAuthenticated()")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> fetchById(@PathVariable("id") Long id) {
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.productService.fetchById(id)),
        HttpStatus.OK);
  }

  @Operation(summary = "Fetch product entry by id", description = "Fetch product entry by id")
  @PreAuthorize("isAuthenticated()")
  @RequestMapping(value = "/fetch/{id}", method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> fetchByIdAlt(@PathVariable("id") Long id) {
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(200, "SUCCESS", "FETCH SUCCESSFULL", this.productService.fetchByIdAlt(id)),
        HttpStatus.OK);
  }

  @Operation(summary = "Save a single product", description = "Save a single product")
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> save(@RequestBody ProductVo input) {
    LOGGER.info("ProductController :: save() called......");
    return this.productService.save(input);
  }

  @Operation(summary = "Update product status", description = "Update product status")
  @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> updateStatus(@RequestBody ProductInputVo input) {
    LOGGER.info("ProductController :: save() called......");
    return this.productService.updateStatus(input);
  }

  @Operation(summary = "Search product ", description = "Search product ")
  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> search(@RequestBody ProductInputVo input) {
    LOGGER.info("ProductController :: save() called......");
    return this.productService.searchByInput(input);
  }

  @Operation(
      summary = "Upload Product Files in Directory",
      description = "Upload Product Files in Directory")
  @RequestMapping(value = "/uploadProductFiles", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> uploadProductFiles(
      @RequestParam("transactionId") Long transactionId,
      @RequestParam("tab_name") String tabName,
      @RequestParam(value = "files", required = false) MultipartFile[] files)
      throws IOException {
    LOGGER.info("ProductController :: uploadProductFiles() called......");
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(
            201,
            "CREATED",
            "Uploaded Successfully",
            null != files && files.length > 0
                ? this.productService.uploadProductMedia(transactionId, tabName, files)
                : null,
            0),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Upload Product Files in Directory",
      description = "Upload Product Files in Directory")
  @RequestMapping(value = "/uploadProductLinks", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> uploadProductLinks(@RequestBody ProductVideoLinksDto dto)
      throws IOException {
    LOGGER.info("ProductController :: uploadProductFiles() called......");

    this.productService.uploadProductLinks(dto);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(201, "CREATED", "Uploaded Successfully", null, 0), HttpStatus.OK);
  }

  @Operation(summary = "Fetch File From Folder", description = "Fetch File From Folder")
  @RequestMapping(value = "/files/{fileName}", method = RequestMethod.GET)
  public ResponseEntity<Resource> getProductFile(@PathVariable String fileName) {
    LOGGER.info("ProductController :: getProductImage() called......");
    // ResponseEntity<ApiResponse> apiResponse = null;
    try {
      //        	LOGGER.info("ProductController :: getProductImage() :: File Upload Folder :: " +
      // fileUploadFolder);
      //      Path filePath = Paths.get(selfieUploadFolder + fileName);
      Path filePath = Paths.get("/opt/path/OsheaStorage/" + fileName);
      String ext = FilenameUtils.getExtension(fileName);
      //          LOGGER.info("ProductController :: getProductImage() :: File Extension :: " + ext);
      MediaType fileContentType = null;
      // Change content type according to your file type
      if (ext.equals("pdf")) {
        fileContentType = MediaType.APPLICATION_PDF;
      } else { // if(ext.equals("jpg") || ext.equals("png") || ext.equals("jpeg"))
        fileContentType = MediaType.IMAGE_JPEG;
      }
      //          LOGGER.info("ProductController :: getProductImage() :: File Content Type :: " +
      // fileContentType);
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return ResponseEntity.ok().contentType(fileContentType).body(resource);
      } else {
        return ResponseEntity.notFound().build();
        //            	apiResponse = new ResponseEntity<>(new ApiResponse(404, "SUCCESS", "File Not
        // Found."), HttpStatus.NOT_FOUND);
        //               return apiResponse;
      }
    } catch (MalformedURLException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Save a single category", description = "Save a single category")
  @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> saveCategory(@RequestBody ProductCategoryVo input) {
    LOGGER.info("ProductController :: saveCategory() called......");
    return this.productService.saveCategory(input);
  }

  @Operation(
      summary = "Fetch product list of a outlet for sale",
      description = "Fetch product list of a outlet for sale")
  @RequestMapping(value = "/productForSale", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> productForSale(@RequestBody ProductInputVo input) {
    LOGGER.info("StockController :: productForSale() called......");
    return this.productService.productForSale(input);
  }

  @Operation(
      summary = "Fetch product list of a outlet for purchase return",
      description = "Fetch product list of a outlet for purchase return")
  @RequestMapping(value = "/productForPurchaseReturn", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> productForPurchaseReturn(@RequestBody ProductInputVo input) {
    LOGGER.info("StockController :: productForPurchaseReturn() called......");
    return this.productService.productForPurchaseReturn(input);
  }

  @Operation(
      summary = "Fetch product list of a outlet for sale return",
      description = "Fetch product list of a outlet for sale return")
  @RequestMapping(value = "/productForSaleReturn", method = RequestMethod.POST)
  public ResponseEntity<ApiResponse> productForSaleReturn(@RequestBody ProductInputVo input) {
    LOGGER.info("StockController :: productForSaleReturn() called......");
    return this.productService.productForSaleReturn(input);
  }

  @Operation(summary = "Update Product", description = "Update Product")
  @PutMapping("/update")
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductDto input) {
    LOGGER.info("UserController :: save() called......");
    return new ResponseEntity<>(
            new ApiResponse(200, "UPDATED", "Updated Successfully", this.productService.update(input)),
            HttpStatus.OK);
  }
}
