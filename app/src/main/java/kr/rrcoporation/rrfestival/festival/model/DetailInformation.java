package kr.rrcoporation.rrfestival.festival.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailInformation {


    //detail common
    private String addr1;
    private String addr2;
    private Integer areacode;
    private String cat1;
    private String cat2;
    private String cat3;
    private Integer createdtime;
    private String firstimage;
    private String firstimage2;
    private String homepage;
    private Float mapx;
    private Float mapy;
    private Integer mlevel;
    private Integer modifiedtime;
    private String overview;
    private Integer sigungucode;
    private String tel;
    private String telname;
    private String title;
    private Integer zipcode;
    //detailIntro
    @SerializedName("agelimit")
    private String ageLimit;
    @SerializedName("bookingplace")
    private String bookingPlace;
    @SerializedName("contentid")
    private int contentId;
    @SerializedName("contenttypeid")
    private int contentTypeId;
    @SerializedName("discountinfofestival")
    private String discountInfo;
    @SerializedName("eventstartdate")
    private int eventStartDate;
    @SerializedName("eventenddate")
    private int eventEndDate;
    @SerializedName("eventplace")
    private String eventPlace;
    @SerializedName("placeinfo")
    private String placeInfo;
    @SerializedName("playtime")
    private String                   termText;
    private String                   program;
    @SerializedName("spendtimefestival")
    private String                   spendTime;
    private String                   sponsor1;
    @SerializedName("sponsor1tel")
    private String                   sponsor1Telephone;
    private String                   sponsor2;
    @SerializedName("sponsor2tel")
    private String                   sponsor2Telephone;
    @SerializedName("subevent")
    private String                   subEvent;
    @SerializedName("usetimefestival")
    private String                   type; //유무료
    //Introduce
    private ArrayList<DetailSummary> summaries;
    //Image
    private ArrayList<DetailImage>   images;

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

    public Integer getAreacode() {
        return areacode;
    }

    public void setAreacode(Integer areacode) {
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

    public Integer getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Integer createdtime) {
        this.createdtime = createdtime;
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

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Float getMapx() {
        return mapx;
    }

    public void setMapx(Float mapx) {
        this.mapx = mapx;
    }

    public Float getMapy() {
        return mapy;
    }

    public void setMapy(Float mapy) {
        this.mapy = mapy;
    }

    public Integer getMlevel() {
        return mlevel;
    }

    public void setMlevel(Integer mlevel) {
        this.mlevel = mlevel;
    }

    public Integer getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Integer modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getSigungucode() {
        return sigungucode;
    }

    public void setSigungucode(Integer sigungucode) {
        this.sigungucode = sigungucode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelname() {
        return telname;
    }

    public void setTelname(String telname) {
        this.telname = telname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public ArrayList<DetailSummary> getSummaries() {
        return summaries;
    }

    public void setSummaries(ArrayList<DetailSummary> summaries) {
        this.summaries = summaries;
    }

    public ArrayList<DetailImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<DetailImage> images) {
        this.images = images;
    }

    public String getEventDate() {

        String startDate = String.valueOf(this.eventStartDate).replaceFirst("([0-9]{4,4})([0-9]{2,2})([0-9]{2,2})", "$1.$2.$3");
        String endDate = String.valueOf(this.eventEndDate).replaceFirst("([0-9]{4,4})([0-9]{2,2})([0-9]{2,2})", "$1.$2.$3");

        return startDate + " ~ " + endDate;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getBookingPlace() {
        return bookingPlace;
    }

    public void setBookingPlace(String bookingPlace) {
        this.bookingPlace = bookingPlace;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(int contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(String discountInfo) {
        this.discountInfo = discountInfo;
    }

    public int getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(int eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public int getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(int eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public String getTermText() {
        return termText;
    }

    public void setTermText(String termText) {
        this.termText = termText;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public String getSponsor1() {
        return sponsor1;
    }

    public void setSponsor1(String sponsor1) {
        this.sponsor1 = sponsor1;
    }

    public String getSponsor1Telephone() {
        return sponsor1Telephone;
    }

    public void setSponsor1Telephone(String sponsor1Telephone) {
        this.sponsor1Telephone = sponsor1Telephone;
    }

    public String getSponsor2() {
        return sponsor2;
    }

    public void setSponsor2(String sponsor2) {
        this.sponsor2 = sponsor2;
    }

    public String getSponsor2Telephone() {
        return sponsor2Telephone;
    }

    public void setSponsor2Telephone(String sponsor2Telephone) {
        this.sponsor2Telephone = sponsor2Telephone;
    }

    public String getSubEvent() {
        return subEvent;
    }

    public void setSubEvent(String subEvent) {
        this.subEvent = subEvent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
