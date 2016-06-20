public class Oven extends BreadFactory {
    @Override
    Bread makeBread() {
        return new Bread();
    }
}
