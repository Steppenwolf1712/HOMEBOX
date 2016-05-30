package de.clzserver.homebox.remoteprocess.client.gui;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.remoteprocess.client.gui.table.ModelRow;
import de.clzserver.homebox.remoteprocess.client.gui.table.ProcessModel;
import de.clzserver.homebox.remoteprocess.client.rmi.ProcessServerFactory;
import de.clzserver.homebox.remoteprocess.remote.IProcessServer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Marc Janßen on 29.05.2016.
 */
public class ProcessManager extends JFrame implements ActionListener, ListSelectionListener {


    private static final String S_BTN_UPDATE = "Update Online-Status";
    private static final String S_BTN_TURN = "idle";
    private static final String S_TITLE = "Prozess-Manager";
    private IProcessServer server;

    private JButton btn_update;
    private JButton btn_turnOnOff_process;
    private JTable table;
    private ProcessModel model;


    public ProcessManager() {
        super(S_TITLE);
        init();
    }

    private void init() {
        server = ProcessServerFactory.getInstance().getProcessServer();

        this.setLayout(new BorderLayout(8,5));

        model = new ProcessModel();
        table = new JTable(model);
        ListSelectionModel temp = table.getSelectionModel();
        temp.addListSelectionListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(temp);
        add(table, BorderLayout.CENTER);

        JPanel botPan = new JPanel(new BorderLayout(5,5));

        btn_update = new JButton(S_BTN_UPDATE);
        btn_update.addActionListener(this);
        botPan.add(btn_update, BorderLayout.WEST);

        btn_turnOnOff_process = new JButton(S_BTN_TURN);
        btn_turnOnOff_process.setEnabled(false);
        btn_turnOnOff_process.addActionListener(this);
        botPan.add(btn_turnOnOff_process, BorderLayout.EAST);

        add(botPan, BorderLayout.SOUTH);

        initModel();
    }

    private void initModel() {
        String[] processList = new String[0];
        try {
            processList = server.getAvailableApps();
        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Konnte die Liste der Verfügbaren Apps nicht abrufen!", e);
        }

        ModelRow row;
        for (String p: processList) {
            row = new ModelRow(p, server);
            model.addModelRow(row);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_update) {
            model.updateRows();
        } else if (e.getSource() == btn_turnOnOff_process) {
            model.getModelRow(table.convertRowIndexToModel(table.getSelectedRow())).turnState();
        }
    }



    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = e.getFirstIndex();
        ModelRow row = model.getModelRow(index);
        btn_turnOnOff_process.setText(row.getBtnText());
        btn_turnOnOff_process.setEnabled(true);
    }
}
