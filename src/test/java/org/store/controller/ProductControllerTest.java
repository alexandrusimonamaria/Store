package org.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.store.config.SecurityConfig;
import org.store.config.SecurityVariables;
import org.store.dto.ProductDto;
import org.store.entity.ProductEntity;
import org.store.exception.ProductNotFoundException;
import org.store.service.ProductService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, SecurityVariables.class})
@TestPropertySource(properties = {
        "USER_PASSWORD=testpass", "ADMIN_PASSWORD=testpass",
        "spring.security.user.name=user", "spring.security.admin.name=admin"
})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductEntity product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(4000.0);
        product.setCategory("Electronics");

        productDto = new ProductDto("Laptop", "Gaming laptop", 4000.0, "Electronics");
    }

    @Test
    @WithMockUser
    void getAllProductsUserAuthenticated() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop")) //check the element content from the list
                .andExpect(jsonPath("$[0].price").value(4000.0));
    }

    @Test
    void getAllProductsWhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getProductByIdWhenNotFound() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ProductNotFoundException("Product with id 99 not found"));

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product with id 99 not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProductAdminRole() throws Exception {
        when(productService.addProduct(any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop")) //check object content
                .andExpect(jsonPath("$.price").value(4000.0));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createProductUserRole() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProductPrice() throws Exception {
        product.setPrice(5000.0);
        when(productService.updateProduct(eq(1L), eq(5000.0))).thenReturn(product);

        mockMvc.perform(patch("/products/1/price")
                        .param("newPrice", "5000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(5000.0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProductWithInvalidBody() throws Exception {
        ProductDto invalidDto = new ProductDto("", "desc", -10.0, "cat");

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
