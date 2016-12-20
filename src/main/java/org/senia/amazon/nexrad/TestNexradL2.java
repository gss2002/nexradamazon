package org.senia.amazon.nexrad;

import org.geotools.gc.GridCoverage;
import org.senia.nexrad.DecodeRadialDatasetSweepHeader;
import org.senia.nexrad.RadarHashtables;
import org.senia.nexrad.RadialDatasetSweepRemappedRaster;
import org.senia.nexrad.SupportedDataType;
import org.senia.nexrad.WCTUtils;

import ucar.nc2.constants.FeatureType;
import ucar.nc2.dt.RadialDatasetSweep;

public class TestNexradL2 {
    public static enum ExportFormatType { UNKNOWN, NATIVE, VECTOR, RASTER }; 
    public static enum ExportFormat {
        NATIVE, RAW_NETCDF,
        SHAPEFILE, GEOJSON, WKT, CSV,  
        ARCINFOASCII, ARCINFOBINARY, GEOTIFF_GRAYSCALE_8BIT, GEOTIFF_32BIT, 
        GRIDDED_NETCDF, VTK, WCT_RASTER_OBJECT_ONLY,
        KMZ
    }

	public static void main(String[] args) {
		SupportedDataType currentDataType;
		// TODO Auto-generated method stub
		if (currentDataType == SupportedDataType.RADIAL) {

			RadialDatasetSweep radialDataset = (RadialDatasetSweep) TypedDatasetFactory.open(FeatureType.RADIAL, dataURL.toString(),
					WCTUtils.getSharedCancelTask(), new StringBuilder());

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

			ExportFormatType outputType;
			if (outputType == ExportFormatType.RASTER) {
				Object radialDatasetRaster;
				if (radialDatasetRaster == null) {
					radialDatasetRaster = new RadialDatasetSweepRemappedRaster();
					radialDatasetRaster.addDataDecodeListener(this);
				}

				String variableName = radialExportVariable == null ? radialDataset.getDataVariables().get(0).toString()
						: radialExportVariable;
				if (exportRadialFilter.getExtentFilter() == null) {
					bounds = header.getNexradBounds();
				} else {
					bounds = exportRadialFilter.getExtentFilter();
				}

				if (exportGridCellSize > 0.0) {

					// calculate the raster size needed in decimal degrees
					// find number grid cells needed for 'long' side of grid
					double longSide = (bounds.getWidth() > bounds.getHeight()) ? bounds.getWidth() : bounds.getHeight();
					this.exportGridSize = (int) Math.round(longSide / exportGridCellSize);
					// rasterizer.setSize(new Dimension(exportGridSize,
					// exportGridSize));
					double boundsDiff = longSide - (exportGridSize * exportGridCellSize);
					bounds.setRect(bounds.getX() - boundsDiff / 2.0, bounds.getY() + boundsDiff / 2.0,
							bounds.getWidth() + boundsDiff, bounds.getHeight() + boundsDiff);

					logger.fine("SETTING GRID CELL SIZE: " + exportGridCellSize);
					logger.fine("SETTING GRID SIZE: " + exportGridSize);
					logger.fine("BOUNDS DIFF: " + boundsDiff);
				}
				radialDatasetRaster.setHeight(exportGridSize);
				radialDatasetRaster.setWidth(exportGridSize);

				radialDatasetRaster.setVariableName(variableName);
				radialDatasetRaster.setSweepIndex(radialExportCut);
				radialDatasetRaster.setWctFilter(wctFilter);
				dataURL = WCTDataUtils.scan(dataURL, scannedFile, useWctCache, true, SupportedDataType.RADIAL);
				if (Double.isNaN(radialExportCappiHeightInMeters)) {
					radialDatasetRaster.process(dataURL.toString(), bounds);
				} else {
					radialDatasetRaster.processCAPPI(dataURL.toString(), bounds,
							new double[] { radialExportCappiHeightInMeters }, radialExportCappiInterpolationType);
				}

				genericRaster = radialDatasetRaster;

				if (exportGridSmoothFactor > 0) {
					radialDatasetRaster.setSmoothingFactor(exportGridSmoothFactor);
					GridCoverage gc = radialDatasetRaster.getGridCoverage(0);
					java.awt.image.RenderedImage renderedImage = gc.getRenderedImage();
					Raster raster = renderedImage.getData();

					radialDatasetRaster.setWritableRaster((WritableRaster) (renderedImage.getData()));
					rasterizer.setWritableRaster((WritableRaster) raster);
				}
			}
		}
	}

}
