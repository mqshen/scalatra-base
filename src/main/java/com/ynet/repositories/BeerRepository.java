package com.ynet.repositories;

import com.ynet.domain.Beer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerRepository extends CrudRepository<Beer, String> {

}
