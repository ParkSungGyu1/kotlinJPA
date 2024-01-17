package com.example.jpatest

import jakarta.persistence.Persistence
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JpaTestController {

    @GetMapping("/practice1")
    fun practice1(){
        val emf = Persistence.createEntityManagerFactory("hello")
        val em = emf.createEntityManager()

        val member = Member()
        member.id = 101L
        member.title = "테스트맨"

        val transaction = em.transaction
        transaction.begin()

        println("BEFORE")
        em.persist(member)
        println("AFTER")

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
        member.id = 101L
        member.title = "회원"

        val transaction = em.transaction
        transaction.begin()

        //1차 캐시에 저장됨
        em.persist(member)

        //3번 멤버 조회
        val member1 = em.find(Member().javaClass, 101L)

        println("조회 결과 : " + member1.title)

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

        //100번 멤버 조회
        val member1 = em.find(Member().javaClass, 100L)
        //100번 멤버 조회
        val member2 = em.find(Member().javaClass, 100L)

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
        member1.id = 1000L
        member1.title = "회원님1"
        val member2 = Member()
        member2.id = 1001L
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

        //100번 멤버 조회
        val member1 = em.find(Member().javaClass, 100L)
        member1.title = "바뀔까"

        transaction.commit()
        em.close()
        emf.close()
    }
}