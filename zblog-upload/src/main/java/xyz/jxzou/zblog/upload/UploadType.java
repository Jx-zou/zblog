package xyz.jxzou.zblog.upload;

public enum UploadType {

    AVATAR(1,"avatar");

    private Integer id;
    private String name;

    UploadType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
