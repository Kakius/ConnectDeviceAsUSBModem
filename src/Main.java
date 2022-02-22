import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd C:\\work\\ConnectDeviceAsUSBModem\\res && adb devices");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                if (line.contains("devices")) {
                    continue;
                }
                if (line.contains("device")) {
                    try {
                        builder = new ProcessBuilder("cmd.exe", "/c", "cd C:\\work\\ConnectDeviceAsUSBModem\\res && adb shell service call connectivity 34 i32 1 s16 text");
                        builder.redirectErrorStream(true);
                        builder.start();
                        System.exit(0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(0);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
