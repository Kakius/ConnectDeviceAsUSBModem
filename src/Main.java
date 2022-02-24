import com.sun.javafx.logging.Logger;
import sun.rmi.runtime.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.logging.LogManager;

public class Main extends Thread {
    public static void main(String[] args) {
        Main pMain = new Main();
        pMain.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.out.println("Поиск устройств");
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
                    System.out.println(line);
                    if (line.contains("device")) {
                        System.out.println("Подключение устройства:" + line);
                        builder = new ProcessBuilder("cmd.exe", "/c", "cd C:\\work\\ConnectDeviceAsUSBModem\\res && adb shell service call connectivity 34 i32 1 s16 text");
                        builder.redirectErrorStream(true);
                        builder.start();
                    }
                }
                Thread.sleep(5000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
