package it.unisa.indianamas.dspace.enumerate;

public enum MetadataTag {
    
    DESCRIPTION("dc.description"),
    KEYWORDS("dc.subject"),
    OWNER("indianamas.owner"),
    CLASSIFICATION("indianamas.classification"),
    INTERPRETATION("indianamas.interpretation"),
    TITLE("dc.title"),
    CREATOR("dc.contributor.other"),
    ABSTRACT("dc.description.abstract"),
    AUTHORS("dc.contributor.author"),
    LANGUAGE("dc.language.iso"),
    TYPE("dc.type"),
    SCENE_COMPOSITION("indianamas.sceneComposition"),
    AGES("dc.coverage.temporal"),
    REGIONS("indianamas.regions"),
    GPS("dc.coverage.spatial"),
    PUBLICATION_DATE("dc.date.available"),
    HAS_SUBPARTS("dc.relation.haspart"),
    IS_PARTOF("dc.relation.ispartof"),
    IS_PARTOF_SCENE("indianamas.relation.ispartofscene"),
    HAS_SCENE_PART("indianamas.relation.hasscenepart"),
    SEMANTICALLY_RELATED_WITH("indianamas.semanticallyRelatedWith"),
    SINTACTICALLY_RELATED_WITH("indianamas.syntacticallyRelatedWith"),
    LAST_MOD_DATE("dc.date.updated"),
    URI("dc.description.uri"),
    FORMAT("dc.format"),
    SUMMARY("dc.subject.other"),
    SUPPORT("indianamas.supportBicknell"),
    PRIMARY_ROLL_SHEET("indianamas.primaryRollSheet"),
    SECONDARY_ROLL_SHEET("indianamas.secondaryRollSheet"),
    NOTE("indianamas.note"),
    SHEET_DIMENSION("indianamas.sheetDimension"),
    HDL("indianamas.HDL"),
    CLUSTER("indianamas.cluster"),
    HAS_COORDINATES("indianamas.subpartCoordinates"),
    HAS_DIMENSION("indianamas.subpartDimensions"),
    ID_RECTANGLE("indianamas.idRectangle"),
    IS_SCENE("indianamas.isScene"),
    CAPTION("dc.caption"),
    ISSUED("dc.date.issued"),
    IMAGETYPE("indianamas.imageType");
    
    private String value;

    private MetadataTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    
    
    
}
