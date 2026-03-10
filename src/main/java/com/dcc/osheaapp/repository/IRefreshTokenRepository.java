package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUsername(String username);
}
