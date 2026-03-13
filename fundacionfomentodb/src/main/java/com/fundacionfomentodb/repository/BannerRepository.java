package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    List<Banner> findByActivoTrueOrderByOrdenAsc();
    List<Banner> findAllByOrderByOrdenAsc();
}