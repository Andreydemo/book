public class Bread implements Dough {
    private Recipe recipe;

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
