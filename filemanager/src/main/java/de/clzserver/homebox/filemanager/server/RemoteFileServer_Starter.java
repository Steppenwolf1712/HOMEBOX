package de.clzserver.homebox.filemanager.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.filemanager.remote.base.RMI_Starter;
import de.clzserver.homebox.config.HBPrinter;

public class RemoteFileServer_Starter extends RMI_Starter {

	public static int RMI_LAN_PORT = 54123;
	
	public RemoteFileServer_Starter() {
		super(IFileServer.class);
	}

	@Override
	public void doCustomRmiHandling() {
		try {
			// System.setProperty("java.rmi.server.hostname", "127.0.0.1");

			HBPrinter.getInstance().printMSG(this.getClass(), "Eröffne Bibliothek!");
			IFileServer fmngr = new RemoteFileServer();

			HBPrinter.getInstance().printMSG(this.getClass(), "Starte Dienst!");
			IFileServer stub = (IFileServer) UnicastRemoteObject
					.exportObject(fmngr, RMI_LAN_PORT);

			Registry registry = LocateRegistry.createRegistry(1099);

			HBPrinter.getInstance().printMSG(
					this.getClass(),
					"Veröffentliche Bibliothek-Dienst unter "
							+ IFileServer.SERVICE_NAME + "!");
			registry.rebind(IFileServer.SERVICE_NAME, stub);

			HBPrinter.getInstance().printMSG(this.getClass(), "Server hochgefahren!");
		} catch (Exception e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Konnte Server nicht starten!", e);
		}
		HBPrinter.getInstance().printMSG(this.getClass(), "Server terminiert!");

	}
}
