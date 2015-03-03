package com.ynet.services


import com.ynet.repositories.BeerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by goldratio on 3/3/15.
 */
@Component("beerService")
class BeerService {

  private val DEFAULT_LIMIT = 100

  @Autowired val beerRepo: BeerRepository = null

  def allBeers(limit: Int = DEFAULT_LIMIT) = {
    beerRepo.findAll()
  }

}
