package it.unisa.indianamas.dspace.enumerate;

public enum BicknellSupport {
    A("a"),B("b"),C("c"),D("d"),E("e");
    private String value;

    private BicknellSupport(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    


}
