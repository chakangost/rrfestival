package kr.rrcoporation.rrfestival.festival.model;

import java.math.BigDecimal;

public class BodyItem {

    private String     addr1;
    private String     addr2;
    private int        areacode;
    private String     cat1;
    private String     cat2;
    private String     cat3;
    private int        contentid;
    private int        contenttypeid;
    private BigDecimal createdtime;
    private String     firstimage;
    private String     firstimage2;
    private double     mapx;
    private double     mapy;
    private int        mlevel;
    private BigDecimal modifiedtime;
    private int        readcount;
    private int        sigungucode;
    private String     tel;
    private String     title;
    private String     zipcode;

    public BodyItem(int contentId, String title, double lat, double lng, String firstimage) {
        this.contentid = contentId;
        this.title = title;
        this.mapy = lat;
        this.mapx = lng;
        this.firstimage = firstimage;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public int getAreacode() {
        return areacode;
    }

    public void setAreacode(int areacode) {
        this.areacode = areacode;
    }

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public int getContentid() {
        return contentid;
    }

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public int getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(int contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public int getMlevel() {
        return mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public int getReadcount() {
        return readcount;
    }

    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }

    public int getSigungucode() {
        return sigungucode;
    }

    public void setSigungucode(int sigungucode) {
        this.sigungucode = sigungucode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public BigDecimal getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(BigDecimal createdtime) {
        this.createdtime = createdtime;
    }

    public BigDecimal getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(BigDecimal modifiedtime) {
        this.modifiedtime = modifiedtime;
    }
}
