package kr.rrcoporation.rrfestival.festival.model;

import com.google.gson.annotations.SerializedName;

public class DetailInformation {

    @SerializedName("agelimit")
    private String ageLimit;
    @SerializedName("bookingplace")
    private String bookingPlace;
    @SerializedName("contentid")
    private int contentId;
    @SerializedName("contenttypeid")
    private int contentTypeId;
    @SerializedName("discountinfofestival")
    private int discountInfo;
    @SerializedName("eventenddate")
    private int eventEndDate;
    @SerializedName("eventplace")
    private String eventPlace;
    @SerializedName("eventstartdate")
    private int eventStartDate;
    @SerializedName("placeinfo")
    private String placeInfo;
    @SerializedName("playtime")
    private String termText;
    private String program;
    @SerializedName("spendtimefestival")
    private String spendTime;
    private String sponsor1;
    @SerializedName("sponsor1tel")
    private String sponsor1Telephone;
    private String sponsor2;
    @SerializedName("sponsor2tel")
    private String sponsor2Telephone;
    @SerializedName("subevent")
    private String subEvent;
    @SerializedName("usetimefestival")
    private String type; //유무료


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

    public int getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(int discountInfo) {
        this.discountInfo = discountInfo;
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

    public int getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(int eventStartDate) {
        this.eventStartDate = eventStartDate;
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
