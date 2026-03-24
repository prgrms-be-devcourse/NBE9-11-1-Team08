package com.test08.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.test08.domain.product.dto.ProductRequest;
import com.test08.domain.product.dto.ProductResponse;
import com.test08.domain.product.entity.Product;
import com.test08.domain.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

1
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 등록 시 repository save 호출")
    void createProduct_success() {
        // given: 등록할 상품 요청 데이터 준비
        ProductRequest request = new ProductRequest("Columbia Nariño", 5000, "img.jpg", "커피콩");

        // when: 서비스로 상품 등록 실행
        productService.createProduct(request);

        // then: 상품 엔티티가 저장되는지 확인
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 단건 조회 성공")
    void findProduct_success() {
        // given: 조회 대상 상품이 존재한다고 가정
        Product product = new Product(1, "Columbia Nariño", 5000, "img.jpg", "커피콩");
        given(productRepository.findById(1)).willReturn(Optional.of(product));

        // when: 상품 단건 조회
        ProductResponse result = productService.findProduct(1);

        // then: 엔티티가 응답 DTO로 올바르게 변환되는지 검증
        assertThat(result.getProductId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Columbia Nariño");
        assertThat(result.getPrice()).isEqualTo(5000);
        assertThat(result.getImageUrl()).isEqualTo("img.jpg");
        assertThat(result.getCategory()).isEqualTo("커피콩");
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외 발생")
    void findProduct_fail() {
        // given: 해당 ID 상품이 없는 상황
        given(productRepository.findById(1)).willReturn(Optional.empty());

        // when & then: 서비스가 예외를 던지는지 확인
        assertThatThrownBy(() -> productService.findProduct(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("상품 수정 성공")
    void updateProduct_success() {
        // given: 기존 상품과 수정 요청 데이터 준비
        Product product = new Product(1, "Old Coffee", 3000, "old.jpg", "원두");
        ProductRequest request = new ProductRequest("New Coffee", 6000, "new.jpg", "드립백");
        given(productRepository.findById(1)).willReturn(Optional.of(product));

        // when: 상품 수정 실행
        productService.updateProduct(1, request);

        // then: 엔티티 내부 값이 변경되었는지 확인
        assertThat(product.getName()).isEqualTo("New Coffee");
        assertThat(product.getPrice()).isEqualTo(6000);
        assertThat(product.getImageUrl()).isEqualTo("new.jpg");
        assertThat(product.getCategory()).isEqualTo("드립백");
    }

    @Test
    @DisplayName("상품 삭제 시 repository deleteById 호출")
    void deleteProduct_success() {
        // when: 상품 삭제 실행
        productService.deleteProduct(1);

        // then: 삭제 메서드가 호출되는지 검증
        verify(productRepository).deleteById(1);
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    void findAllProducts_success() {
        // given: 여러 상품이 저장되어 있다고 가정
        Product product1 = new Product(1, "Columbia Nariño", 5000, "img1.jpg", "커피콩");
        Product product2 = new Product(2, "Brazil Serra", 6000, "img2.jpg", "커피콩");
        given(productRepository.findAll()).willReturn(List.of(product1, product2));

        // when: 전체 상품 조회
        List<ProductResponse> result = productService.findAllProducts();

        // then: 엔티티 목록이 DTO 목록으로 변환되는지 확인
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Columbia Nariño");
        assertThat(result.get(1).getName()).isEqualTo("Brazil Serra");
    }
}
