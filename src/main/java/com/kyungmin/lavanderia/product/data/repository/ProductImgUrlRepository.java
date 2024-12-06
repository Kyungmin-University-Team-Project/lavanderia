package com.kyungmin.lavanderia.product.data.repository;

import com.kyungmin.lavanderia.product.data.entity.ProductImgUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImgUrlRepository extends JpaRepository<ProductImgUrl, Long> {
}
