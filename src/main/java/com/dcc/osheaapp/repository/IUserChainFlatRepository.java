package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.report.common.model.UserChainFlat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserChainFlatRepository extends JpaRepository<UserChainFlat, Long> {

    Optional<UserChainFlat> findByBaId(Long id);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where baId in (?1)")
    List<UserChainFlat> findAllByBaId(List<Long> baIds);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where asmId in (?1)")
    List<UserChainFlat> findByAsmId(Long asmIds);


    @Query(nativeQuery = true, value = "select * from UserChainFlat where aseId in (?1)")
    List<UserChainFlat> findByAseId(Long aseId);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where bdmId in (?1)")
    List<UserChainFlat> findByBdmId(Long bdmId);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where bdeId in (?1)")
    List<UserChainFlat> findByBdeId(Long bdeId);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where nsmId in (?1)")
    List<UserChainFlat> findByNsmId(Long nsmId);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where soId in (?1)")
    List<UserChainFlat> findBySoId(Long soId);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where zsmId in (?1)")
    List<UserChainFlat> findByZsmId(Long zsmId);

    @Query(nativeQuery = true, value = "select * from UserChainFlat where rsmId in (?1)")
    List<UserChainFlat> findByRsmId(Long rsmId);



//    @Query(nativeQuery = true, value = "select * from UserChainFlat where baId in (?1)")
//    UserChainFlat findByBaId(Long id);
}
