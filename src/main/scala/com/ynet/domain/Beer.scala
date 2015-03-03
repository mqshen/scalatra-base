package com.ynet.domain

import javax.persistence.{GenerationType, Id, Table, Entity, GeneratedValue}
/**
 * Created by goldratio on 3/3/15.
 */
@Entity
@Table(name = "beer")
class Beer extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0


}
