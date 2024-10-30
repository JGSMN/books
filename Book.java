package org.example.springbootdeveloper.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String writer;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    // JPA에서 열거형 데이터를 DB에 저장할 때 방식을 지정
    // : enum의 이름을 문자열로 저장
    @Column(nullable = false)
    private Category category;

    public Book(Object o, String bookTitle, String bookAuthor, Category bookCategory) {
    }

    /*
    * Book 이라는 클래스에 id라는 고유번호와 GeneratedValue로 고유번호를 책이 추가될때마다 자동 생성해줌
    * Column으로 뒤에 붙어있는 변수들을 열으로 저장
    * nullable = false 를 붙임으로써 비워둘수 없는 필드로 만듬
    * length로 최대 몇글자인지 지정
    * @Enumerated 어노테이션은 카테고리를 문자열로 데이터베이스에 저장하겠다는 의미
    * public Book은 새로운 책을 만들때 사용하는 특별한 메서드이 () 안에 내용을 담음
    * */
}