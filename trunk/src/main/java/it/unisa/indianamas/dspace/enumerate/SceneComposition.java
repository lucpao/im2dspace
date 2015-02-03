package it.unisa.indianamas.dspace.enumerate;

public enum SceneComposition {
    SINGLE_CARVING("single_carving"), MANY_CARVING("MANY_CARVING");
    private String value;

    private SceneComposition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
