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

## 규칙10. toString은 항상 재정의하라

toString을 잘 만들어 놓으면 클래스를 좀 더 쾌적하게 사용할 수 있다. toString 메서드는 println이나 printf와 같은 함수, 문자열 연결 연산자, assert, 디버거 등에 객체가 전달되면 자동으로 호출된다. (중략) 가능하다면 toString 메서드는 객체 내의 중요 정보를 전부 담아 반환해야 한다.

## 규칙11. clone을 재정의할 때는 신중하라

- 비-final 클래스에 clone을 재정의할 때는 반드시 super.clone을 호출해 얻은 객체를 반환해야 한다.
- 사실상, clone 메서드는 또 다른 형태의 생성자다. 원래 객체를 손상시키는 일이 없도록 해야 하고, 복사본의 불변식도 제대로 만족시켜야 한다.
- Cloneable 인터페이스를 구현하는 클래스를 계승하는 경우가 아니라면 객체를 복사할 대안을 제공하거나, 아예 복제 기능을 제공하지 않는 것이 낫다. 예를 들어 불변 객체의 경우 복제를 허용하지 않는 것이 낫다.
  - 객체 복제를 지원하는 좋은 방법은, 복사 생성자나 복사 팩터리를 제공하는 것이다.

## 규칙12. Comparable 구현을 고려하라

compareTo 메서드의 일반 규악은 equasl와 비슷하다.

- 객체 참조를 비교하는 방향을 뒤집어도 객체 간 대소 관계는 그대로 유지되어야 한다. (대칭성)
- 추이성(transitivity)이 만족되어야 한다.
- 비교 결과 같다고 판정된 모든 객체 각각을 다른 어떤 객체와 비교할 경우, 그 비교 결과는 모두 동일해야 한다. (반사성)
- 강력히 추천하지만 절대적으로 요구되는 것이 아닌 조건 하나는 compareTo를 통한 동치성 검사 결과는 일반적으로 equals 메서드 실행 결과와 같아야 한다.

# CHAP04. 클래스와 인터페이스

## 규칙13. 클래스와 멤버의 접근 권한은 최소화하라

잘 설계된 모듈과 그렇지 못한 모듈을 구별 짓는 가장 중요한 속성 하나는 모듈 내부의 데이터를 비롯한 구현 세부사항을 다른 모듈에 잘 감추느냐의 여부다. (중략) 원칙은 단순하다. 각 클래스와 멤버는 가능한 한 접근 불가능하도록 만들라는 것.

- 객체필드는 절대로 public으로 선언하면 안된다. (중략) 변경 가능 public 필드를 가진 클래스는 다중 스레드에 안전하지 않다.


## 규칙14. public 클래스 안에는 public 필드를 두지 말고 접근자 메서드를 사용하라

### public 클래스

당연히 private 필드와 public 접근자 메서드( getter)를 제공하는 것이 마땅하다. 변경 가능 필드를 외부로 공개하면 안된다.

### package-private 또는  private 중첩 클래스

클래스가 추상화하려는 내용을 제대로 기술하기만 한다면, 데이터 필드를 공개하더라도 잘못이라 말할 수 없다.

## 규칙15. 변경 가능성을 최소화하라

변경 불가능 클래스를 만드는 이유는 다양하다. 우선 변경 불가능 클래스는 변경 가능 클래스보다 설계하기 쉽고, 구현하기 쉬우며, 사용하기도 쉽다. 오류 가능성도 적고, 더 안전하다. 변경 불가능 클래스를 만들 때는 아래의 다섯 규칙을 따르면 된다.

- 객체 상태를 변경하는 메서드를 제공하지 않는다.
- 계승할 수 없도록 한다. 계승을 금지하려면 보통 클래스를 final로 선언하면 된다.
- 모든 필드를 final로 선언한다.
- 모든 필드를 private로 선언한다.
- 변경 가능 컴포넌트에 대한 독점적 접근권을 보장한다.

변경 불가능 클래스는 객체를 변경하는 대신 새로운 객체를 만들어 반환하도록 구현한다. 함수형 접근법으로도 알려져 있는데, 피연산자를 변경하는 대신, 연산을 적용한 결과를 새롭게 만들어 반환하기 때문이다.

