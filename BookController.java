package org.example.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.common.constant.ApiMappingPattern;
import org.example.springbootdeveloper.dto.request.BookRequestDto;
import org.example.springbootdeveloper.dto.request.BookRequestUpdateDto;
import org.example.springbootdeveloper.dto.response.bookResponseDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.entity.Category;
import org.example.springbootdeveloper.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.BOOK)
@RequiredArgsConstructor
// 초기화 되지 않은 final 필드나 @NonNull이 붙은 필드에 대해 생성자를 생성
public class BookController {
    /*
    *  공용 클래스 북컨트롤러에서 불러온다
    * */

    // Service 객체를 주입 받아 사용하는 변수
    private final BookService bookService;

    /*
    * 나만볼수있는 상태의 북서비스를 값을 변경할수 없는 상태로 불러온다
    * */

    // 생성자 주입 - RequiredArgsConstructor로 대체
//    public BookController(BookService bookService) {
//        this.bookService = bookService;
//    }

    // 책 생성
    @PostMapping
    public ResponseEntity<ResponseDto<bookResponseDto>> createBook(@RequestBody BookRequestDto requestDto) {
        ResponseDto<bookResponseDto> result = bookService.createBook(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    /*
    * 포스트매핑 어노테이션을 이용해서 HTTP POST 요청이 들어올때 실행되고
    * ResponseEntity를 통해 ResponseDto<bookResponseDto>라는 데이터를 응답으로 보냄
    * createBook(선언함) @RequestBody 클라이언트가 데이터를 보내면
    * BookRequestDto 객체에 정보를 넣어라
    * ResponseDto<bookREsponseDto>를 통해 결과를 만든것
    * = bookService.createBook(requestDto) 가 실행되면 result에 넣어라
    * result에 응답정보를 담으면
    * 응답내용을 body(result)에 담아서 데이터를 보내주는데
    * 요청이 성공했고 클라이언트에게 응답을 만들어 보낸다
    * */

    // 전체 책 조회
    @GetMapping
    public ResponseEntity<List<bookResponseDto>> getAllBooks() {
        List<bookResponseDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /*
    * 겟매핑 어노테이션 은 GET요청이 들어오면 메서드 실행 하라는 표시이고
    * 실행시 getAllBooks를 실행해 이전 데이터를 조회함
    * bookResponseDto에 담겨있는 메서드를 전부 호출해 모든 책을 가져옴
    * List로 books라는 변수를 호출하고  bookService를 이용해getAllBooks를 불러온다
    * 응답을 클라이언트에게 반환하는데 책목록 데이터를 보냄
    *
    * */



    // 단건 책 조회
    @GetMapping("/{id}")
    public ResponseEntity<bookResponseDto> getBookById(@PathVariable Long id) {
        bookResponseDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /*
    * 겟매핑 어노테이션에 /{id}라는 특정 id가 포함되어야 하고
    * ResponseEntity<bookResponseDto>는 책 정보하나를 불러옴
    * @PathVariable Long id 는 주소에서 받아온 값을 id라는 변수로 사용하겠다 라는 의미이고
    * book이라는 변수를 bookResponseDto로 지정해주고 id로 책을 찾아서 그 책을 book이라는 변수에 저장함
    * 찾은 책을 클라이언트에게 반환함
    * */

    // 제목에 특정 단어가 포함된 책 조회
    @GetMapping("/search/title")
    public ResponseEntity<List<bookResponseDto>> getBooksByTitleContaining(
            @RequestParam String keyword
    ) {
        List<bookResponseDto> books = bookService.getBooksByTitleContaining(keyword);
        return ResponseEntity.ok(books);
    }

    /*
    * 겟매핑 어노테이션에 /search/title 경로로 들어올때 메서드 실행
    * @RequestParam은 주소에서 전달된 값을 메서드의 변수로 사용
    * bookService.getBooksByTitleContaining(keyword) 를 실행시키고 거기서 받은 키워드를
    * books라는 변수에 저장하는 것이다. bookResponseDto타입의 객체가 books에 담기게되며 책 정보가 여러개가 된다
    * */

    // 카테고리별 책 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<bookResponseDto>> getBooksByCategory(@PathVariable Category category) {
        List<bookResponseDto> books = bookService.getBooksByCategory(category);
        return ResponseEntity.ok(books);
    }

    /*
    * 겟매핑 어노테이션에 /category/{category} 경로로 들어오면 메서드 실행
    * @PathVariable으로 주소의 {category}부분을 category 라는 변수로 사용하자 하는 의미이다
    * bookService의 getBooksByCategory 메서드를 호출하여 특정 카테고리에 있는 책들을 찾는다
    * 찾은 책 목록을 응답(books)으로 반환함
    * */

    // 카테고리와 작성자별 책 조회
    @GetMapping("/search/category-writer")
    public ResponseEntity<List<bookResponseDto>> getBooksByCategoryAndWriter(
            @RequestParam(required = false) Category category,
            @RequestParam String writer
    ) {
        List<bookResponseDto> books = bookService.getBooksByCategoryAndWriter(category, writer);
        return ResponseEntity.ok(books);
    }

    /*
    * 겟매핑 어노테이션에 /search/category-writer 경로로 들어오면 메서드 실행
    * @RequestParam 으로 주소에서 전달된 값을 메서드의 변수로 사용한다
    * @RequestParam 뒤에 writer 를 부르는데 작가이름을 받기 위해 부르는 변수이다
    * bookService 의 getBooksByCategoryAndWriter 를 호출하여 카테고리와 작가이름으로 책을 검색한다
    * 검색된 책 목록 반환
    * */

    // 특정 id 책 수정
    @PutMapping("/{id}")
    public ResponseEntity<bookResponseDto> updateBook(
            @PathVariable Long id, @RequestBody BookRequestUpdateDto requestDto
    ) {
        bookResponseDto updatedBook = bookService.updateBook(id, requestDto);
        return ResponseEntity.ok(updatedBook);
    }

    /*
    * 겟매핑 어노테이션 실행시 /{id}경로로 들어오면 메서드 실행
    * @PathVariable 을 이용해 주소에서 전달된 값을 메서드의 변수로 사용할때 id의 값이 들어옴 id는 책의 고유번호
    * @RequestBody를 사용하여 클라이언트가 보낸 데이터를 메서드의 변수로 씀
    * BookRequestUpdateDto는 사용자가 수정하는 책의 정보를 담은 객체
    * requestDto에 수정된 정보가 저장됨
    *
    * */

    // 특정 id 책 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    /*
    * @DeleteMapping 은 DELETE요청은 데이터를 삭제할때 사용 /{id}으로 특정책을 지정
    * bookService에 메서드를 호출하여 특정 ID의 책을 삭제함
    * 삭제가 완료되었다는 답장을 보내고 삭제성공으로인해 반환할 내용이 없음을 나타냄
    * */
}