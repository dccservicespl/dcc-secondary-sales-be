package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVersionRepository extends JpaRepository<Version, Long> {
    Optional<Version> findByStatusAndAppType(Boolean status, String appType);
}
