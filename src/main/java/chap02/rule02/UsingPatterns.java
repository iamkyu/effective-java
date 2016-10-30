package chap02.rule02;

import chap02.rule02.patterns.NutritionFactsWithBuilder;
import chap02.rule02.patterns.NutritionFactsWithJavaBean;
import chap02.rule02.patterns.NutritionFactsWithTelescoping;

/**
 * @author Kj Nam
 * @since 2016-10-31
 */
public class UsingPatterns {
    public void usingPatterns() {
        //점층적 생성자 패턴(telescoping constructor pattern) 사용
        NutritionFactsWithTelescoping cocaCola1 = new NutritionFactsWithTelescoping(240, 8, 100, 3, 35, 27);

        //자바빈 패턴(java bean pattern) 사용
        NutritionFactsWithJavaBean cocaCola2 = new NutritionFactsWithJavaBean();
        cocaCola2.setServiceSize(11);
        cocaCola2.setServings(18);
        cocaCola2.setCalories(100);
        cocaCola2.setSodium(35);
        cocaCola2.setCarbohydrate(27);

        //빌더 패턴(builder pattern) 사용
        NutritionFactsWithBuilder cocaCola3 = new NutritionFactsWithBuilder.Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
    }
}
