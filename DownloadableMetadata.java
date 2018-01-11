import java.util.ArrayList;

/**
 * Describes a file's metadata: URL, file name, size, and which parts already downloaded to disk.
 *
 * The metadata (or at least which parts already downloaded to disk) is constantly stored safely in disk.
 * When constructing a new metadata object, we first check the disk to load existing metadata.
 *
 * CHALLENGE: try to avoid metadata disk footprint of O(n) in the average case
 * HINT: avoid the obvious bitmap solution, and think about ranges...
 */

class DownloadableMetadata {
    private final String metadataFilename;
    private String filename;
    private String url; 
 
    // added by me 
    private double size;
    private ArrayList<Range> downloadedRanges;
    
    DownloadableMetadata(String url) {
        this.url = url;
        this.filename = getName(url);
        this.metadataFilename = getMetadataName(filename);
        
        this.size = 0; 
        downloadedRanges = new ArrayList<Range>();
    }

    private static String getMetadataName(String filename) {
        return filename + ".metadata";
    }

    private static String getName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    void addRange(Range range) {
        //TODO
    	this.downloadedRanges.add(range);
    }

    String getFilename() {
        return filename;
    }

    boolean isCompleted() {
        //TODO
    	if(this.size == 100){
    		return true;
    	} else {
    		return false;
    	}
    }

    void delete() {
        //TODO
    }

    Range getMissingRange() {
        //TODO
    	return null;
    }

    String getUrl() {
        return url;
    }
}
