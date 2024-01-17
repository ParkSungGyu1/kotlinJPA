package com.example.jpatest

import jakarta.persistence.*

@Entity
class Member (


){
    @Id
    var id: Long = 0

    @Column(name = "title")
    var title: String = ""

}