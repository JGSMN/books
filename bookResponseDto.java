package org.example.springbootdeveloper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springbootdeveloper.entity.Category;

// 서버가 클라이언트에 응답할 때 필요한 데이터만 전달
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class bookResponseDto {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private Category category;
}

/*
* 공용 클래스 bookResponseDto를 만들고
* 공용클래스 bookResponseDto 내부에서만 쓸수있는
* id, writer, title, content, category를 생성함
* */