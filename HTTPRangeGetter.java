import java.io.BufferedInputStream;
import java.io.IOException;
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
        //TODO

    	URL url = new URL(this.url);
    	HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
    	// check response code 
    	
    	httpConnection.setRequestMethod("GET");
        httpConnection.setReadTimeout(READ_TIMEOUT);
        httpConnection.setConnectTimeout(CONNECT_TIMEOUT);
        httpConnection.setRequestProperty("Range", "bytes="+range.getStart() +"-"+range.getEnd());
        httpConnection.connect();
        
       
        BufferedInputStream  dataInputStream = new  BufferedInputStream(httpConnection.getInputStream());
        
        byte data[] = new byte[CHUNK_SIZE];
        int numOfBytesRead = 0;
        long offset = range.getStart();

        while ((numOfBytesRead = dataInputStream.read(data, 0, CHUNK_SIZE)) != -1)
        {
            outQueue.add(new Chunk(data, offset, numOfBytesRead));
            offset += numOfBytesRead;
        }
        
        
        dataInputStream.close();
        
    }

    @Override
    public void run() {
        try {
            this.downloadRange();
        } catch (IOException | InterruptedException e) {
           
            System.err.println("Download range "+ this.range +" failed");
        }
    }
}
