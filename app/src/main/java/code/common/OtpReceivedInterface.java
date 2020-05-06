package code.common;

public interface OtpReceivedInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}