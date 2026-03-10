package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.ProductCategoryVo;
import com.dcc.osheaapp.vo.views.ProductCategoryView;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductCategoryRepository extends JpaRepository<ProductCategoryVo, Long> {

	List<ProductCategoryVo> findAll();

	@Query(nativeQuery = true, value = "SELECT * FROM product_category where parent_id = :parentId and is_active = true")
	List<ProductCategoryVo> getDataByParentId(Long parentId);

	@Query(nativeQuery = true, value = "SELECT distinct a.* "
			+ " FROM product_category a, product b, counter_stock_manage c "
			+ " where a.ID = b.category_id and b.ID = c.product_id and"
			+ " outlet_id = :outletId and parent_id = :parentId " + " and a.is_active = true and b.is_active = true")
	List<ProductCategoryVo> getProductCategoryInOutlet(Long parentId, Long outletId);

	Optional<ProductCategoryVo> findByCategoryNameAndParentId(String categoryName, Long parentId);

	// @Query(nativeQuery = true, value = "SELECT id, category_name FROM
	// product_category where
	// parent_id = :parentId and is_active = true")
	List<ProductCategoryView> findByParentIdAndIsActive(Long parentId, boolean isActive);

	@Query(nativeQuery = true, value = "SELECT distinct a.* "
			+ " FROM product_category a, product b, counter_stock_manage c "
			+ " where a.ID = b.category_id and b.ID = c.product_id and"
			+ " outlet_id = :outletId and parent_id = :parentId " + " and a.is_active = true and b.is_active = true"
			+ " and DATE_FORMAT(ifnull(c.transaction_date, c.created_on),'%Y-%m') = :monYr")
	List<ProductCategoryVo> getProductCategoryInOutletMonYr(Long parentId, Long outletId, String monYr);
}
