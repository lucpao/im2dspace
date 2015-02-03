package it.unisa.indianamas.dspace.enumerate;

public enum ImageType {
    DOMAIN_SPECIFIC_IMAGE("domain_specific_image"),GENERICIMAGE("generic_image"),SKETCH("sketch"),BWIMAGE("bw_image"),BICKNELLTRACING("bicknell_tracing"),CARVING_PICTURE("carving_picture");
    private String value;

    private ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    


}
