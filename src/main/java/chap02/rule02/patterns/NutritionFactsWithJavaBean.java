package chap02.rule02.patterns;

/**
 * @author Kj Nam
 * @since 2016-10-30
 *
 * 자바빈 패턴(java bean pattern) 적용.
 */
public class NutritionFactsWithJavaBean {
    private int serviceSize = -1;
    private int servings = -1;
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public void setServiceSize(int serviceSize) {
        this.serviceSize = serviceSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}
