package com.dcc.osheaapp.managerial.Repository;

import com.dcc.osheaapp.managerial.vo.EditRequestReportVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EditRequestReportRepo extends CrudRepository<EditRequestReportVo, Long> {
	@Query(nativeQuery = true, value = "select count(*) FROM user_assotiation_details uad\n" +
			"        INNER JOIN  user_details u on uad.user_id = u.id\n" +
			"        INNER JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad.user_id\n" +
			"        INNER JOIN outlet o on oum.outlet_id = o.id\n" +
			"        INNER JOIN user_credential uc on uc.id = u.user_cred \n" +
			"        INNER JOIN stock_entry se on se.user_id = uad.user_id\n" +
			"        where uad.assotiated_user_id = :bdeId and se.stock_status in ('Edit', 'Prev_Adj') and \n" +
			"        activity_type in ('stock', 'purchase', 'sale')")
	Long fetchEditRequestCountOfBaUnderBde(Long bdeId);

	@Query(nativeQuery = true, value = "select se.id as stock_id, se.updated_on, se.activity_type as activity_type, se.outlet_id, o.outlet_name, o.outlet_code,\n" +
			"        se.stock_status, se.transaction_date, se.user_id, u.full_name ba_name,\n" +
			"        SUBSTRING_INDEX(uc.username, '@', 1) as ba_code,\n" +
			"        se.total_amount_of_item as total_amount_items, se.total_no_of_item as total_no_of_items,\n" +
			"        se.total_amount_of_item_updated as total_amount_of_item_updated, se.total_no_of_item_updated as total_no_of_item_updated\n" +
			"        FROM user_assotiation_details uad\n" +
			"        INNER JOIN  user_details u on uad.user_id = u.id\n" +
			"        INNER JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad.user_id\n" +
			"        INNER JOIN outlet o on oum.outlet_id = o.id\n" +
			"        INNER JOIN user_credential uc on uc.id = u.user_cred \n" +
			"        INNER JOIN stock_entry se on se.user_id = uad.user_id\n" +
			"        where uad.assotiated_user_id = :bdeId and se.stock_status in ('Edit', 'Prev_Adj') and \n" +
			"        activity_type in ('stock', 'purchase', 'sale')\n" +
			"        ")
	List<EditRequestReportVo> fetchEditRequestListOfBaUnderBde(Long bdeId);

	@Query(nativeQuery = true, value = "select se.id as stock_id, se.updated_on, se.activity_type as activity_type, se.outlet_id, o.outlet_name, o.outlet_code,\n" +
			"        se.stock_status, se.transaction_date, se.user_id, u.full_name ba_name,\n" +
			"        SUBSTRING_INDEX(uc.username, '@', 1) as ba_code,\n" +
			"        se.total_amount_of_item as total_amount_items, se.total_no_of_item as total_no_of_items,\n" +
			"        se.total_amount_of_item_updated as total_amount_of_item_updated, se.total_no_of_item_updated as total_no_of_item_updated\n" +
			"        FROM stock_entry se\n" +
			"        INNER JOIN  user_details u on se.user_id = u.id\n" +
			"        INNER JOIN outlet o on se.outlet_id = o.id\n" +
			"        INNER JOIN user_credential uc on uc.id = u.user_cred \n" +
			"        where se.user_id = :baId and se.stock_status in ('Edit', 'Admin Approved', 'Admin Rejected', 'Prev_Adj')\n" +
			"        and \n" +
			"        activity_type in ('stock', 'purchase', 'sale')")
	List<EditRequestReportVo> fetchEditRequestListOfBa(Long baId);
}