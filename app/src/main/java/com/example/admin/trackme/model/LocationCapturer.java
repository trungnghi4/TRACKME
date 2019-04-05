package com.example.admin.trackme.model;

public interface LocationCapturer {

    void stopToCaptureLocations();

    void startToCaptureLocations();

    void setLocationCapturerListener(LocationCapturerListener listener);

}
