package kr.rrcoporation.rrfestival.festival.model;

import com.google.gson.annotations.SerializedName;

public class SummaryInformation {

    @SerializedName("contentid")
    private int contentId;
    @SerializedName("contenttypeid")
    private int contentTypeId;
    @SerializedName("fldgubun")
    private int fieldType; //1.행사소개 2.행사내용
    @SerializedName("infoname")
    private String infoName;
    @SerializedName("infotext")
    private String infoText;
    @SerializedName("serialnum")
    private int serialNumber;


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

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
