package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.common.constant.ResponseMessage;
import org.example.springbootdeveloper.dto.request.BookRequestDto;
import org.example.springbootdeveloper.dto.request.BookRequestUpdateDto;
import org.example.springbootdeveloper.dto.response.bookResponseDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.entity.Book;
import org.example.springbootdeveloper.entity.Category;
import org.example.springbootdeveloper.repository.BookRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService<PostBookResponseDto> {

    private final BookRepository bookRepository;



    // 1. 게시글 생성(Post)
    public ResponseDto<bookResponseDto> createBook(BookRequestDto requestDto) {
        Book book = new Book(
                null, requestDto.getWriter(), requestDto.getTitle(),
                requestDto.getContent(), requestDto.getCategory()
        );

        Book savedBook = bookRepository.save(book);
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, convertToResponseDto(savedBook));
    }

    /*
    * createBook은 사용자가 보낸 책 정보를 보낸걸 담아서 새로운 책을 만들고 저장함
    * ResponseDto<bookResponseDto>이 반환타입인데 책이 성공적으로
    * 저장되었는지와 저장된책의 정보를 담은 응답을 반환함
    * 새로운 책을 만드는데 각각 작가,제목,내용,카테고리를 Book객체에 넣어줌
    * */

    // 2. 전체 책 조회
    public List<bookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                // .map((book) -> convertToResponseDto(book))
                .collect(Collectors.toList());
    }

    /*
    * 모든 책 정보를 가져와서 리스트형태로 만들어서 반환함
    * 각 책 정보가 bookResponseDto 형식으로 반환되어 리스트로 제공됨
    * bookRepository.findAll으로 책 목록을 가져와서 Book객체들이 있는 리스트를 반환함
    * .stream은 stream으로 책 목록을 변환하여 모든책을 순서대로 처리할 수 있게 해줌
    * .map은 스트림에 있는 각 책을 변환하는 작업을 해주는데
    *  this::convertToResponseDto는 Book객체를 bookResponseDto형식으로 반환하는 메서드를 가리킴
    * 여기서 this::convertToResponseDto 는 각각의 책을 convertToResponseDto메서드를 통해
    * bookResponseDto 형식으로 바꾸겠다
    * .collect(Collectors.toList())는 스트림으로 변환된 데이터를 다시 리스트로 모우는 작업을 함
    * 이렇게 모인 리스트를 getAllBooks로 다시보냄
    * */

    // 3. 특정 ID 책 조회
    public bookResponseDto getBookById(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다: " + id));
            return convertToResponseDto(book);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new bookResponseDto();
        }
    }

    /*
    * 특정 ID로 책을 찾아서 bookResponseDto형식으로 반환함
    * 롱 아이디로 책의 고유번호로 책을 찾음
    *
    * try catch로 예외처리를 함
    * try로 책을 찾는작업을 시행하고, 문제가 발생하면 catch하여 오류 처리
    * 데이터베이스에서 ID로 책을 찾음
    * orElseThrow는 찾으려는 책이 없으면 예외오류를 발생시킴
    * 책이 정상적으로 조회되면 convertToResponseDto(book)메서드를 호출하여 찾는 책을 반환시킴
    * */

    // 3-1. 제목에 특정 단어가 포함된 책 조회
    public List<bookResponseDto> getBooksByTitleContaining(String keyword) {
        List<Book> books = bookRepository.findByTitleContaining(keyword);
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /*
    * 특정 단어가 제목에 포함된 책 목록을 가져와 리스트형태로 반환시킴
    * 데이터베이스에서 제목에 특정 단어가 포함된 책을 찾아옴
    * .stream은 books에 있는 리스트에 있는 책들을 스트림으로 반환시킴
    * */

    // 3-2. 카테고리별 책 조회
    public List<bookResponseDto> getBooksByCategory(Category category) {
        return bookRepository.findByCategory(category)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /*
    * 주어진 카테고리에 해당하는 책 데이터를 찾는 메서드
    * 인터페이스에 findByCategory(Category category) 메서드가 정의되어 있어야 하며
    * .stream 으로 메서드 결과를 스트림으로 변환후 데이터 처리를 쉽게 함
    * .map으로 각 책 엔티티를 convertToResponseDto메서드를 book ResponseDto객체로 변환
    * convertToResponseDto메서드를 별도의 메서드로 bookResponseDto형식으로 매핑하는 역할
    * .collect로 변환된 bookResponseDto객체를 리스트로 수집함
    * */

    // 3-3. 카테고리 & 작성자별 책 조회
    public List<bookResponseDto> getBooksByCategoryAndWriter(Category category, String writer) {
        List<Book> books;

        if (category == null) {
            books = bookRepository.findByWriter(writer);
        } else {
            books = bookRepository.findByCategoryAndWriter(category, writer);
        }

        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /*
    * 책데이터를 저장할 books 리스트를 선언후 if(category == null) 조건문을 선언함
    * writer에 해당하는 메서드를 책 목록으로 검색함
    * bookRepository.findByWriter(writer)메서드는
    * writer필드를 기준으로 책을 검색하는 메서드로 bookRepository 인터페이스에 정의되어 있어야함
    * 아니면 bookRepository.findByCategoryAndWriter(category, writer)를 호출하여
    * 두가지조건을 충족하는 책을 조회하는데 마찬가지로 bookRepository 인터페이스에 정의되어야함
    * books.stream으로 검색된 리스트를 스트림으로 변환후 작업편하게함
    * .map으로 각 책 엔티티를 bookResponseDto 객체로 변환함
    * .collect(Collectors.toList())로 변환된 객체들을 리스트로 수집하여 반환함
    * */

    // 4, 특정 ID 책 수정
    public bookResponseDto updateBook(Long id, BookRequestUpdateDto updateDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다: " + id));

        book.setTitle(updateDto.getTitle());
        book.setContent(updateDto.getContent());
        book.setCategory(updateDto.getCategory());

        Book updatedBook = bookRepository.save(book);
        return convertToResponseDto(updatedBook);
    }

    /*
    * id를 기준으로 책을 조회하고 만약 해당하는 책이 존재하지 않으면
    * 예외를 발생시키며 책을 찾을수 없다는 메시지와 id값을 포함시켜 말해줌
    *
    * updateDto에서 각 제목,내용,카테고리를 가져와 업데이트함
    *
    * 수정된 책을 bookRepository.save(book) 메서드를 통해 저장하여 업데이트된 DB에 반영함
    * updatedBook객체를 bookResponseDto로 변환후 반환
    * */

    // 5. 책 삭제
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    /*
    * bookRepository에서 deleteById를 호출하여 id에 해당하는 책을 삭제함
    * */

    // Entity -> Response Dto 변환
    private bookResponseDto convertToResponseDto(Book book) {
        return new bookResponseDto(
                book.getId(), book.getWriter(), book.getTitle()
                , book.getContent(), book.getCategory()
        );
    }

}

/*
* bookResponseDto의 생성자 호출하여 Book객체의 각 필드를 bookResponseDto에 매핑함
* book.getId(), book.getWriter(), book.getTitle()
*   , book.getContent(), book.getCategory() 등 Book의 객체를 필드값에 추출하여
* bookResponseDto의 필드에 할당함
* */