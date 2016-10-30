package chap02.rule02.patterns;

/**
 * @author Kj Nam
 * @since 2016-10-31
 *
 * 빌더 패턴(builder pattern) 적용.
 */
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
