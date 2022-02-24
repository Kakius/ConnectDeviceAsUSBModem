import cmd.CmdExecute;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Thread {

    private CmdExecute execute;

    public static void main(String[] args) {
        Main pMain = new Main();
        pMain.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                if (execute == null) {
                    execute = new CmdExecute();
                }
                execute.getDevice();
                execute.getOSVersion();
                execute.enableUsbTethering();
                Thread.sleep(5000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
