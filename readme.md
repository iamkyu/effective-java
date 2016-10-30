# Effective Java
본 내용은 [Effective Java|저자 조슈아 블로크|역자 이병준|인사이트 |2014.09.01](http://book.naver.com/bookdb/book_detail.nhn?bid=8064518) 을 학습하며 정리한 내용으로 개인 참고용으로 작성 됨. 샘플 소스 코드는 `src/main/java` 아래에 /챕터번호/규칙번호로 대응된다 (예 `/chap02/rule01`).



# CHAP02. 객체의 생성과 삭제

## 규칙01. 생성자 대신 정적(static) 팩터리 메서드를 사용할 수 없는지 생각해 보라.

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



## 규칙02. 생성자 인자가 많을 때는 Builder 패턴 적용을 고려하라.

정적 팩터리나 생성자는 선택적 인자가 많은 상황에 잘 적응하지 못한다.

### 점층적 생성자 패턴(telescoping constructor pattern)

점층적 생성자 패턴을 적용할 수 있지만 인자수가 늘어나면 클라이언트 코드를 작성하기 어려워지고 읽기 어려운 코드가 된다.

### 자바빈 패턴(javabean pattern)

인자 없는 생성자를 호출하여 객체를 만든 다음, 설정 메서드들(setter)을 호출하여 필드의 값을 채운다. 점층적 생성자 패턴에 비해 객체를 생성하기 쉬우며 읽기도 좋다. 하지만

- 1회 함수 호출로 객체 생성을 끝낼 수 없으므로, 객체 일관성이 일시적으로 깨진다.
- 변경 불가능(immutable) 클래스를 만들 수 없다.

### 빌더 패턴(Builder pattern)

```java
public class NutritionFactsWithBuilder {
    private final int serviceSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    private NutritionFactsWithBuilder(Builder builder) {
        this.serviceSize = builder.serviceSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }

    public static class Builder {
        //필수인자
        private final int serviceSize;
        private final int servings;

        //선택적인자-기본값으로 초기화
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int serviceSize, int servings) {
            this.serviceSize = serviceSize;
            this.servings = servings;
        }

        public NutritionFactsWithBuilder build() {
            return new NutritionFactsWithBuilder(this);
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }
    }
}
```

- 객체가 변경 불가능해짐.
- `NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8).calories(100).sodium(35).carbohydrate(27).build();` 와 같이 메소드 체이닝 방식으로 사용 가능.

