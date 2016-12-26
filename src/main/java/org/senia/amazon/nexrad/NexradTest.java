package org.senia.amazon.nexrad;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;

import org.senia.nexrad.DecodeException;
import org.senia.nexrad.DecodeRadialDatasetSweep;
import org.senia.nexrad.DecodeRadialDatasetSweepHeader;
import org.senia.nexrad.FileScanner;
import org.senia.nexrad.GridDatasetRemappedRaster;
import org.senia.nexrad.Level2Transfer;
import org.senia.nexrad.NexradHeader;
import org.senia.nexrad.RadarHashtables;
import org.senia.nexrad.RadialDatasetSweepRemappedRaster;
import org.senia.nexrad.StreamingRadialDecoder;
import org.senia.nexrad.SupportedDataType;
import org.senia.nexrad.WCTFilter;
import org.senia.nexrad.WCTGridCoverageSupport;
import org.senia.nexrad.WCTRasterExport;
import org.senia.nexrad.WCTRasterizer;
import org.senia.nexrad.WCTTransfer;
import org.senia.nexrad.WCTUtils;


import ucar.nc2.constants.FeatureType;
import ucar.nc2.dt.RadialDatasetSweep;
import ucar.nc2.ft.FeatureDatasetFactoryManager;

public class NexradTest {
    private FileScanner scannedFile;
    private NexradHeader header;
    private StreamingRadialDecoder decoder;
    private RadialDatasetSweep radialDataset;
    private DecodeRadialDatasetSweepHeader radialDatasetHeader;
    private DecodeRadialDatasetSweep radialDatasetDecoder;
    private GridDatasetRemappedRaster grid;
    private RadialDatasetSweepRemappedRaster radialDatasetRaster;
    private WCTFilter wctFilter;
    private WCTRasterizer rasterizer;
    private WCTGridCoverageSupport gcSupport = new WCTGridCoverageSupport();
    private WCTRasterExport rasterExport;
	public static void main(String[] args) {
		NexradTest rad = new NexradTest();
		rad.radarRun();
		// TODO Auto-generated method stub

	}

	public void radarRun() {

			URL dataURL = null;
			try {
				dataURL = new File("/Users/gsenia/radardata/KCLE.ar2v").toURI().toURL();
				System.out.println(dataURL);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
       
            URL dataURL2 = null;
			// HARD CODE - for right now, all Level2 files will be checked for "AR2V0001" partial BZIP format
            try {
				dataURL2 = Level2Transfer.decompressAR2V0001(dataURL, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				scannedFile.scanURL(dataURL2);
			} catch (MalformedURLException | UnsupportedEncodingException | DecodeException | SQLException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(scannedFile.isZCompressed());
      /*  
        scannedFile = new FileScanner();
        scannedFile.
        currentDataType = scannedFile.getLastScanResult().getDataType();

		if (currentDataType == SupportedDataType.RADIAL) {

			RadialDatasetSweep radialDataset = (RadialDatasetSweep) FeatureDatasetFactoryManager
					.open(FeatureType.RADIAL, dataURL.toString(), WCTUtils.getSharedCancelTask(), new StringBuilder());

			DecodeRadialDatasetSweepHeader radialDatasetHeader;
			if (radialDatasetHeader == null) {
				radialDatasetHeader = new DecodeRadialDatasetSweepHeader();
			}
			radialDatasetHeader.setRadialDatasetSweep(radialDataset);
			DecodeRadialDatasetSweepHeader header = radialDatasetHeader;

			String urlString = dataURL.toString();
			if (radialDatasetHeader.getICAO().equals("XXXX")) {
				int idx = urlString.lastIndexOf('/');
				String icao = urlString.substring(idx + 1, idx + 5);
				if (icao.equals("6500")) {
					icao = urlString.substring(idx + 5, idx + 9);
				}

				System.err.println("SETTING SITE FROM FILENAME FOR: " + icao);

				RadarHashtables nxhash = RadarHashtables.getSharedInstance();
				radialDatasetHeader.setStationInfo(icao, nxhash.getLat(icao), nxhash.getLon(icao),
						nxhash.getElev(icao));
			}

		}
		*/
	}
}
