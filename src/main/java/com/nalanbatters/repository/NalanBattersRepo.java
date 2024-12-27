package com.nalanbatters.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nalanbatters.model.NalanBattersModel;

public interface NalanBattersRepo extends JpaRepository<NalanBattersModel, Long>{

}
