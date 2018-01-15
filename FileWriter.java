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
        
    	RandomAccessFile outfile = new RandomAccessFile(downloadableMetadata.getFilename(), "rw");
    	long totalBytesWritten = 0;
    	
    	 while(true){	 
    		 try {	 
				Chunk chunk = chunkQueue.take();
				outfile.seek(chunk.getOffset());
				outfile.write(chunk.getData(), 0, chunk.getSize_in_bytes());
				totalBytesWritten += chunk.getSize_in_bytes();

			} catch (InterruptedException e) {
				System.out.println("INTERUPTED EXCEPTION");
			}
    		 
    		 if(totalBytesWritten >= IdcDm.fileLength){
					System.out.println("YAAAAAAA");
					System.out.println(IdcDm.metadata.isCompleted());
					break;
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
