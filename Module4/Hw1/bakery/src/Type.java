public enum Type {
    BREAD {
        @Override
        BakeableItem bake() {
            return new Bread();
        }
    },

    MEAT_PASTRY {
        @Override
        BakeableItem bake() {
            return new MeatPastry();
        }
    },

    CABBAGE_PASTRY {
        @Override
        BakeableItem bake() {
            return new CabbagePastry();
        }
    },

    CAKE {
        @Override
        BakeableItem bake() {
            return new Cake();
        }
    },

    BRICK {
        @Override
        BakeableItem bake() {
            return new Brick();
        }
    };

    abstract BakeableItem bake();
}
