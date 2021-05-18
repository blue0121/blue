package blue.base.core.common;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public class Tuple {
    private Object[] cacheObjects;

	public Tuple(Object...objects) {
	    this.cacheObjects = new Object[objects.length];
	    for (int i = 0; i < objects.length; i++) {
	        this.cacheObjects[i] = objects[i];
        }
	}

	@SuppressWarnings("unchecked")
	public <T> T getN(int index) {
	    if (this.cacheObjects.length < index) {
	        return null;
        }
	    return (T) this.cacheObjects[index - 1];
    }

	public <T> T getFirst() {
        return getN(1);
    }

    public <T> T getSecond() {
        return getN(2);
    }

    public <T> T getThird() {
        return getN(3);
    }

}
