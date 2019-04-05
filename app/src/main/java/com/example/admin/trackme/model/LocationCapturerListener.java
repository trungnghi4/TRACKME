package com.example.admin.trackme.model;

public interface LocationCapturerListener {
    void connectionFailed(String errorMessage);

    void onLocationCaptured(double latitude, double longitude);
}
