package de.clzserver.homebox.filemanager.onlinecheck;

/**
 * Created by Marc Janßen on 25.05.2016.
 */
public class OnlineCheckerTest {

    public static void main(String[] args) {
        OnlineStatus status = OnlineChecker.checkStatus();

        System.out.println("OnlineChecker test: das Programm befindet sich im Status \n\t"+status.getStatus()+"\n und der Onlinestatus lautet\n\t"+status.isOnline());

        System.out.println("Starte Permantchecker");

        OnlineChecker auto = OnlineChecker.getInstance();
        auto.startOnlineChecker();
    }
}
