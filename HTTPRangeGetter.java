import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * A runnable class which downloads a given url.
 * It reads CHUNK_SIZE at a time and writs it into a BlockingQueue.
 * It supports downloading a range of data, and limiting the download rate using a token bucket.
 */
public class HTTPRangeGetter implements Runnable {
    static final int CHUNK_SIZE = 4096;
    private static final int CONNECT_TIMEOUT = 500;
    private static final int READ_TIMEOUT = 2000;
    private final String url;
    private final Range range;
    private final BlockingQueue<Chunk> outQueue;
    private TokenBucket tokenBucket;

    HTTPRangeGetter(
            String url,
            Range range,
            BlockingQueue<Chunk> outQueue,
            TokenBucket tokenBucket) {
        this.url = url;
        this.range = range;
        this.outQueue = outQueue;
        this.tokenBucket = tokenBucket;
    }


    
    private void downloadRange() throws IOException, InterruptedException {
     
    	URL url = new URL(this.url);
    	HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
    	httpConnection.setRequestMethod("GET");
        httpConnection.setReadTimeout(READ_TIMEOUT);
        httpConnection.setConnectTimeout(CONNECT_TIMEOUT);
        String rangeString = String.format("bytes=%d-%d", range.getStart() , range.getEnd());
        httpConnection.setRequestProperty("Range", rangeString);
        httpConnection.connect();
        BufferedInputStream  dataInputStream = new  BufferedInputStream(httpConnection.getInputStream());
        int code = httpConnection.getResponseCode();
        
        if(code == 200 || code == 206){
        	byte data[] = new byte[CHUNK_SIZE];
            int bytesRead = 0;
            long offset = range.getStart();
            int totalBytesReadFromRange = 0;
            while ((bytesRead = dataInputStream.read(data, 0, CHUNK_SIZE)) != -1) {
                outQueue.add(new Chunk(data, offset, bytesRead));
                offset += bytesRead;
                tokenBucket.take(CHUNK_SIZE);
                totalBytesReadFromRange += bytesRead;
            }
            // added because of the -1
           if(range.isLastRange){
        	   totalBytesReadFromRange++;
           }
            
            range.inWorker = false;
          //  System.out.println(totalBytesReadFromRange + " : " + range.getLength());
            if(totalBytesReadFromRange == this.range.getLength()){
            	range.isWritten = true;
            //	System.out.println("JONNY WE GOT THE ENTIRE RANGE " + range.index);
            	IdcDm.metadata.addRange(range);
            } else {
            //THROW BACK INTO EXECUTOER 
            //	System.out.println("range not downloaded");
            	IdcDm.executor.submit(this);
            }	
        }
        
        tokenBucket.terminate();
        httpConnection.disconnect();
        dataInputStream.close();
        
    }

    @Override
    public void run() {
        try {
            this.downloadRange();
        } catch (IOException | InterruptedException e) {
           try {
			wait();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
            System.err.println("Download range "+ this.range +" failed");
        }
    }
}
