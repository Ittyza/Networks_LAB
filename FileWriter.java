import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;

/**
 * This class takes chunks from the queue, writes them to disk and updates the file's metadata.
 *
 * NOTE: make sure that the file interface you choose writes every update to the file's content or metadata
 *       synchronously to the underlying storage device.
 */
public class FileWriter implements Runnable {

    private final BlockingQueue<Chunk> chunkQueue;
    private DownloadableMetadata downloadableMetadata;

    FileWriter(DownloadableMetadata downloadableMetadata, BlockingQueue<Chunk> chunkQueue) {
        this.chunkQueue = chunkQueue;
        this.downloadableMetadata = downloadableMetadata;
    }

    private void writeChunks() throws IOException {
        
    	
    	// creates output stream in current folder
    	
    	RandomAccessFile outfile = new RandomAccessFile(downloadableMetadata.getFilename(), "rw");
    	// TODO: write metadeta updates
    	
    	 while(true){	 
    		 try {
    			 
				Chunk chunk = chunkQueue.take();
			
				if(chunk.getOffset() == -1){
					System.out.println("end of writing!");
    				break; 
    			 } 
				outfile.write(chunk.getData(), (int)chunk.getOffset(), chunk.getSize_in_bytes());
			} catch (InterruptedException e) {
				
			}
    	 }
    	
    	outfile.close();
    	
    }

    @Override
    public void run() {
        try {
            this.writeChunks();
        } catch (IOException e) {
            System.err.println("Error " + e.toString());
        }
    }
}
