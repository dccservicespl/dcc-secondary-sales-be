package com.dcc.osheaapp.common.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends JpaRepository<T, ID> {
  Optional<T> findById(ID id);

  List<T> findAll();
}
