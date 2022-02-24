package cmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CmdExecute {
    private final static String CMD = "cmd.exe";
    private final static String STORE = "/c";
    private ProcessBuilder processBuilder;
    private Process process;
    private BufferedReader bufferedReader;
    private final static String PATH_ADB = "cd C:\\work\\ConnectDeviceAsUSBModem\\res && ";
    private List<String> devices;
    private Map<String, String> codeToVersionMap;

    public void getDevice() {
        try {
            System.out.println("[INFO] Метод getDevice");
            devices = new ArrayList<>();
            System.out.println("Получаю список устройств");
            processBuilder = new ProcessBuilder(CMD, STORE,
                    PATH_ADB + "adb devices");
            process = processBuilder.start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String deviceCode = "";
            while (true) {
                deviceCode = bufferedReader.readLine();
                if (deviceCode == null) {
                    break;
                } else if (deviceCode.contains("devices")) {
                    continue;
                } else if (deviceCode.contains("device")) {
                    deviceCode = deviceCode.replaceAll("device", "").trim();
                    System.out.println("Код устройства: " + deviceCode);
                    devices.add(deviceCode);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex);
        } finally {
            closeStream();
        }
        System.out.println();
    }

    public void getOSVersion() {
        try {
            System.out.println("[INFO] Метод getOSVersion");
            codeToVersionMap = new HashMap<>();
            if (devices.isEmpty()) {
                System.out.println("Список устройств пуст. Выходим");
                System.out.println();
                return;
            }
            for (String code : devices) {
                processBuilder = new ProcessBuilder(CMD, STORE,
                        PATH_ADB + "adb -s " + code + " shell getprop ro.build.version.release");
                process = processBuilder.start();
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String OSVersion = bufferedReader.readLine();
                System.out.println("Версия ОС устройства: " + OSVersion);
                codeToVersionMap.put(code, OSVersion);

            }
            closeStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex);
        }
        System.out.println();
    }

    public void enableUsbTethering() {
        try {
            System.out.println("[INFO] Метод enableUsbTethering");
            if (codeToVersionMap.isEmpty()) {
                System.out.println("Список устройств пуст. Выходим");
                System.out.println();
                return;
            }
            System.out.println("Включение USB Tethering");
            for (String code : codeToVersionMap.keySet()) {
                String OSVersion = codeToVersionMap.get(code);
                String codeFunctions = "";
                String[] temp = OSVersion.split(Pattern.quote("."));
                int version = Integer.parseInt(temp[0]);
                if (version <= 8) {
                    codeFunctions = "34";
                } else if (version >= 9) {
                    codeFunctions = "33";
                } else {
                    return;
                }
                System.out.println("Код устройства: " + code);
                System.out.println("Версия устройства: " + OSVersion);
                processBuilder = new ProcessBuilder(CMD, STORE,
                        PATH_ADB + "adb -s " + code + " shell service call connectivity " + codeFunctions + " i32 1 s16 text");
                process = processBuilder.start();
                closeStream();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
        System.out.println();
    }


    private void closeStream() {
        try {
            if (process != null) {
                process.getInputStream().close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }

}
