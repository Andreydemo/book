public class Oven extends BreadFactory implements Gas {
    private int levelOfGas;

    @Override
    Bread makeBread() {
        levelOfGas -= 10;
        return new Bread();
    }

    @Override
    public int getLevelOfGas() {
        return levelOfGas;
    }

    public void setLevelOfGas(int levelOfGas) {
        this.levelOfGas = levelOfGas;
    }
}