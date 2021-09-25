package com.reeco.transport.infrastructure.persistence.postgresql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresFTPRepository extends JpaRepository<FtpEntity, Integer> {
    @Query(value = "SELECT ftp.id FROM public.ftp ftp WHERE ftp.username = ?1",nativeQuery = true)
    Integer findFtpByUserName(String username);
}
