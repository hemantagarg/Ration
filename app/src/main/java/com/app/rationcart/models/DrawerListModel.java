package com.app.rationcart.models;


public class DrawerListModel {

    private String id;
    private String category;
    private String cat_id, SubMenu1Id, SubMenu1AvatarId;
    private String name;
    private String pic;
    private String status, teamId = "";


    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSubMenu1AvatarId() {
        return SubMenu1AvatarId;
    }

    public void setSubMenu1AvatarId(String subMenu1AvatarId) {
        SubMenu1AvatarId = subMenu1AvatarId;
    }

    public String getSubMenu1Id() {
        return SubMenu1Id;
    }

    public void setSubMenu1Id(String subMenu1Id) {
        SubMenu1Id = subMenu1Id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
