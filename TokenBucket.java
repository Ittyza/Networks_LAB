/**
 * A Token Bucket (https://en.wikipedia.org/wiki/Token_bucket)
 *
 * This thread-safe bucket should support the following methods:
 *
 * - take(n): remove n tokens from the bucket (blocks until n tokens are available and taken)
 * - set(n): set the bucket to contain n tokens (to allow "hard" rate limiting)
 * - add(n): add n tokens to the bucket (to allow "soft" rate limiting)
 * - terminate(): mark the bucket as terminated (used to communicate between threads)
 * - terminated(): return true if the bucket is terminated, false otherwise
 *
 */
class TokenBucket {

    public long tokens;
    public int maxAmountOfTokens;
    public boolean isTerminated;
    
    TokenBucket() {
        this.tokens = 0;
        this.maxAmountOfTokens = 0;
        this.isTerminated = false;
    }

    void take(long i_tokens) {
        this.tokens -= i_tokens;
        
    }

    void terminate() {
        this.isTerminated = true;
    }

    boolean terminated() {
        return this.isTerminated;
    }

    void set(long i_tokens) {
        this.tokens = i_tokens;
    }

    void add(long i_tokens) {
        if(i_tokens > this.maxAmountOfTokens){
            this.tokens = maxAmountOfTokens;
        } else {
           this.tokens += i_tokens;
        }
    }
}



