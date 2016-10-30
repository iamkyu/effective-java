# Effective Java
본 내용은 [Effective Java|저자 조슈아 블로크|역자 이병준|인사이트 |2014.09.01](http://book.naver.com/bookdb/book_detail.nhn?bid=8064518) 을 학습하며 정리한 내용으로, 개인 참고용으로 작성.

# CHAP2. 객체의 생성과 삭제

## 규칙1. 생성자 대신 정적(static) 팩터리 메서드를 사용할 수 없는지 생각해 보라.

### 장점

- 생성자와 달리 이름을 가진다.
- 생성자와 달리 호출할 때마다 새로운 객체를 생성할 필요가 없다.
- 생성자와 달리 반환값 자료형의 하위 자료형 객체를 반환할 수 있다.
- 형인자 자료형(parameterized type) 객체를 만들때 편리하다.
  - 자바6 까지는 `Map<String, List<String>> m = new HashMap<String, List<String>()`과 같이 생성자에 중복해서 형인자를 전달해야 했으나, 자바 7부터는 다이아몬드 연산자를 통해 `Map<String, List<String>> m = new HashMa<>()`과 같이 사용 가능하다. 따라서 자바 7 이후에서는 해당 장점이 큰 의미를 가지지는 않는다.

| 용어                              | 예                                  |
| ------------------------------- | ---------------------------------- |
| 형인자 자료형(parameterized type)     | `List<String>`                     |
| 실 형인자(actual type parameter)    | `String`                           |
| 제네릭 자료형(generic type)           | `List<E>`                          |
| 형식 형인자(formal type parameter)   | `E`                                |
| 비한정적 와일드카드 자료형                  | `List<?>`                          |
| 무인자 자료형(raw type)               | `List`                             |
| 한정적 자료형(bounded type parameter) | `<E extends Numbers>`              |
| 재귀적 형 한정                        | `<T extends Comparable<T>>`        |
| 한정적 와일드카드 자료형                   | `List<? extends Numbers>`          |
| 제네릭 메서드(generic method)         | `static <E> List<E> asList(E[] a)` |
| 자료형 토큰(type token)              | `String.class`                     |

- *출처: [아율아빠님 블로그 http://m.blog.naver.com/spdlqjdudghl/220756675096](http://m.blog.naver.com/spdlqjdudghl/220756675096)*

### 단점

- 접근제어자가 public이나 protected로 선언된 생성자가 없으므로 하위 클래스를 만들 수 없다.
- 정적 팩터리 메서드가 다른 정적 메서드와 확연히 구분되지 않는다.
  - 정적 팩터리 메서드 이름으로 자주 사용되는 것들: `valueOf`, `of`, `getInstance`, `newInstance`, `getType`, `newType]`