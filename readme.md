## Effective Java
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



# CHAP03. 모든 객체의 공통 메소드

## 규칙08. equals를 재정의할 때는 일반 규약을 따르라

### equal를 재정의 하지 않아도 될 때

- 각각의 객체가 고유하다.
- 클래스에 논리적 동일성 검사 방법이 있건 없건 상관없다.
- 상위클래스엣 재정의한 equals가 하위 클래스에서 사용하기에도 적당하다.
- 클래스가 private 또는 package-private로 선언되었고, equals 메서드를 호출할 일이 없다.



### equals를 정의할 때 준수해야 할 일반 규약

equals 메서드는 동치 관계를 구현한다. 다음과 같은 관계를 동치 관계라 한다.

- 반사성: null이 아닌 참조 x가 있을 때, `x.equals(x)`는 true를 반환.
- 대칭성: null이 아닌 참조 x와 y가 있을 때, `x.equals(y)`는 `y.equals(x)`가 true일 때만 true를 반환.
- 추이성: null 아닌 참조 x,y,z가 있을 때, `x.equals(y)`가 true 이고 `y.equals(z)`가 true이면 `x.equals(z)`도 true.
- 일관성: null 아닌 참조 x와 y가 있을 때, equals를 통해 비교되는 정보에 아무 변화가 없다면, `x.equals(y)` 호출 결과는 호출 횟수에 상관 없이 항상 같아야 한다.
- null 아닌 참조 x에 대해서, `x.equals(null)`은 항상 false이다.



### 훌륭한 equals 메서드 구현을 위해 따라야 할 지침

- == 연산자를 사용하여 equals의 인자가 자기 자신인지 검사하라.
- instanceof 연산자를 사용하여 인자의 자료형이 정확한지 검사하라.
- equals의 인자를 정확한 자료형으로 변환하라.
- "중요" 필드 각각이 인자로 주어진 객체의 해당 필드와 일치하는지 검사한다.
- 대칭성, 추이성, 일관서으이 세 속성이 만족되는지 검토하라.
- equals를 구현할 때는 hashCode도 재정의하라.
- equals 메서드의 인자 형을 Object에서 다른 것으로 바꾸지 마라.

## 규칙09. equals를 재정의할 때는 반드시 hashCode도 재정의하라

equals 메서드를 재정의 하는 클래스가 hashCode를 재정의 하지 않으면 HashMap, HashSet, Hashtable 같은 해시 기반 컬렉션과 함께 사용하면 오동작하게 된다. Object 클래스 명세의 일반 규약은 아래와 같다.

- 응용프로그램 실해 중 같은 객체의 hashCode를 여러 번 호출하는 경우, equals가 사용하는 정보들이 변경되지 않았다면 언제나 동일한 정수가 반환되어야 한다.
- equals메서드가 같다고 판정한 두 객체의 hashCode 값은 같아야 한다.
- equals메서드가 다르다고 판정한 두 객체의 hashCode 값은 꼭 다를 필요는 없다. 그러나 서로 다른 hashCode 값이 나오면 해시 테이블의 성능이 향상될 수 있다는 점은 이해하고 있어야 한다.


### 이상적인 해시 함수에 '가까운' 함수를 만드는 방법

1. 17과 같은 0 아닌 상수를 result라는 이름의 int 변수에 저장.
2. 객체 안에 모든 중요 필드 f에 대해(equals 메서드가 사용하는 필드들) 아래의 절차를 시행.
   1. f 가 boolean 이면 1 or 0
   2. f 가 byte char short int 이면 (int)f
   3. f 가 long 이면 (int)(f ^ ( f >>> 32))
   4. f 가 float 이면 Float.floatToIntBits(f)
   5. f 가 double 이면 Double.floatToLongBits(f)를 계산 후 그 결과로 3번을 수행
   6. f 가 객체 참조일 경우 eqauls 메소드를 재귀적으로 호출하면 hashCode 메소드도 재귀적으로 자동 호출
   7. f 가 배열이라면 배열의 각 요소를 별개의 필드처럼 계산. Arrays.hashCode 메소드들 중 하나를 사용할 수 있음
   8. 위의 절차에서 계산 된 해쉬코드 c를 사용하여 result를 계산한다. result = 31 * result + c
3. result를 반환한다.
4. 동치 관계에 있는 객체의 해시 코드 값이 똑같이 계산되는지 점검한다.