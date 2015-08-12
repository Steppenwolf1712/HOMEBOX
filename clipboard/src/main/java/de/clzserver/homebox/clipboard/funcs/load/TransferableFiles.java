package de.clzserver.homebox.clipboard.funcs.load;

import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.File;

import de.clzserver.homebox.clipboard.funcs.Type_Factory;

public class TransferableFiles implements Transferable {

	private final List<File> content;
	
	public TransferableFiles(List<File> data) {
		content = data;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor))
			return content;
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] erg = {Type_Factory.APP_FLAV};
		return erg;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return Type_Factory.getDataFlavorTypes(flavor).equals(Type_Factory.APP_TYPE);
	}

}