- 변경 불가능 객체는 단순하다. 생성될 때 부여된 한가지 상태만 갖는다.
- 변경 불가능 객체는 스레드에 안전할 수 밖에 없다. 어떤 동기화도 필요 없으며, 여러 스레드가 동시에 사용해도 상태가 훼손될 일이 없다.
- 변경 불가능한 객체는 그 내부도 공유할 수 있다. 예를 들어 BigInteger 클래스는 값의 부호와 크기를 각각 int 변수와 int 배열로 표현한다. negate 메서드는 같은 크기의 값을 부호만 바꿔서 새로운 BigInteger 객체로 반환한다. 그러나 배열은 복사하지 않는다.
- 변경 불가능 객체는 다른 객체의 구성요소로도 훌륭하다. 구성요소들의 상태가 변경되지 않는 객체는 설사 복잡하다 해도 훨씬 쉽게 불변식을 준수할 수 있다.

변경 불가능 객체의 유일한 단점은 값마다 별도의 객체를 만들어야 한다는 점이다. 변경 가능한 클래스로 만들 타당한 이유가 없다면, 반드시 변경 불가능 클래스로 만들어야 한다. 변경 불가능 클래스로 만들 수 없다면, 변경 가능성을 최대한 제한하라. 특별한 이유가 없다면 모든 필드는 final로 선언하라.

## 규칙16. 계승하는 대신 구성하라

> 이 절에서 다루는 계승은 한 클래스가 다른 클래스를 extends 하는 경우

계승은 코드 재사용을 돕는 강력한 도구지만, 항상 최선이라고 할 수 없다. 메서드 호출과 달리, 계승은 캡슐화 원칙을 위반한다. 하위 클래스가 정상 동작하기 위해서는 상위 클래스의 구현에 의존할 수 밖에 없다. 상위 클래스의 구현은 릴리스가 거듭되면서 바뀔 수 있는데, 그러다 보면 하위 클래스 코드는 수정된 적이 없어도 망가질 수 있다.

- 기존 클래스를 계승하는 대신, 새로운 클래스에 기존 클래스 객체를 참조하는 private 필드를 하나 둔다.

이런 설계 기법을 구성(composition)이라고 부른다. 새로운 클래스에 포함된 각각의 메서드는 기존 클래스에 있는 메서드 가운데 필요한 것을 호출해서 그 결과를 반환하면 된다. 

## 규칙17. 계승을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 계승을 금지하라

재정의 가능 메서드를 내부적으로 어떻게 사용하는지(self-use) 반드시 문서에 남겨라. public이나 protected로 선언된 모든 메서드와 생성자에 대해, 어떤 재정의 기능 메서드를 어떤 순서로 호출하는지, 그리고 호출 결과가 추후 어떤 영향을 미치는지 문서로 남겨라.

- 계승을 위해 설계한 클래스를 테스트할 유일한 방법은 하위 클래스를 직접 만들어 보는 것이다.

### 계승을 허용하려면 반드시 따라야할 제약사항

- 생성자는 직접적이건 간접적이건 재정의 가능 메서드를 호출해서는 안된다. 상위 클래스 생성자가 하위 클래스 생성자보다 먼저 실행되므로 메서드가 원하는 대로 실행되지 않을 것이다.
- Cloneable이나 Serializable을 구현하는 클래스라면 clone이나 readObject 메서드도 위와 같은 규칙을 따라야 한다.
- Serializabe 인터페이스를 구현하는 계승용 클래스에 readResolve와 writeReplace 메서드가 있다면 이 두 메서드는 private가 아니라 protected로 선언해야 한다.

## 규칙18. 추상 클래스 대신 인터페이스를 사용하라

- 인터페이스는 믹스인(mixin)을 정의하는데 이상적이다. 믹스인은 클래스가 주자료형 이외에 추가로 구현할 수 있는 자료형으로, 어떤 선택적 기능을 제공한다는 사실을 선언하기 위해 쓰인다.
- 인터페이스는 비계층적인 자료형 프레임워크를 만들 수 있도록 한다.
- 추상골격구현 클래스를 중요 인터페이스마다 두면, 인터페이스의 장점과 추상 클래스의 장점을 결합할 수 있다.
- public 인터페이스는 신중하게 설계해야 한다. 공개되고 나면 수정이 거의 불가능 하다. 새로운 메서드를 추가하게 된다면, 추상 클래스의 경우 계승하는 모든 클래스가 새로운 메서드를 제공받게 되지만, 인터페이스로는 이런 작업이 불가능하다.

## 규칙19. 인터페이스는 자료형(type)을 정의할 때만 사용하라

