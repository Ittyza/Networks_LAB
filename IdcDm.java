import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;


public class IdcDm {

	private static final long RANGE_SIZE = 512000 * 2; // 0.5 MB
	
    /**
     * Receive arguments from the command-line, provide some feedback and start the download.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int numberOfWorkers = 1;
        Long maxBytesPerSecond = null;

        if (args.length < 1 || args.length > 3) {
            System.err.printf("usage:\n\tjava IdcDm URL [MAX-CONCURRENT-CONNECTIONS] [MAX-DOWNLOAD-LIMIT]\n");
            System.exit(1);
        } else if (args.length >= 2) {
            numberOfWorkers = Integer.parseInt(args[1]);
            if (args.length == 3)
                maxBytesPerSecond = Long.parseLong(args[2]);
        }

       // String url = args[0];
        String url = "https://archive.org/download/Mario1_500/Mario1_500.avi";

        System.err.printf("Downloading");
        if (numberOfWorkers > 1)
            System.err.printf(" using %d connections", numberOfWorkers);
        if (maxBytesPerSecond != null)
            System.err.printf(" limited to %d Bps", maxBytesPerSecond);
        System.err.printf("...\n");

        DownloadURL(url, numberOfWorkers, maxBytesPerSecond);
    }

    /**
     * Initiate the file's metadata, and iterate over missing ranges. For each:
     * 1. Setup the Queue, TokenBucket, DownloadableMetadata, FileWriter, RateLimiter, and a pool of HTTPRangeGetters
     * 2. Join the HTTPRangeGetters, send finish marker to the Queue and terminate the TokenBucket
     * 3. Join the FileWriter and RateLimiter
     *
     * Finally, print "Download succeeded/failed" and delete the metadata as needed.
     *
     * @param url URL to download
     * @param numberOfWorkers number of concurrent connections
     * @param maxBytesPerSecond limit on download bytes-per-second
     */
    private static void DownloadURL(String url, int numberOfWorkers, Long maxBytesPerSecond) {
        
  
    	 DownloadableMetadata metadata = new DownloadableMetadata(url);
    	 
    	 LinkedBlockingQueue<Chunk> chunkQueue = new LinkedBlockingQueue<Chunk>();
    	 
    	 TokenBucket tokenBucket = new TokenBucket();
    	 
    	 
    	try {
    		
    		ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkers);
    		
    		
    		URL urlObj = new URL(url);
    		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
			long fileLength = connection.getContentLengthLong();
			
			System.out.println(fileLength);
			
			Range[] ranges = createRanges(fileLength);
			
			
			 RateLimiter ratelimiter = new RateLimiter(tokenBucket , maxBytesPerSecond);
             Thread rateLimiterThread = new Thread(ratelimiter);
             //rateLimiterThread.start();
			
			 FileWriter fileWriter = new FileWriter(metadata, chunkQueue);
	         Thread writerThread = new Thread(fileWriter);
	        // writerThread.start();
			
			
			Range currRange =  metadata.getMissingRange();
			
//			while(currRange != null) {
//				executor.submit(new HTTPRangeGetter(url, currRange, chunkQueue, tokenBucket));
//				currRange = metadata.getMissingRange();
//			}
//			
//			//executor.shutdown();
//			
//			tokenBucket.terminate();
			
		
		} catch (IOException e) {
			
		}
    	 
    	 
    	
    	
    	 
    	 
    	 /*
    	 
    	 
    	 // set up writer and start thread
         FileWriter fileWriter = new FileWriter(metadata, chunkQueue);
         Thread writerThread = new Thread(fileWriter);
         writerThread.start();
         // set up tokenbucket and start thread
         TokenBucket tokenbucket = new TokenBucket();
         
         //tokenbucket.maxAmountOfTokens = maxBytesPerSecond;
         
        
         
         
         Thread[] httpGetterThread = new Thread[numberOfWorkers];
        
         
        // int lengthOfRange = (downloadableMetaData.getSize + numberOfWorkers - 1) / numberOfWorkers;
         
         while(metadata.isCompleted() == false){
        	 
        	 Range rangeToDownload = metadata.getMissingRange();
        	 
        	 RateLimiter ratelimiter = new RateLimiter(tokenbucket , maxBytesPerSecond);
             Thread rateLimiterThread = new Thread(ratelimiter);
             rateLimiterThread.start();
             
        	 
        	 
        	 
         }
         
        
        metadata.delete();
     */
    	}
    
    private static long getLength(String url) throws IOException {
		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		//System.out.println(connection.getHeaderField(0));
		return connection.getContentLengthLong();
	}
    
    private static Range[] createRanges(long contentLength) {
		long end, offset = 0;
		int i;
		int numOfRanges = (int) (contentLength / RANGE_SIZE);
		Range[] ranges = new Range[numOfRanges];
		for (i = 0; i < ranges.length - 1; i++) {
			end = offset + RANGE_SIZE;
			ranges[i] = new Range(offset, end);
			offset = end;
			System.out.println(offset);
		}
		ranges[i] = new Range(offset, contentLength - offset);
		return ranges;
	}
    
    
}
