package de.clzserver.homebox.filemanager.client;

import de.clzserver.homebox.filemanager.onlinecheck.OnlineChecker;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineStatus;

import javax.swing.*;
import java.io.*;

/**
 * Created by Marc Janßen on 25.05.2016.
 */
public class FileClientTest {

    public static void main(String[] args) {
        OnlineStatus status = OnlineChecker.checkStatus();

        System.out.println("OnlineStatus ist: "+status);

        System.out.println("Erstelle Manager Objekt!");

        IFileManager mgr = FileManagerFactory.getInstance().getFileManager(status);

        String rFile_pointer = "build\\resources\\main\\User_Contents\\Marc\\TestFile.txt";

        boolean testRuns = true;

        while (testRuns) {

            File file = null;
            try {
                file = mgr.getFile(rFile_pointer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Habe das File: " + file.getName() + " bekommen");

            if (file.getName().endsWith(".txt")) {
                System.out.println("Das ist auch ein Textfile ^^ soweit bin ich dann ja schonmal gekommen");

                FileReader fr = null;
                try {
                    fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);

                    System.out.println("Inhalt der TextDatei");

                    String temp = "", line = br.readLine();
                    while (line != null) {
                        temp += line + "\n";
                        System.out.println("lese: " + line);
                        line = br.readLine();
                    }
                    System.out.println(temp);

                    br.close();

                    System.out.println("Versuche den Text zu ändern und zu commiten!");
                    String msg = JOptionPane.showInputDialog("Was soll im File für eine zusätzliche Zeile gespeichert werden?");

                    if (msg == null || msg.length() == 0)
                        break;

                    temp += msg;

                    File tfile = new File("TestErgebnis.txt");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tfile));

                    writer.write(temp);
                    writer.flush();
                    writer.close();

                    testRuns = mgr.commitFile(tfile, rFile_pointer, null, false, true);
                    System.out.println("Das File wurde erfolgreich geändertt");

                    tfile.delete();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
