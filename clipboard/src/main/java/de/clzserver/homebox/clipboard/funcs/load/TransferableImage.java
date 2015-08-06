package de.clzserver.homebox.clipboard.funcs.load;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class TransferableImage implements Transferable{

	private Image i = null;
	
	public TransferableImage(Image img) {
		this.i = img;
	}

	@Override
	public Object getTransferData(DataFlavor df)
			throws UnsupportedFlavorException, IOException {

		if (df.equals(DataFlavor.imageFlavor) && i != null) {
			return i;
		} else 
			throw new UnsupportedFlavorException(df);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavs = new DataFlavor[1];
		flavs[0] = DataFlavor.imageFlavor;
		return flavs;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flav) {
		DataFlavor[] flavs = getTransferDataFlavors();
		for (int j = 0; j<flavs.length; j++) {
			if (flav.equals(flavs[j])) {
				return true;
			}
		}
		return false;
	}
	
	
}
