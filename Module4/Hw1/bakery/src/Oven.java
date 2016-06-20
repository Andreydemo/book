public class Oven implements Gas, Bakeable {
    private int levelOfGas;

    @Override
    public int getLevelOfGas() {
        return levelOfGas;
    }

    public void setLevelOfGas(int levelOfGas) {
        this.levelOfGas = levelOfGas;
    }

    @Override
    public BakeableItem bake(Type type) {
        levelOfGas -= 10;
        return type.bake();
    }
}