package com.ynet.web.controller

import com.ynet.annotations.ServletPath
import com.ynet.services.BeerService
import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

/**
 * Created by goldratio on 3/3/15.
 */
@Controller
@ServletPath("/beer")
class BeerController extends ScalatraServlet with ScalateSupport {

  @Autowired val beerService: BeerService = null

  get("/") {
    val allBeers = beerService.allBeers()
    beer.html.list(allBeers)
  }

}
