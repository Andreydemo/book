public class Brick implements BakeableItem {
    private Recipe recipe;

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}