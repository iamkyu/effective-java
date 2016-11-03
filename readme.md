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


## 규칙03. private 생성자나 enum 자료형은 싱글톤 패턴을 따르도록 설계하라.

### 정적멤버를 이용한 싱글톤

```java
public class Elvis {
  public static final Evis INSTANCE = new Elvis();
  private Elvis() { }
}
```

- private 생성자는 `Elvis.INSTANCE`를 초기화 할 때 한번만 호출.
- 하지만 리플렉션(reflection)을 통해 private 생성자에 접근할 수도 있음. 이를 방어하려면 두 번째 객체를 생성하라는 요청을 받으면 예외를 던지도록 수정 필요.

### 정적팩터리 메서드를 이용한 싱글톤

```java
public class Elvis {
  private static final Evis INSTANCE = new Elvis();
  private Elvis() { //두번재 객체를 생성하라는 요청을 받으면 예외를 던지도록 한다 }
  public static Elvis getInstance() {
    return INSTANCE;
  }
}
```

- `Elvis.getInstance()`는 항상 같은 객체에 대한 참조를 반환.
- API를 변경하지 않고도 싱글턴 패턴을 포기하는 것이 가능. (스레드마다 별도의 객체를 반환하도록 수정한다던지)
- 제네릭 타입의 수용이 쉬움

### Enum을 통한 싱글톤 (Java 1.5 or later)

```java
public enum EnumInitialization {
	INSTANCE;
	static String test = "";
	public static EnumInitialization getInstance() {
		test = "test";
		return INSTANCE;
	}
}
```

- public 필드를 사용하는 구현법과 동등.
- 좀 더 간결하고, 직렬화가 자동으로 처리. 직렬화가 아무리 복잡하게 이루어져도 여러 객체가 생기지 않으며, 리플랙션을 통한 공격에도 안전.
- 이와 관련 된 좀 더 자세한 내용은 [https://blog.seotory.com/post/2016/03/java-singleton-pattern](https://blog.seotory.com/post/2016/03/java-singleton-pattern) 을 참고하면 좋다.



## 규칙04. 객체 생성을 막을 때는 private 생성자를 사용하라.

정적 메서드나 필드만 모은 클래스를 만드는 경우

- 기본 자료형 값(primitive value) 또는 배열에 적용되는 메소드를 한군데 모아둘 때. (ex `java.lang.Math`, `java.util.Arrays`)
- 특정 인터페이스를 구현하는 객체를 만드는 팩터리 메서드 등의 정적 메소드를 모아놓을 때. (ex `java.tuil.Collections`)
- final 클래스에 적용할 메소드를 모아놓을 때.

```java
public class UtilityClass {
  //기본 생성자가 자동 생성되지 못하도록 하여 객체 생성 방지
  private UtilityClass() {
    throw new AssertionError();
  }
}
```

위에서 예로 든 경우와 같이 객체 생성이 목적이 아닌 클래스들은 private 생성자를 클래스에 넣어 객체 생성을 방지해야 한다.



## 규칙05. 불필요한 객체는 만들지 말라

기능적으로 동일한 객체는 필요할 때마다 만드는 것보다 재사용하는 편이 낫다. 객체를 재사용하는 프로그램은 더 빠르고 더 우아하다.

- `String` 객체를 만들 때 new 연산자를 사용하지 말 것.
- 생성자와 정적 팩터리 메소드를 함께 제공하는 변경 불가능 클래스의 경우, 생성자 대신 정적 팩터리 메소드를 사용할 것. (ex `Boolean(String)` 보다는 `Boolean.valueOf(String)`)
- 메소드가 실행될 때 마다 불 필요하게 초기화 되는 클래스는 정적 초기화 블록을 사용할 것. (ex `Calender`)
- 기본 자료형과 객체표현형 사이의 auto-boxing 을 주의할 것.



## 규칙06. 유효기간이 지난 객체 참조는 폐기하라

(자바와 같이) 자동적으로 쓰레기 객체를 수집하는 언어에서 발생하는 메모리 누수 문제는(널리 알려진 용어로는 의도치 않은 객체 보유) 찾아 내기 어렵다.

- 가장 간단한 방법은 쓸 일 없는 객체 참조는 무조건 null로 만들어라.
- 객체 참조를 null 처리하는 것은 규범이라기보단 예외적인 조치가 되어야 한다.
- 가장 좋은 방법은 변수나 객체를 정의할 때 그 유효 범위를 최대한 좁게 만드는 것이다.



## 규칙07. 종료자 사용을 피하라

종료자(finalizer)는 예측 불가능하며, 대체로 위험하고, 일반적으로 불필요하다.

> [java.lang.Object.finalize()](http://docs.oracle.com/javase/7/docs/api/java/lang/Object.html?is-external=true#finalize())

### 종료자의 단점

- 종료자의 동작은 예측할 수 없다(즉시 실행되지 않으며 실행된다는 보장도 없다).
- 프로그램의 성능을 심각하게 떨어뜨린다.

