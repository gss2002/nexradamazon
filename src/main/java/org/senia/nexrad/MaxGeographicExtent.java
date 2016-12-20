package org.senia.nexrad;

public class MaxGeographicExtent {



	   /**
	    * Get Lat/Lon bounds for a Nexrad Level-3 Product given the header object
	    */
	   public static java.awt.geom.Rectangle2D.Double getNexradExtent(NexradHeader header) {
	      return getNexradExtent(header.getLat(), header.getLon(), header.getProductCode());
	   }
	   
	   /**
	    * Get Lat/Lon bounds for Default 230km Range Products (Base Reflectivity, etc...)
	    */
	   public static java.awt.geom.Rectangle2D.Double getNexradExtent(double wsrLat, double wsrLon) {
	      return getNexradExtent(wsrLat, wsrLon, 19);
	   }
	   
	   /**
	    * Get Lat/Lon bounds for a provided product code
	    */
	   public static java.awt.geom.Rectangle2D.Double getNexradExtent(double wsrLat, double wsrLon, int pcode) {

	      AlbersEquidistant radarProjection = new AlbersEquidistant(wsrLat+1.0,wsrLat-1.0,wsrLat,wsrLon,1.0);
	      
	      double[] wsrAlbers = radarProjection.project(wsrLon, wsrLat);
	      
	      // Return a rectangle describing the 230km radius of the WSR Radars
	      // Must check 4 corners to get true rectangle of desired projection - Start with LL Corner
	      double range = 245000.0;
	      // Check for products with a range of 460km
	      if (pcode == NexradHeader.LEVEL2 || 
	          pcode == NexradHeader.LEVEL2_REFLECTIVITY || 
	          pcode == NexradHeader.LEVEL2_VELOCITY || 
	          pcode == NexradHeader.LEVEL2_SPECTRUMWIDTH ||
	          pcode == NexradHeader.LEVEL2_DIFFERENTIALREFLECTIVITY ||
	          pcode == NexradHeader.LEVEL2_CORRELATIONCOEFFICIENT ||
	          pcode == NexradHeader.LEVEL2_DIFFERENTIALPHASE ||
	          pcode == NexradHeader.L3PC_BASE_REFLECTIVITY_248NM || 
	          pcode == NexradHeader.L3PC_COMPOSITE_REFLECTIVITY_248NM_8LVL || 
	          pcode == NexradHeader.L3PC_COMPOSITE_REFLECTIVITY_248NM_16LVL ||
	          pcode == NexradHeader.L3PC_LONG_RANGE_BASE_REFLECTIVITY_8BIT ||
	          pcode == NexradHeader.L3PC_LONG_RANGE_BASE_VELOCITY_8BIT) {
	         range*=2;
	      } 
	      else if (pcode == NexradHeader.L3PC_VELOCITY_32NM || 
	          pcode == NexradHeader.L3PC_SPECTRUM_WIDTH_32NM) {
	         range/=3.875;
	      }
	      else if (pcode == NexradHeader.XMRG) {
	         // return whole world extent until we know better
	         return new java.awt.geom.Rectangle2D.Double(-125.0, 22.0, 58.0, 35.0);   
	      }
	      
	//System.out.println("CALCNEXRADEXTENT ::: PCODE: "+pcode+"  range:"+range);      
	      
	      
	      double[] cornersX = new double[4];
	      double[] cornersY = new double[4];
	      cornersX[0] = wsrAlbers[0] - range; // LL X
	      cornersY[0] = wsrAlbers[1] - range; // LL Y
	      cornersX[1] = wsrAlbers[0] - range; // UL X
	      cornersY[1] = wsrAlbers[1] + range; // UL Y
	      cornersX[2] = wsrAlbers[0] + range; // UR X
	      cornersY[2] = wsrAlbers[1] + range; // UR Y
	      cornersX[3] = wsrAlbers[0] + range; // LR X
	      cornersY[3] = wsrAlbers[1] - range; // LR Y

	      double[][] cvtBounds = new double[4][2];
	      for (int i=0; i<4; i++) {
	         cvtBounds[i] = radarProjection.unproject(cornersX[i], cornersY[i]);         
	      }

	      // Get min and max of x and y coordinates:  this defines the corners in new projection
	      // We only have to test two points to find the max and mins
	      double minX = (cvtBounds[0][0] < cvtBounds[1][0]) ? cvtBounds[0][0] : cvtBounds[1][0];
	      double minY = (cvtBounds[0][1] < cvtBounds[3][1]) ? cvtBounds[0][1] : cvtBounds[3][1];
	      double maxX = (cvtBounds[2][0] > cvtBounds[3][0]) ? cvtBounds[2][0] : cvtBounds[3][0];
	      double maxY = (cvtBounds[1][1] > cvtBounds[2][1]) ? cvtBounds[1][1] : cvtBounds[2][1];

//	      System.out.println("MIN" + minX + " , " + minY);
//	      System.out.println("MAX" + maxX + " , " + maxY);
	      
	      return (new java.awt.geom.Rectangle2D.Double(minX, minY, Math.abs(maxX-minX), Math.abs(maxY-minY)));
	      
	   }

	}
