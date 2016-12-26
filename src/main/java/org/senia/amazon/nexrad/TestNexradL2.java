package org.senia.amazon.nexrad;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Formatter;

import org.geotools.gc.GridCoverage;
import org.senia.nexrad.DecodeException;
import org.senia.nexrad.DecodeHintNotSupportedException;
import org.senia.nexrad.DecodeRadialDatasetSweep;
import org.senia.nexrad.DecodeRadialDatasetSweepHeader;
import org.senia.nexrad.RadarHashtables;
import org.senia.nexrad.RadialDatasetSweepRemappedRaster;
import org.senia.nexrad.StreamingProcess;
import org.senia.nexrad.SupportedDataType;
import org.senia.nexrad.WCTDataUtils;
import org.senia.nexrad.WCTExportException;
import org.senia.nexrad.WCTExportNoDataException;
import org.senia.nexrad.WCTRasterExport;
import org.senia.nexrad.WCTRasterExport.GeoTiffType;
import org.senia.nexrad.WCTRasterizer;
import org.senia.nexrad.WCTUtils;

import ucar.ma2.InvalidRangeException;
import ucar.nc2.constants.FeatureType;
import ucar.nc2.dt.RadialDatasetSweep;
import ucar.nc2.ft.FeatureDatasetFactoryManager;
import ucar.nc2.util.CancelTask;

public class TestNexradL2 {
    public static enum ExportFormatType { UNKNOWN, NATIVE, VECTOR, RASTER }; 
    public static enum ExportFormat {
        NATIVE, RAW_NETCDF,
        SHAPEFILE, GEOJSON, WKT, CSV,  
        ARCINFOASCII, ARCINFOBINARY, GEOTIFF_GRAYSCALE_8BIT, GEOTIFF_32BIT, 
        GRIDDED_NETCDF, VTK, WCT_RASTER_OBJECT_ONLY,
        KMZ
    }
    
	public static void main(String [] args) {
		try {
			doMosaic();
		} catch (DecodeException | WCTExportException | IOException | ParseException
				| DecodeHintNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void doMosaic() throws DecodeException, WCTExportNoDataException, WCTExportException, IOException, ParseException, DecodeHintNotSupportedException {
        
        // Setup files
        URL[] urls = new URL[] {
            new File("/Users/gsenia/radardata/KCLE.ar2v").toURI().toURL(),
        };
        
        // Setup rasterizer
        final WCTRasterizer rasterizer = new WCTRasterizer(1200, 1200);
        rasterizer.setAttName("value");
        // Setup geographic extent of interest
        // -83.5, 31, -77, 37.5
        rasterizer.setBounds(new Rectangle.Double(-83.5, 31, 6.5, 6.5));
        rasterizer.setEqualCellsize(true);

        // Loop through files, decode and rasterize onto single grid keeping the max value at each pixel
        for (URL url : urls) {

            // Create a bogus CancelTask to send in to the data decoder
            CancelTask emptyCancelTask = new CancelTask() {
				@Override
                public boolean isCancel() {
                    return false;
                }
				@Override
                public void setError(String arg0) {
                }
				@Override
				public void setProgress(String arg0, int arg1) {
				}
            };


            RadialDatasetSweep radialDataset = (RadialDatasetSweep) FeatureDatasetFactoryManager.open(
                        FeatureType.RADIAL, 
                        url.toString(), emptyCancelTask, new Formatter());


            
            DecodeRadialDatasetSweepHeader header = new DecodeRadialDatasetSweepHeader();
            header.setRadialDatasetSweep(radialDataset);
            DecodeRadialDatasetSweep radialDatasetDecoder = new DecodeRadialDatasetSweep(header);
            
            // This sets the 'moment' or variable to process
            RadialDatasetSweep.RadialVariable radialVar = (RadialDatasetSweep.RadialVariable) radialDataset.getDataVariable("Reflectivity");
            radialDatasetDecoder.setRadialVariable(radialVar);

            
//            USE THIS OPTION TO FILTER ON ATTRIBUTES SUCH AS VALUE RANGE, HEIGHT, ETC...
//            radialDatasetDecoder.setDecodeHint("nexradFilter", exportRadialFilter);
            
//            THIS OPTION SETS THE SWEEPS TO PROCESS TO ONLY THE FIRST (LOWEST)
            radialDatasetDecoder.setDecodeHint("startSweep", new Integer(0));
            radialDatasetDecoder.setDecodeHint("endSweep", new Integer(0));

            radialDatasetDecoder.decodeData(new StreamingProcess[] { rasterizer });
            
        }
        
        WCTRasterExport export = new WCTRasterExport();
		// Export mosaic grid to ASCII GRID file
        try {
			export.saveGeoTIFF(new File("/Users/gsenia/KCLE.geotiff"), rasterizer, GeoTiffType.TYPE_32_BIT);
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			export.saveNetCDF(new File("/Users/gsenia/KCLE"), rasterizer);
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        export.saveAsciiGrid(new File("/Users/gsenia/KCLE.asc"), rasterizer);
        export.saveGrADSBinary(new File("/Users/gsenia/KCLE.grads"), rasterizer);

        
    }

}
