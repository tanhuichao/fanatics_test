package model;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("active")
    ACTIVE,
    @SerializedName("inactive")
    INACTIVE
}
