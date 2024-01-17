package com.example.jpatest

import jakarta.persistence.Persistence
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JpaTestController {

    @GetMapping("/practice1")
    fun practice1(){
        // JPA 기능을 가지고 있다 (영속성 컨텍스트)
        val emf = Persistence.createEntityManagerFactory("hello")
        val em = emf.createEntityManager()

        // 비영속 상태
        val member = Member()
        member.id = 103L
        member.title = "테스트맨"

        // transaction
        val transaction = em.transaction
        transaction.begin()

        println("BEFORE")  // before 찍힘
        em.persist(member) // insert 날라감
        println("AFTER") // after 찍힘

        transaction.commit()

        em.close()
        emf.close()
    }

    /**
     * 1차 캐시
     */
    @GetMapping("/practice2")
    fun practice2(){

        // EntityManager 생성
        val emf = Persistence.createEntityManagerFactory("hello")
        val em = emf.createEntityManager()


        //객체를 생성한 상태(비영속)
        val member = Member()
        member.id = 1001L
        member.title = "회원"

        val transaction = em.transaction
        transaction.begin()

        //1차 캐시에 저장됨
        em.persist(member) // insert

        //3번 멤버 조회
        val member1 = em.find(Member().javaClass, 103L)  // select

        println("조회 결과 : " + member1.title) // println

        transaction.commit()
        em.close()
        emf.close()
    }

    /**
     * 영속성 엔티티의 동일성 보장
     */
    @GetMapping("/practice3")
    fun practice3(){

        // EntityManager 생성
        val emf = Persistence.createEntityManagerFactory("hello")
        val em = emf.createEntityManager()

        val transaction = em.transaction
        transaction.begin()

        //103번 멤버 조회
        val member1 = em.find(Member().javaClass, 103L)
        //103번 멤버 조회
        val member2 = em.find(Member().javaClass, 103L)

        println("비교 결과 : " + (member1 === member2))

        transaction.commit()
        em.close()
        emf.close()

    }

    /**
     * 트랜잭션을 지원하는 쓰기지연
     */

    @GetMapping("/practice4")
    fun practice4(){

        // EntityManager 생성
        val emf = Persistence.createEntityManagerFactory("hello")
        val em = emf.createEntityManager()

        val transaction = em.transaction
        transaction.begin()


        val member1 = Member()
        member1.id = 10000L
        member1.title = "회원님1"

        val member2 = Member()
        member2.id = 10001L
        member2.title = "회원님2"

        // 이 때 Insert 쿼리를 보내게 될까?
        em.persist(member1)
        em.persist(member2)


        println("=============================")

        transaction.commit()
        em.close()
        emf.close()
    }
    /**
     * 변경 감지 Dirty Checking
     */
    @GetMapping("/practice5")
    fun practice5(){
        // EntityManager 생성
        val emf = Persistence.createEntityManagerFactory("hello")
        val em = emf.createEntityManager()

        val transaction = em.transaction
        transaction.begin()

        //103번 멤버 조회
        val member1 = em.find(Member().javaClass, 103L)
        member1.title = "바뀔까"

        transaction.commit()
        em.close()
        emf.close()
    }
}