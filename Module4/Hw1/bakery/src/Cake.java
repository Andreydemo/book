public class Cake implements BakeableItem {
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
