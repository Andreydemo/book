public enum Type {
    BREAD {
        @Override
        Dough bake() {
            return new Bread();
        }
    },

    MEAT_PASTRY {
        @Override
        Dough bake() {
            return new MeatPastry();
        }
    },

    CABBAGE_PASTRY {
        @Override
        Dough bake() {
            return new CabbagePastry();
        }
    },

    CAKE {
        @Override
        Dough bake() {
            return new Cake();
        }
    };

    abstract Dough bake();
}
