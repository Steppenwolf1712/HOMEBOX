package de.clzserver.homebox.filemanager.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.registry.RMI_Starter;
import de.clzserver.homebox.registry.RegistryHandle;

public class RemoteFileServer_Starter extends RMI_Starter {

	private RemoteFileServer fmngr;

	public RemoteFileServer_Starter() {
		super(null, IFileServer.class);
	}

	public RemoteFileServer getServer() {
		return fmngr;
	}

	@Override
	public void doCustomRmiHandling() {
		try {
			// System.setProperty("java.rmi.server.hostname", "127.0.0.1");

			HBPrinter.getInstance().printMSG(this.getClass(), "Eröffne Bibliothek!");
			fmngr = new RemoteFileServer();

			HBPrinter.getInstance().printMSG(this.getClass(), "Starte Dienst!");
			IFileServer stub = (IFileServer) UnicastRemoteObject
					.exportObject(fmngr, IFileServer.RMI_LAN_PORT);

			Registry registry = RegistryHandle.getInstance().getRegistry();

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

	}

	public static boolean shutDown(RemoteFileServer currentServer) {
		try {
			Registry registry = RegistryHandle.getInstance().getRegistry();

			registry.unbind(IFileServer.SERVICE_NAME);

			return UnicastRemoteObject.unexportObject(currentServer, false);
		} catch (RemoteException e) {
			HBPrinter.getInstance().printError(RemoteFileServer_Starter.class,
					"Konnte den Service des Filemanagers nicht stoppen!", e);
		} catch (NotBoundException e) {
			HBPrinter.getInstance().printError(RemoteFileServer_Starter.class,
					"Der Service des Filemanagers ist zurzeit nicht gebunden und kann deshalb nicht von der Registryy entfernt werden!", e);
		}
		return false;
	}
}
