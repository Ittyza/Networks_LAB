import java.io.File;
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
    	long totalBytesWritten = 0;
    	 while(true){	 
    		 try {
    			 
				Chunk chunk = chunkQueue.take();
//			
//				if(chunk.getOffset() == -1){
//					System.out.println("end of writing!");
//    				break; 
//    			 } 
				//System.out.println(totalBytesWritten + " : " + IdcDm.fileLength);
				
				outfile.seek(chunk.getOffset());
				outfile.write(chunk.getData(), 0, chunk.getSize_in_bytes());
				totalBytesWritten += chunk.getSize_in_bytes();
				//System.out.println("FileWriter "+totalBytesWritten + " : " + IdcDm.fileLength);
				
				
				
			} catch (InterruptedException e) {
				System.out.println("INTERUPTED EXCEPTION");
			}
    		 
    		 if(totalBytesWritten >= IdcDm.fileLength){
					System.out.println("YAAAAAAA");
					break;
				} else {
					//System.out.println("NOO");
					//System.out.println("total Written " + totalBytesWritten + " :" + IdcDm.fileLength);
				}
    		 
    	 }
    	System.out.println("CLOSE FILE");
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
