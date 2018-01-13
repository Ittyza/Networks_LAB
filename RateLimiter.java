/**
 * A token bucket based rate-limiter.
 *
 * This class should implement a "soft" rate limiter by adding maxBytesPerSecond tokens to the bucket every second,
 * or a "hard" rate limiter by resetting the bucket to maxBytesPerSecond tokens every second.
 */
public class RateLimiter implements Runnable {
    private final TokenBucket tokenBucket;
    private final Long maxBytesPerSecond;
    private boolean soft;


    RateLimiter(TokenBucket tokenBucket, Long maxBytesPerSecond) {
        this.tokenBucket = tokenBucket;
        this.maxBytesPerSecond = maxBytesPerSecond;
        this.soft = false;
    }

    @Override
    public void run() {
        //TODO
        
        /* Soft rate limiter - use Tread.sleep() to1 time the tokens */
        
        while(tokenBucket.terminated() == false){
            try {
            	 if(this.soft){
                     tokenBucket.add(maxBytesPerSecond);
                 } else {
                     tokenBucket.set(maxBytesPerSecond);
                 }
                Thread.sleep(1000); // adding maxBps to token bucket every second
               System.out.println("ratelimiter");
            } catch (Exception e){
            	 System.err.println("Download failed:" + e);
            }
        }
    }
}
