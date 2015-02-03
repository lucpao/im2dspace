package it.unisa.indianamas.dspace.enumerate;

public enum Type {
    JOURNAL("journal"),CONFERENCE("conference"),BOOK("book"),CHAPTER("chapter"),THESIS("thesis"),UNPUBLISHED("unpublished");
    private String value;

    private Type(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
