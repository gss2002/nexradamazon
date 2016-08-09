package org.senia.amazon.nexrad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

public class AwsS3 {

	 public void downloadNexrad(String path) throws IOException {

	        /*
	         * Get AWS Anonymous Creds for NexradL2 Bucket
	         */
	        AWSCredentials creds = new AnonymousAWSCredentials();



	        Region eastRegion = Region.getRegion(Regions.US_EAST_1);
	        String nexradL2Bucket = "noaa-nexrad-level2";
	
	        AmazonS3 s3 = new AmazonS3Client(creds);
	        s3.setRegion(eastRegion);

	        ListObjectsRequest lor = new ListObjectsRequest().withBucketName(nexradL2Bucket).withPrefix(path);
	        ObjectListing objectListing = s3.listObjects(lor);
	        System.out.println(objectListing.getBucketName());
	        System.out.println(objectListing.getObjectSummaries().size());
	        int lastObject = objectListing.getObjectSummaries().size();
	        String radarObj = objectListing.getObjectSummaries().get(lastObject-1).getKey();
	        for (S3ObjectSummary summary: objectListing.getObjectSummaries()) {
	        
	            System.out.println(summary.getKey());
	        }
	        S3Object obj=s3.getObject(new GetObjectRequest(nexradL2Bucket,radarObj));
	        File file=new File("/tmp/AWSStorage/"+radarObj+".ar2v");

	        // if the directory does not exist, create it
	        if (!file.getParentFile().exists()) {
	            file.getParentFile().mkdirs();
	        }


	        try {
	            IOUtils.copy(obj.getObjectContent(), new FileOutputStream(file));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }	        
	        System.out.println(radarObj);
	    
	 }
}
