package chap02.rule02.patterns;

/**
 * @author Kj Nam
 * @since 2016-10-30
 *
 * 점층적 생성자 패턴(telescoping constructor pattern) 적용.
 */
public class NutritionFactsWithTelescoping {
    private final int serviceSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFactsWithTelescoping(int serviceSize, int servings, int calories) {
        this(serviceSize, servings, calories, 0);
    }

    public NutritionFactsWithTelescoping(int serviceSize, int servings, int calories, int fat) {
        this(serviceSize, servings, calories, fat, 0);
    }

    public NutritionFactsWithTelescoping(int serviceSize, int servings, int calories, int fat, int sodium) {
        this(serviceSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFactsWithTelescoping(int serviceSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.serviceSize = serviceSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
