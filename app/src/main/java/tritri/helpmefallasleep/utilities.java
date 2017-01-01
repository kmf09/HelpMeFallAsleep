package tritri.helpmefallasleep;

/**
 * Created by Katrina on 12/10/2016.
 */

public class Utilities {
    public static void setIsRunning(AudioService.AudioServiceListener audioServiceListener, boolean value) {
        if (audioServiceListener != null) {
            audioServiceListener.serviceFinishedCallback(value);
        }
    }
}