인터페이스를 구현하는 클래스를 만들게 되면, 그 인터페이스는 해당 클래스의 객체를 참조할 수 있는 자료형 역할을 하게 된다. 인터페이스를 구현해 클래스를 만든다는 것은, 해당 클래스의 객체로 어떤 일을 할 수 있는지 클라이언트에게 알리는 행위다.

- 상수 인터페이스 패턴은 인터페이스를 잘못 사용한 것이다.

상수 정의를 인터페이스에 포함시키면 구현 세부사항이 클래스의 공개 API에 스며들게 된다. 이런 상수들은 enum 자료형이나 객체 생성이 불가능한 유틸리티 클래스에 넣어서 공개해야 한다.

## 규칙20. 태그 달린 클래스 대신 클래스 계층을 활용하라

때로는 하나의 클래스가 두 가지 이상의 기능을 가지고 있으며, 그 중 어떤 기능을 제공하는 표시하는 태그(tag)를 가진 클래스를 만날 때가 있다(하나의 클래스로 원을 표현할 수도 있고 사각형도 표현할 수 있다던지).  이런 클래스에는 다양한 문제가 있다.

- enum선언, 태그 필드, switch문 등의 상투적 코드(boilerplate)가 반복된다.
- 서로 다른 기능을 위한 코드가 한 클래스에 모여 있으니 가독성이 떨어진다.
- 객체를 만들때마다 필요 없는 기능을 위한 필드도 함께 생성되므로, 메모리 요구랑도 늘어난다.
- 새로운 기능을 추가하려면 소스 파일을 반드시 수정해야 한다.

클래스 안에 태그 필드를 명시적으로 두고 싶다는 생각이 든다면, 클래스 계층을 통해 태그를 제거할 방법이 없는지 생각하라.

## 규칙21. 전략을 표현하고 싶을 때는 함수 객체를 사용하라

```java
class StringLengthComparator {
  public int compare(String s1, String s2) {
    return s1.length() - s2.length();
  }
}
```

자바는 함수 포인터를 지원하지 않는다. 하지만 객체 참조를 통해 비슷한 효과를 달성할 수 있다. 가지고 있는 메서드나 그런 메서드 하나 뿐인 객체는 해당 메서드의 포인터 구실을 하고, 그런 객체를 함수 객체라고 부른다. StringLengthComparator 객체는 문자열을 비교하는 데 사용될 수 있는,  실행 가능 전략(concrete strategy) 이다. 실행 가능 전략 클래스들은 대체로 무상태(stateless) 클래스다.

함수객체의 주된 용도는 전략패턴을 구현하는 것이다. 자바로 이 패턴을 구현하기 위해서는 전략을 표현하는 인터페이스를 선언하고, 실행 가능 전략 클래스가 전부 해당 인터페이스를 구현하도록 해야 한다. 실행 가능 전략이 한 번만 사용되는 경우에는 보통 그 전략을 익명 클래스 객체로 구현한다. 반복적으로 사용된다면 private static  멤버 클래스로 전략을 표현한 다음, 전략 인터페이스가 자료형인 public static final 필드를 통해 외부에 공개하는 것이 바람직하다.

## 규칙22. 멤버 클래스는 가능하면 static으로 선언하라

중첩 클래스는 해당 클래스가 속한 클래스 안에서만 사용된다. 그렇지 않으면 중첩 클래스로 만들면 안된다. 중처 클래스에는 네 가지 종류가 있다.

- 정적 멤버 클래스: 중첩 클래스를 메서드 밖에서 사용할 수 있거나, 메서드 안에 놓기에 너무 길 경우
- 비-정적 멤버 클래스: 멤버 클래스의 객체 각각이 바깥 객체에 대한 참조를 가져야 하는 경우
- 익명 클래스: 중첩 클래스가 특정 메서드에 속해야 하고, 오직 한 곳에서만 객체를 생성하며, 해당 중첩 클래스의 특성을 규정하는 자료형이 이미 있을 때.
- 지역 클래스

# CHAP05. 제네릭

## 규칙23. 새 코드에는 무인자 제네릭 자료형을 사용하지 마라

자바 1.5부터 형인자(type parameter)와 제네릭(generic)이 도입되었다. 제네릭 자료형을 형인자 없이 사용할 수 도 있지만 이는 이전 버전과의 호환성 때문이다. 무인자 자료형을 쓰면 형 안전성이 사라지고, 제네릭의 장점 중 하나인 표현력(expressiveness) 측면에서 손해를 보게 된다. 제네릭 원소들의 자료형을 모르고 상관할 필요가 없다면 무인자 자료형 대신 비한정적 와일드카드 자료형을 사용하라.